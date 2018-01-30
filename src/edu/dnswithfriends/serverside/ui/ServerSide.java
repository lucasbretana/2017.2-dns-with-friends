package edu.dnswithfriends.serverside.ui;

import edu.dnswithfriends.serverside.dns.DnsServer;

import java.io.PrintStream;
import java.util.Scanner;
import java.lang.UnsupportedOperationException;

public class ServerSide{

  public static final PrintStream out = System.out;
  public static final PrintStream err = System.err;
  public static final Scanner keyboard = new Scanner(System.in);

  public static final Character Y = new Character('Y');
  public static final Character Y_ = new Character('y');
  public static final Character N = new Character('N');
  public static final Character N_ = new Character('n');

  private DnsServer dns_server = null;

  public ServerSide(){
    this.dns_server = new DnsServer();
    dns_server.run();
  }

  public ServerSide(boolean confirm_first){
    if(!confirm_first) {
      this.out.println("The current DNS server configuration is: ");
      this.dns_server.listConfigs(this.out);
      this.dns_server = new DnsServer();
      this.dns_server.run(); // Start the other thread
      
      readCommands();
    }
  
    Character ans = Y;

    // While there are changes to do, to it
    while((ans.equals(Y)) || (ans.equals(Y_))){
      this.out.println("Okay, lets review it.");
      this.out.println("The current DNS server configuration is: ");
      this.dns_server.listConfigs(this.out);
      this.out.println("Change? (y/n)");
      ans = keyboard.useDelimiter("").next().toCharArray()[0];
      
      if((ans.equals(N)) || (ans.equals(N_))){
        this.dns_server = new DnsServer();
        this.dns_server.run(); // Start the other thread

        readCommands();
      }

      DnsServer.forEachConfig(c -> {
        this.out.println("The \"" + c.name() + "\" is set to \"" + c.value() + "\", change it: (blank to keep it)");
        //TODO: fix this cast :: c.set(keyboard.next());
      });
    }
  }


  private void readCommands(){
    Scanner cmd_reader = new Scanner(System.in);
    String cmd;
    while(cmd_reader.hasNext()){
      cmd = cmd_reader.next();
      switch (cmd) {
        case "/kill": kill() ; break;
        case "/restart": restart(); break;
        case "/override": override(); break;
        default:
          this.err.println("Unkoun command \"" + cmd.toString() + "\". Seak the manual.");
      }

    }
  }
  
  private void kill() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Not supported yet");
  }
  
  private void restart() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Not supported yet");
  }

  private void override() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Not supported yet");
  }

  public static void main(String...args) {
  }
}

