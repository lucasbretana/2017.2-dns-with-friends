package dnswithfriends.clientside.ui;

import java.io.File;

import java.util.Scanner;

import dnswithfriends.clientside.client.Client;

/**
 * Used to interact with the server
   */
public class UIClient{

  private static final String DEFAULT_IP = "localhost";
  private static final int DEFAULT_PORT = 2018;
  private static Scanner keyboard = null;
  public static void main(String...args){
    try{
      Client client = null;
      keyboard = new Scanner(System.in);
      if(args.length > 1)
        client = new Client(args[0], Integer.parseInt(args[1])); 
      else
        client = new Client(DEFAULT_IP, DEFAULT_PORT);

      while(true){
        String file_path = null;
        file_path = keyboard.nextLine();
        if(file_path.equals("/quit"))
          break;

        client.setMessage(new File(file_path));
        client.send();
        System.out.println("Got: " + client.rawMessage());
        //client.read();
      }
    }catch(Exception e){
      System.err.println("Error on ui");
      e.printStackTrace();
      System.exit(313);
    }
  }
}
