package dnswithfriends.serverside.request;

import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import dnswithfriends.protocol.Protocolable;

public abstract class Requestable implements Protocolable {

  public BufferedReader in = null;
  public List<String> consultList = null;


  static protected String HEADER = null;

  static public Requestable createRequest(InputStream client) throws Exception{
    if(client == null) throw new NullPointerException("Client didn't sent anything. Got a null for input stream.");
    BufferedReader in = new BufferedReader(new InputStreamReader(client));

    String firstLine = null;
    try {
      firstLine = in.readLine();
    }catch(IOException ioE){
      System.err.println("Could not read the line from the server.");
      ioE.printStackTrace();
      System.exit(1);
    }

    if(firstLine.equalsIgnoreCase(DnsRequest.HEADER)) {
      return new DnsRequest(in);
    }else if(firstLine.equalsIgnoreCase(FriendRequest.HEADER)){
      return new FriendRequest(in);
    }
    throw new Exception("Unknow kind of request (\"" + firstLine.toString()  + "\")");
  }

  abstract public void read();

  public List<String> getList(){
    return this.consultList;
  }

}
