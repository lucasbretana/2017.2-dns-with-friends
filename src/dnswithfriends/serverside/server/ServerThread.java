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

/**
 * The resposable class to take care of a new request
 */
public class ServerThread extends Thread{
  static private int count = 0;
  private Socket connection = null;
  private PrintStream outputToClient = null;
  private BufferedReader inputFromClient = null;
  private String message = null;
  private Server server = null;

  /**
   * Creates the ServerThread with its information about the request
   * @param server a refence to the main server, where is the data
   * @param conn the connection with the client
   */
  public ServerThread(Server server, Socket conn) throws IOException{
    this.server = server;
    this.connection = conn;
    this.outputToClient = new PrintStream(this.connection.getOutputStream());
    this.inputFromClient = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
    setName("ServerThread, aka, client #" + ServerThread.count++);
  }

  /**
   * Sets the message to send to the client from a file
   * @param f the file
   */
  @Deprecated
  public void setMessage(File f) throws IOException{
    List<String> lines = java.nio.file.Files.readAllLines(f.toPath());
    String message = "";
    for(String s : lines){
      message += s;
    }
    this.message = message;
  }

  /**
   * Sends the message to the client
   */
  public void send() throws IOException{
    this.outputToClient.println(this.message);
  }

  /**
   * The behavior of the thread is described here
   * It will try to read the many differentes formats of request
   */
  @Override
  public void run(){
    while(true){
      String request = null;
      try{
        request = this.inputFromClient.readLine();
        if(request.equalsIgnoreCase("DNS")){
          dnsRequest();
        }else if(request.equalsIgnoreCase("FRIEND")){
          friendRequest();
        }else{
          System.err.println("Unkown request format:\n" + request);
          System.err.println("Skipping.");
        }
      }catch(IOException ioE){
        System.err.println("Could not read from the new client.");
        ioE.printStackTrace();
        System.exit(20);
      }
    }
  }

  /**
   * Used to disconnection from the client
   */
  @Deprecated
  public void disconnect(String reason) throws IOException{
    this.message = "CLOSED\n" + reason;
    send();
  }

  /**
   * Read the rest of the message as a dnsRequest
   */
  private void dnsRequest() {
    try{
      List<Entry> listHosts = this.server.getHosts();
      String line = this.inputFromClient.readLine();
      String tokens[] = line.split(" ");

      if(tokens[0].equals("CONSULT")){
        boolean ip = tokens[1].equals("-ip");
        boolean dns = !ip;

        boolean deep = tokens[2].equals("-d");
        Integer howDeep = null;
        boolean iter = tokens[2].equals("-i");

        Integer numCon = 1;
        List<String> answers = new ArrayList<String>();
        //System.out.println(""+ tokens[0] + " " + tokens[1] + " " + tokens[2] + " " + tokens[3] + " " + tokens[4] + " " + tokens[5]);
        if(deep){
          howDeep = Integer.parseInt(tokens[3]);

          if(!tokens[4].equals("-n")){
            System.err.println("Invalid request.");
            System.err.println("Skipping.");
            return;
          }

          numCon = Integer.parseInt(tokens[5]);
        }else if(iter){
          if(!tokens[3].equals("-n")){
            System.err.println("Invalid request.");
            System.err.println("Skipping.");
            return;
          }

          numCon = Integer.parseInt(tokens[4]);
        }else{
          System.err.println("Invalid request.");
          System.err.println("Unkown mode: " + tokens[2]);
          System.err.println("Skipping.");
          return;
        }

        //System.out.println("ip : " + ip + " - dns : " + dns + " - deep : " + deep + " - howDeep : " + howDeep + " - num : " + numCon);
        List<String> consults = new ArrayList<String>();
        for(int i=0 ; i<numCon; ++i){
          line = this.inputFromClient.readLine();
          consults.add(line);
        }

        if(ip){
          boolean found = false;
          for(String consult : consults){
            found = false;
            if(listHosts == null){
              answers.add( (deep) ? "REPLACE" : "UNKOWN");
            }else{
              for(Entry e : listHosts){
                if(e.getIp().equals(consult)){
                  answers.add(e.getName());
                  found = true;
                  break;
                }
              }
              if(!found)
                answers.add("REPLACE");
            }
          }
        }else if(dns){
          boolean found = false;
          for(String consult : consults){
            found = false;
            if(listHosts == null){
              answers.add((deep) ? "REPLACE" : "UNKNOW");
            }else{
              for(Entry e : listHosts){
                if(e.getName().equals(consult)){
                  answers.add(e.getIp());
                  found = true;
                  break;
                }
              }
              if(!found){
                answers.add("REPLACE");
              }
            }
          }
        }else{
          System.err.println("Invalid request");
          System.err.println("Skipping.");
          return;
        }

        if((deep) && (howDeep > 1)){
          answers = replaceNotFound(ip, howDeep, answers);
        }else if((howDeep != null) && (howDeep <= 1)){
          answers.replaceAll(i -> (i.equals("REPLACE")) ? "UNKOWN" : i);
        }else if(iter){
          if(numCon == 1){
            answers.clear();
            answers.add((this.server.bestFriend() != null) ? this.server.bestFriend().getIp() : "UNKOWN");
          }else{
            for(String s : answers){
              if(s.equals("REPLACE"))
                answers.set(answers.indexOf(s), "UNKOWN");
            }
          }
        }else{
          System.err.println("FATAL");
          System.exit(-8);
        }

        //Have the answet, mount the message and send it
        String message = "DNS\n";
        if((iter) && (answers.size() == 1))
          message += "RETRY\n";
        else{
          message += "OK\n";
          message += numCon + "\n";
        }
        for(String s : answers)
          message += s + "\n";

        this.outputToClient.println(message);

      }else{
        System.err.println("Invalid request.");
        System.err.println("Unkown DNS action.");
        System.err.println("Skipping.");
        return;
      }
    }catch(IOException ioE){
      System.err.println("Error reading the request.");
      System.exit(30);
    }

  }

  /**
   * Used to replace any not found infomation with the best solution for that consult
   * @param byIp is the consult by IP
   * @param deep how many friends can be asked
   * @param answer the current information, may contain stuff to be replaced
   */
  private List<String> replaceNotFound(boolean byIp, Integer deep, List<String> answer){
    --deep;
    Friend best = this.server.bestFriend();
    if(best.equals(Server.defaultFriend)){
      answer.replaceAll(i -> (i.equals("REPLACE")) ? "UNKOWN" : i);
      return answer;
    }
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
          if(!line.equals("DNS")){
            System.err.println("Wrong message answer from friend server.");
            System.err.println("Unkown header: " + line);
            System.exit(-2);
          }

          line = inputFromClient.readLine();
          if(line.equals("ERROR")){
            answer.set(answer.indexOf(repAns), "UNKOWN");
          }else if(line.equals("OK")){
            line = inputFromClient.readLine();
            if(!line.equals("1")){
              System.err.println("The friend server should not have more than one answer.");
              System.err.println("Just gonna skip everything after the first one.");
            }
            line = inputFromClient.readLine();
            answer.set(answer.indexOf(repAns), line);
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

  /**
   * Read the rest of the request as a friendRequest
   */
  private void friendRequest() throws IOException{
    List<Friend> list = this.server.getFriends();
    String line = this.inputFromClient.readLine();
    String lineTokens[] = null;

    if(line.equals("ADD")){
      lineTokens = inputFromClient.readLine().split(" ");
      if(!Util.validateIp(lineTokens[0])){
        System.err.println("Invalid message from friend.");
        System.err.println("IP: " + lineTokens[0]);
        System.exit(301);
      }
      list.add(new Friend(lineTokens[0], Integer.parseInt(lineTokens[1])));
    }else if(line.equalsIgnoreCase("UPDATE")){
      String line2 = this.inputFromClient.readLine();
      String line3 = this.inputFromClient.readLine();
      String line4 = this.inputFromClient.readLine();
      list.forEach(f ->
          {
            try{
              if((f.getIp().equals(line2)) && (f.getLastUpdate().before(DateFormat.getDateTimeInstance().parse(line4))))
                f.replaceIp(line3);
            }catch(ParseException pE){
              pE.printStackTrace();
              System.exit(-1);
            }
          }
          );
    }else{
      System.err.println("Invalid request.");
      System.err.println("Unkown action: " + line);
      System.err.println("Skipping.");
    }

  }

  /**
   * Used to kill the thread and free its resources
   */
  @Override
  public void interrupt(){
    try{
      this.inputFromClient.close();
      this.outputToClient.close();
      this.connection.close();
      super.interrupt();
    }catch(IOException ioE){
      System.err.println("Error closing the connection in " + this.getName());
      System.err.println("Skipping.");
      ioE.printStackTrace();
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


