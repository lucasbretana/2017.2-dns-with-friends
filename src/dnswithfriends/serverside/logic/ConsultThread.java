package dnswithfriends.serverside.logic;

import java.net.Socket;
import java.io.InputStream;
import java.io.IOException;

import dnswithfriends.serverside.request.Requestable;
import dnswithfriends.serverside.request.DnsRequest;


public class ConsultThread extends Thread {
  private Socket client = null;
  private Requestable req = null;
  
  public ConsultThread(Socket client) {
    this.client = client;
  }

  @Override
  public void run(){
    try{
      this.req = Requestable.createRequest(client.getInputStream());

      boolean b = req.isA();
      System.out.println("DEBUG: " + b);
    }catch(IOException ioE){
      System.err.println("Could not load message.");
      ioE.printStackTrace();
      System.exit(55);
    }catch(Exception e){
      System.err.println("Could not load message.");
      e.printStackTrace();
      System.exit(55);
    }
  }
}

