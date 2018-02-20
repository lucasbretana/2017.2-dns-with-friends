package dnswithfriends.serverside.logic;

import java.util.List;
import java.util.ArrayList;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

import dnswithfriends.serverside.logic.ConsultThread;
import dnswithfriends.serverside.logic.Friend;

public class CPU extends Thread {
  
  private ServerSocket listener = null;
  private List<ConsultThread> connectedClient = null;

  protected List<Friend> friendList = null;
  protected List<Entry> database = null;

  public CPU(int port) throws IOException {
    listener = new ServerSocket(port);
    setName("Listener");
    connectedClient = new ArrayList<ConsultThread>();
    friendList = new ArrayList<Friend>();
    database = new ArrayList<Entry>();
  }

  @Override
  public void run(){
    while(true){
      ConsultThread c = null;
      try{
        c = new ConsultThread(this.listener.accept());
        this.connectedClient.add(c);
        c.start();
      }catch(IOException ioE){
        System.err.println("Could not accept a new client.");
        ioE.printStackTrace();
        System.exit(78);
      }catch(Exception e){
        System.err.println("Could not accept a new client.");
        e.printStackTrace();
        System.exit(99);
      }

    }
  }
}
