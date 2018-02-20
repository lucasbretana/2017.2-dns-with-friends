package dnswithfriends.serverside.logic;

import java.net.Socket;
import java.io.InputStream;
import java.io.IOException;

import dnswithfriends.serverside.request.Requestable;
import dnswithfriends.serverside.request.DnsRequest;


public class ConsultThread extends Thread {
  private Socket client = null;
  private Requestable req = null;
  private CPU father = null;
  
  public ConsultThread(CPU c, Socket client) {
    this.father = c;
    this.client = client;
  }

  @Override
  public void run(){
    try{
      this.req = Requestable.createRequest(client.getInputStream());

      //TODO: continue from here
      // now its time to process the request according to its type
      if(this.req instanceof DnsRequest){
        DnsRequest dR = (DnsRequest)this.req;
        //DNS REQUEST PROCESS
        if(dR.isConsultByIp()){
          for(String s : dR.getList()) {
            Entry aux = new Entry(
           for(Entry e : father.getList()){
             if(
           }
          }
        }

      }else if(this.req instanceof FriendRequest){
        FriendRequest fR = (FriendRequest)this.req;
        // FRIEND REQUEST PROCESS
      }else{
        System.err.println("Unknow request. Skipping.");
      }
    }catch(IOException ioE){
      System.err.println("Could not load message.");
      ioE.printStackTrace();
      System.exit(55);
    }catch(Exception e){
      System.err.println("Could not load message.");
      e.printStackTrace();
      System.exit(55);
    }
    System.out.println(this.req.toString());
  }
}

