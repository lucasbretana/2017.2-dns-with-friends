package dnswithfriends.serverside.answer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import dnswithfriends.util.Util;
import dnswithfriends.protocol.IO;
import dnswithfriends.protocol.Protocolable;
import dnswithfriends.serverside.answer.DnsConsult;

public interface Answerable extends Protocolable{
  public String header = null;


  /**
   * This method should be called when it's make a re-consult
   * The answer will be sent here to be creted the aproprieted class
   * @param askedServer is the input of the server
   */
  static public Answerable createAnswer(InputStream askedServer) throws Exception{

    if(askedServer == null) throw new NullPointerException("Server didn't sent anything. Got a null!");
    BufferedReader in = new BufferedReader(new InputStreamReader(askedServer));

    String firstLine = null;
    try {
      firstLine = in.readLine();
    }catch(IOException ioE){
      System.err.println("Could not read the line from the server.");
      ioE.printStackTrace();
      System.exit(1);
    }

    int curIdx = 0;
    String tokens[] = firstLine.split(IO.SPACE.toString());
    if(tokens[curIdx++].equalsIgnoreCase(DnsConsult.HEADER)) {
      return new DnsConsult(in);
    }
    throw new Exception("Unknow kind of request (\"" + tokens[curIdx].toString()  + "\")");
  }
  
  

  //@Override
  //default public String toString(){
  //  return "This a default message" + IO.ENTER + "\"haveFailed\" == \"" + this.haveFailed + "\"";
  //}
}
