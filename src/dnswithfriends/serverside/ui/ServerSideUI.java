package dnswithfriends.serverside.ui;

import dnswithfriends.protocol.IO;
import dnswithfriends.serverside.dns.DnsServer;
import dnswithfriends.serverside.dns.config.Config;

import java.util.Scanner;

import java.lang.UnsupportedOperationException;

public class ServerSideUI {

  private DnsServer dns_server = null;

  public ServerSideUI (){
    this.dns_server = new DnsServer();
    dns_server.run();
  }

  public ServerSideUI (boolean confirm_first){
    if(!confirm_first) {
      IO.out.println("The current DNS server configuration is: ");
      DnsServer.listConfigs(IO.out);
      this.dns_server = new DnsServer();
      this.dns_server.run(); // Start the other thread
      
      readCommands();
    }
  
    Character ans = IO.Y;

    // While there are changes to do, to it
    while((ans.equals(IO.Y)) || (ans.equals(IO.Y_))){
      IO.out.println("Okay, lets review it.");
      IO.out.println("The current DNS server configuration is: ");
      DnsServer.listConfigs(IO.out);

      IO.out.println("Change? (y/n)");
      ans = IO.keyboard.useDelimiter("").next().toCharArray()[0];
      
      if((ans.equals(IO.N)) || (ans.equals(IO.N_))){
        this.dns_server = new DnsServer();
        this.dns_server.run(); // Start the other thread

        readCommands();
      }

      DnsServer.forEachConfig((Config c) -> {
        IO.out.println("The \"" + c.name() + "\" is set to \"" + c.value() + "\", change it: (blank to keep it)");
        c.set(IO.keyboard.next());
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
          IO.println("Unkoun command \"" + cmd.toString() + "\". Seak the manual.");
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

