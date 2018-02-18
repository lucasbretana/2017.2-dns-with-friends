package dnswithfriends.serverside.logic;

import java.util.List;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

import dnswithfriends.serverside.logic.ConsultThread;

public class CPU extends Thread {
  
  ServerSocket listener = null;
  List<ConsultThread> connectedClient = null;

  public CPU(int port) throws IOException {
    this.listener = new ServerSocket(port);
    this.connectedClient = null;
  }

  @Override
  public void run(){
    while(true){
      ConsultThread c = null;
      try{
        c = new ConsultThread(this.listener.accept());
      }catch(IOException ioE){
        System.err.println("Could not accept a new client.");
        ioE.printStackTrace();
        System.exit(78);
      }catch(Exception e){
        System.err.println("Could not accept a new client.");
        e.printStackTrace();
        System.exit(99);
      }

      this.connectedClient.add(c);
      c.run();
    }
  }
}
