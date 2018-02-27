package dnswithfriends.test;

import java.util.ArrayList;
import java.util.List;

import java.io.File;

import dnswithfriends.serverside.server.Server;
import dnswithfriends.clientside.client.Client;

/**
 * Autometed tests
 * Save a lot of work
 */
public class Tester{
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";

  public static void main(String...args) throws Exception{
    Server server = null;
    Client client = null;
    if(args.length > 0){
      server = new Server(Integer.parseInt(args[0]));
      client = new Client("localhost", Integer.parseInt(args[0]));
    }else{
      System.err.println("Usage: program <port> <host-list> <friend-list>");
      server = new Server(dnswithfriends.serverside.ui.UIServer.DEFAULT_PORT);
      client = new Client("localhost", dnswithfriends.serverside.ui.UIServer.DEFAULT_PORT);
    }

    if(args.length > 2){
      server.loadEntry(new File(args[1]));
      server.loadFriends(new File(args[2]));
    }else{
      server.loadEntry(new File("../resources/data/HOSTS-LIST"));
      server.loadFriends(new File("../resources/data/FRIENDS-LIST"));
    }
    server.start();

    List<String> responses_off = null;
    List<String> responses_on = null;

    responses_off = java.nio.file.Files.readAllLines(new File("../resources/responses/" + "DNS-RESPONSE-OK-DNS-RESPONSES-2").toPath());
    client.setMessage(new File("../resources/requests/DNS-CONSULT-IP-DEEP-2-CONSULTS-2"));
    client.send();
    responses_on = client.rawMessage();
    System.out.println("Test #1 : consult IPS in deep, return OK answer in DNS\n" + ((compareLists(responses_off, responses_on) ? ANSI_GREEN + "PASSED" : ANSI_RED + "NOT PASSES")) + ANSI_RESET);

    responses_off = java.nio.file.Files.readAllLines(new File("../resources/responses/" + "DNS-RESPONSE-RETRY-RESPONSES-1").toPath());
    client.setMessage(new File("../resources/requests/" + "DNS-CONSULT-IP-ITERATIVE-CONSULTS-1"));
    client.send();
    responses_on = client.rawMessage();
    System.out.println("Test #2 : consult IPS in interactive, RETRY response\n" + ((compareLists(responses_off, responses_on) ? ANSI_GREEN + "PASSED" : ANSI_RED + "NOT PASSES")) + ANSI_RESET);

    responses_off = java.nio.file.Files.readAllLines(new File("../resources/responses/" + "DNS-RESPONSE-OK-IPS-RESPONSES-2").toPath());
    client.setMessage(new File("../resources/requests/" + "DNS-CONSULT-DNS-DEEP-2-CONSULTS-2"));
    client.send();
    responses_on = client.rawMessage();
    System.out.println("Test #3 : consult DNS in deep, return OK answer in IPS\n" + ((compareLists(responses_off, responses_on) ? ANSI_GREEN + "PASSED" : ANSI_RED + "NOT PASSES")) + ANSI_RESET);

    responses_off = java.nio.file.Files.readAllLines(new File("../resources/responses/" + "DNS-RESPONSE-RETRY-RESPONSES-1").toPath());
    client.setMessage(new File("../resources/requests/" + "DNS-CONSULT-DNS-ITERATIVE-CONSULTS-1"));
    client.send();
    responses_on = client.rawMessage();
    if(responses_on.size() == 0){
      System.out.println("Fucking raw message: " + client.rawMessage());
    }
    System.out.println("Test #4 : consult DNS in deep, return OK answers in IPS\n" + ((compareLists(responses_off, responses_on) ? ANSI_GREEN + "PASSED" : ANSI_RED + "NOT PASSES")) + ANSI_RESET);
    
    server.interrupt();
    client.close();
  }

  static private <T> boolean compareLists(List<T> list1, List<T> list2){
    if(list1.size() != list2.size()){
      System.out.println("Son of a gun ... " + list1.size() + " != " + list2.size());
      return false;
    }

    boolean res = true;
    for(int i=0 ; i<list1.size() ; ++i){
      T x = list1.get(i);
      T y = list2.get(i);
      
      res &= x.equals(y);
      //TODO: remove
      //if(!x.equals(y))
      //  System.out.println(x.toString() + " != " + y.toString());
      //
      if(!res) return false;
    }
    return res;
  }
}

