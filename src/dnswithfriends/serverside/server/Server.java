package dnswithfriends.serverside.server;

import java.net.ServerSocket;

import java.util.List;
import java.util.Random;

import java.io.File;
import java.io.IOException;

import dnswithfriends.serverside.server.ServerThread;
import dnswithfriends.serverside.data.Friend;
import dnswithfriends.serverside.data.Entry;

public class Server extends Thread{
  private ServerSocket server = null;
  private List<ServerThread> connectedClient = null;
  private List<Friend> listOfFriends = null;
  private List<Entry> listOfKnowHost = null;

  public Server(int port) throws IOException{
    this.server =  new ServerSocket(port);
  }

  @Override
  public void run(){
    while(true) {
      System.out.println("Server is waiting a connection...");
      ServerThread client = null; 
      try{
        client = new ServerThread(this, this.server.accept());
      }catch(IOException ioE){
        System.err.println("Could not accept a new connection.");
        ioE.printStackTrace();
        System.exit(10);
      }
      this.connectedClient.add(client);
      client.start();
      System.out.println("Connected!");
    }
  }

  @Override
  public void interrupt(){
    if(connectedClient == null){
      super.interrupt();
      return;
    }
    for(ServerThread client : connectedClient){
      try{
        client.disconnect("Server closed by admin.");
        client.interrupt();
      }catch(IOException ioE){
        System.err.println("Could not close the connection with " + client.getName());
        System.err.println("Skipping.");
        ioE.printStackTrace();
      }
    }
    super.interrupt();
  }

  public List<Friend> getFriends(){
    return this.listOfFriends;
  }

  public List<Entry> getHosts(){
    return this.listOfKnowHost;
  }

  public Friend bestFriend(){
    // this server it's just like me, no best friend, just random ones
    return listOfFriends.get(new Random().nextInt(listOfFriends.size()));
  }
}
//  public void setMessage(File f) throws IOException{
//    List<String> lines = java.nio.file.Files.readAllLines(f.toPath());
//    String message = "";
//    for(String s : lines){
//      message += s + "\n";
//    }
//    this.message = message;
//  }
//
//    public void send() throws IOException{
//    this.outputToServer.println(this.message);
//  }

