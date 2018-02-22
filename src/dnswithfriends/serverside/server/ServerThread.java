package dnswithfriends.serverside.server;

import java.net.Socket;
import java.net.UnknownHostException;

import java.util.List;
import java.util.ArrayList;

import java.text.DateFormat;
import java.text.ParseException;

import java.io.PrintStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import dnswithfriends.serverside.data.Friend;
import dnswithfriends.serverside.data.Entry;

import dnswithfriends.util.Util;

public class ServerThread extends Thread{
  static private int count = 0;
  private Socket connection = null;
  private PrintStream outputToClient = null;
  private BufferedReader inputFromClient = null;
  private String message = null;
  private Server server = null;

  public ServerThread(Server server, Socket conn) throws IOException{
    this.server = server;
    this.connection = conn;
    this.outputToClient = new PrintStream(this.connection.getOutputStream());
    this.inputFromClient = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
    setName("ServerThread, aka, client #" + ServerThread.count++);
  }

  public void setMessage(File f) throws IOException{
    List<String> lines = java.nio.file.Files.readAllLines(f.toPath());
    String message = "";
    for(String s : lines){
      message += s;
    }
    this.message = message;
  }

  public void send() throws IOException{
    this.outputToClient.println(this.message);
  }

  @Override
  public void run(){
    while(true){
      String request = null;
      try{
        request = this.inputFromClient.readLine();
      }catch(IOException ioE){
        System.err.println("Could not read from the new client.");
        ioE.printStackTrace();
        System.exit(20);
      }
      String tokens[] = request.split("\n");
      if(tokens[0].equalsIgnoreCase("DNS")){
        dnsRequest(tokens);
      }else if(tokens[0].equalsIgnoreCase("FRIEND")){
        friendRequest(tokens);
      }else{
        System.err.println("Unkown request format:\n" + request);
        System.err.println("Skipping.");
      }
    }
  }

  public void disconnect(String reason) throws IOException{
    this.message = "CLOSED\n" + reason;
    send();
  }

  private void dnsRequest(String tokens[]){
    List<Entry> list = this.server.getHosts();
    String lineTokens[] = tokens[1].split(" ");

    if(lineTokens[0].equals("CONSULT")){
      boolean ip = lineTokens[1].equals("-ip");
      boolean dns = !ip;

      boolean deep = lineTokens[2].equals("-d");
      Integer howDeep = null;
      boolean iter = lineTokens[2].equals("-i");

      Integer numCon = 1;
      if(deep){
        howDeep = Integer.parseInt(lineTokens[3]);

        if(!lineTokens[4].equals("-n")){
          System.err.println("Invalid request (\""+ tokens[1] + "\"");
          System.err.println("Skipping.");
          return;
        }

        numCon = Integer.parseInt(lineTokens[5]);
      }else if(iter){
        if(!lineTokens[3].equals("-n")){
          System.err.println("Invalid request (\""+ tokens[1] + "\"");
          System.err.println("Skipping.");
          return;
        }

        numCon = Integer.parseInt(lineTokens[4]);
      }else{
          System.err.println("Invalid request (\""+ tokens[1] + "\"");
          System.err.println("Unkown mode: " + lineTokens[2]);
          System.err.println("Skipping.");
          return;
      }

      List<String> consults = new ArrayList<String>();
      List<String> answers = new ArrayList<String>();
      for(int i=2 ; i<numCon+2; ++i){
        consults.add(tokens[i]);
      }

      boolean found = false;
      if(ip){
        for(String consult : consults){
          found = false;
          for(Entry e : list){
            if(e.getIp().equals(consult)){
              answers.add(e.getName());
              found = true;
              break;
            }
          }
          if(!found)
            answers.add("REPLACE");
        }
      }else if(dns){
        for(String consult : consults){
          found = false;
          for(Entry e : list){
            if(e.getName().equals(consult)){
              answers.add(e.getIp());
              found = true;
              break;
            }
          }
          if(!found)
            answers.add("REPLACE");
        }

        if(deep)
          answers = replaceNotFound(ip, howDeep, answers);
      }else{
        System.err.println("Invalid request (\""+ tokens[1] + "\"");
        System.err.println("Skipping.");
        return;
      }

    }else{
      System.err.println("Invalid request.");
      System.err.println("Unkown DNS action: " + lineTokens[0]);
      System.err.println("Skipping.");
      return;
    }

  }

  private List<String> replaceNotFound(boolean byIp, Integer deep, List<String> answer){
    --deep;
    Friend best = this.server.bestFriend();
    Socket friendConn = null;
    try{
      friendConn = new Socket(best.getIp(), best.getPort());
    }catch(UnknownHostException unE){
      System.err.println("Friend not found.");
      unE.printStackTrace();
      answer.replaceAll(i -> (i.equals("REPLACE")) ? answer.set(answer.indexOf(i), "UNKOWN") : i);
    }catch(IOException ioE){
      System.err.println("Error connection with the friend.");
      ioE.printStackTrace();
      System.exit(-3);
    }

    PrintStream outputToFriend = null;
    BufferedReader inputFromFriend = null;
    try{
      outputToFriend = new PrintStream(friendConn.getOutputStream());
      inputFromFriend = new BufferedReader(new InputStreamReader(friendConn.getInputStream()));

      for(String repAns : answer){
        if(repAns.equals("REPLACE")){
          String message = "DNS\nCONSULT " + (byIp ? "-ip" : "-dns")  + " -d " + deep + "-n 1" + repAns;

          outputToFriend.println(message);
          String line = inputFromFriend.readLine();
          String tokens[] = line.split("\n");
          if(!tokens[0].equals("DNS")){
            System.err.println("Wrong message answer from friend server.");
            System.err.println("Unkown header: " + line);
            System.exit(-2);
          }

          if(tokens[1].equals("ERROR")){
            answer.set(answer.indexOf(repAns), "UNKOWN");
          }else if(tokens[1].equals("OK")){
            if(!tokens[2].equals("1")){
              System.err.println("The friend server should not have more than one answer.");
              System.err.println("Just gonna skip everything after the first one.");
            }
            answer.set(answer.indexOf(repAns), tokens[3]);
          }
        }
      }
    }catch(IOException ioE){
      System.err.println("Could not open/use a channel of communication with friend.");
      ioE.printStackTrace();
      System.exit(-4);
    }
    return answer;
  }

  private void friendRequest(String tokens[]){
    List<Friend> list = this.server.getFriends();
    String lineTokens[] = null;

    if(tokens[1].equals("ADD")){
      lineTokens = tokens[2].split(" ");
      if(!Util.validateIp(lineTokens[0])){
        System.err.println("Invalid message from friend.");
        System.err.println("IP: " + lineTokens[0]);
        System.exit(301);
      }
      list.add(new Friend(lineTokens[0], Integer.parseInt(lineTokens[1])));
    }else if(tokens[1].equalsIgnoreCase("UPDATE")){
      list.forEach(f ->
         {
          try{
            if((f.getIp().equals(tokens[2])) && (f.getLastUpdate().before(DateFormat.getDateTimeInstance().parse(tokens[4]))))
                f.replaceIp(tokens[3]);
          }catch(ParseException pE){
            pE.printStackTrace();
            System.exit(-1);
          }
         }
        );
    }else{
      System.err.println("Invalid request.");
      System.err.println("Unkown action: " + tokens[1]);
      System.err.println("Skipping.");
    }

  }

}
/**
 * tokens[0] = FRIEND
 * tokens[1] = UPDATE
 * tokens[2] = nameservice
 * tokens[3] = newip
 * tokens[4] = date
 */


//CONSULT -dns -d 5 -n 2
//lineTokens[0] = CONSULT
//lineTokens[1] = -dns | -ip
//lineTokens[2] = -d            | -i
//lineTokens[3] = <int>         | -n
//lineTokens[4] = -n            | <int>
//lineTokens[5] = <int>


