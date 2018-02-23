package dnswithfriends.serverside.server;

import java.net.ServerSocket;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import java.io.File;
import java.io.IOException;

import dnswithfriends.serverside.server.ServerThread;

import dnswithfriends.serverside.data.Friend;
import dnswithfriends.serverside.data.Entry;

/**
 * Main server thread, listen to the port and pass the responsability of controling the request to a ServerThread
 */
public class Server extends Thread{
  public static int timeout = 5;

  public static final Friend defaultFriend = new Friend("1.1.1.1", 2018);

  private ServerSocket server = null;
  private List<ServerThread> connectedClient = null;
  private List<Friend> listOfFriends = null;
  private List<Entry> listOfKnowHost = null;

  /**
   * Creates a new server
   */
  public Server(int port) throws IOException{
    this.server =  new ServerSocket(port);
    this.server.setSoTimeout(timeout * 1000);
    this.connectedClient = new ArrayList<ServerThread>();
    setName("Server main thread");
    this.listOfFriends = new ArrayList<Friend>();
    this.listOfKnowHost = new ArrayList<Entry>();
  }

  /**
   * The description of the thread behavior
   */
  @Override
  public void run(){
    ServerThread client = null; 
    boolean done = false;
    while(!done){
      try{
        client = new ServerThread(this, this.server.accept());
        done = true;
      }catch(java.net.SocketTimeoutException tiE){
        //System.err.println("Timeout. Retrying ...");
        done = false;
      }catch(IOException ioE){
        //System.err.println("Could not accept a new connection.");
        ioE.printStackTrace();
        System.exit(10);
      }
    }
    this.connectedClient.add(client);
    client.start();
  }

  /**
   * Used to kill the thread and free its resources
   */
  @Override
  public void interrupt(){
    //System.err.println("Killing: " + getName());
    if(connectedClient == null){
      super.interrupt();
      return;
      }
    for(ServerThread client : connectedClient){
      try{
        client.disconnect("Server closed by admin.");
        client.interrupt();
        //System.err.println("Killing: " + client.getName());
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

  /**
   * Returns the best friend, or a default one
   * The default is used to make it easy to the tests
   */
  public Friend bestFriend(){
    if(listOfFriends.size() == 0)
      return Server.defaultFriend;

    return listOfFriends.get(new Random().nextInt(listOfFriends.size()));
  }

  /**
   * Load the know friends from a file
   * @param f the file
   */
  public void loadFriends(File f){
    try{
      List<String> friends = java.nio.file.Files.readAllLines(f.toPath());
      for(String s : friends){
        String tokens[] = s.split(" ");
        this.listOfFriends.add(new Friend(tokens[0], Integer.parseInt(tokens[1])));
      }
    }catch(IOException ioE){
      System.err.println("Error reading friends from file.");
      System.exit(-6);
    }
  }

  /**
   * Load the know hosts of the server from a file
   * @param f the file
   */
  public void loadEntry(File f){
    try{
      List<String> entry = java.nio.file.Files.readAllLines(f.toPath());
      for(String s : entry){
        String tokens[] = s.split(" ");
        this.listOfKnowHost.add(new Entry(tokens[0], tokens[1]));
      }
    }catch(IOException ioE){
      System.err.println("Error reading host from the file.");
      System.exit(-7);
    }
  }

}
