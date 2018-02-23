package dnswithfriends.serverside.ui;

import java.util.Scanner;

import java.io.File;

import dnswithfriends.serverside.server.Server;

/**
 * Used to interact with the server
 */
@SuppressWarnings("deprecation")
public class UIServer{
  public static final Integer DEFAULT_PORT = 2018;
  public static final String CMD_QUIT = "/quit";
  public static Scanner keyboard = null;

  public static void main(String...args) {
    try{
      keyboard = new Scanner(System.in);
      Server server = null;
      if(args.length > 0)
        server = new Server(Integer.parseInt(args[0]));   
      else
        server = new Server(DEFAULT_PORT);

      if((args.length > 1) && (!args[1].equals("0")))
        Server.timeout = Integer.parseInt(args[1]);
      server.start();

      String command = null;
      do{
        System.out.print("\n> ");
        command = keyboard.nextLine();
        if(command.equalsIgnoreCase("/load-friends")){
          System.out.println("\nfile name: ");
          server.loadFriends(new File(keyboard.nextLine()));
        }else if(command.equalsIgnoreCase("/load-hosts")){
          System.out.println("\nfile name: ");
          server.loadEntry(new File(keyboard.nextLine()));
        }else if(command.equalsIgnoreCase("/help")){
          printHelp();
        }else if(command.equalsIgnoreCase(CMD_QUIT)){
          System.out.println("Good bye");
        }else{
          System.out.println("Unkown command: " + command);
          printHelp();
        }
      }while(!command.equalsIgnoreCase(CMD_QUIT));
      server.interrupt();
      server.stop();
    }catch(Exception e){
      System.err.println("Fatal error.");
      e.printStackTrace();
      System.exit(666);
    }

  }

  static private void printHelp(){
    System.out.println("List of know commands.");
    System.out.println("Syntax:\nCOMMAND : action description");
    System.out.println("/load-friends : used to load server friends from a file, expect a <file-name>");
    System.out.println("/load-hosts : used to load server know hosts from a file, expect a <file-name>");
    System.out.println("/help :  show this help page");
    System.out.println("/quit : used to close the server");
  }

}
