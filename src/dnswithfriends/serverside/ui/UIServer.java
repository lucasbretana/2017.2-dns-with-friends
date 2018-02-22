package dnswithfriends.serverside.ui;

import java.util.Scanner;

import dnswithfriends.serverside.server.Server;

public class UIServer{
  private static final Integer DEFAULT_PORT = 2018;
  private static final String CMD_QUIT = "/quit";
  private static Scanner keyboard = null;

  public static void main(String...args) {
    try{
      keyboard = new Scanner(System.in);
      Server server = null;
      if(args.length > 0)
        server = new Server(Integer.parseInt(args[0]));   
      else
        server = new Server(DEFAULT_PORT);

      server.start();

      String command = null;
      do{
        System.out.println("Also, I'm waiting for commands");
        command = keyboard.nextLine();
        System.out.println("Command: \"" + command + "\"");
      }while(!command.equalsIgnoreCase(CMD_QUIT));
      System.out.println("Closing.");
      server.interrupt();
      System.out.println("Closed.");
    }catch(Exception e){
      System.err.println("Fatal error.");
      e.printStackTrace();
      System.exit(666);
    }

  }
}
