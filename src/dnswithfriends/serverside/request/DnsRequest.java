package dnswithfriends.serverside.request;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import dnswithfriends.protocol.IO;

public class DnsRequest extends Requestable{
  static protected String HEADER = "DNS";
  static public String TYPE = "CONSULT";

  private Boolean isConsult = null;
  private Boolean consultByIp = null;
  private Integer howDeep = null;
  private List<String> requestList = null;

  public DnsRequest(BufferedReader clientInput){
    this.requestList = new ArrayList<String>();
    this.in = clientInput;

    read();
  }

  public DnsRequest(InputStream str){
    String firstLine = null;

    try{
      this.requestList = new ArrayList<String>();
      in = new BufferedReader(new InputStreamReader(str));
      firstLine = in.readLine();
      if(!firstLine.equalsIgnoreCase(HEADER)){
        System.err.println("Invalid creation of a Dns Request (invalid header \"" + firstLine +"\")");
        System.exit(123);
      }
    }catch(IOException ioE){
      System.err.println("Could not create a request.");
      ioE.printStackTrace();
      System.exit(85);
    }

    read();
  }

  public boolean isConsultByIp(){
    return this.consultByIp;
  }
  
  @Override
  public List<String> getList(){
    return this.requestList;
  }

  /**
   * CASE 1: regulat consult
   *
   * DNS
   * CONSULT -ip -d 5 -n 2
   * lucasbretana.servebeer.com
   * menescraft.serveminecraft.net
   *
   * CASE 2:
   * DNS
   * CONSULT -dns -d 2 -n 1
   * 192.168.25.1
   *
   * CASE 3: update dns
   * DNS
   * UPDATE -ip
   * oldnameservice.com
   * newnameservice.com
   */
  @Override
  public void read(){
    String line = null;
    String tokens[] = null;
    int curIdx = 0;
    try{
      line = this.in.readLine();
      tokens = line.split(IO.INLINE_SEPARATOR.toString());

      this.isConsult = tokens[curIdx].equalsIgnoreCase(DnsRequest.TYPE);
      if(!this.isConsult){
        System.err.println("DNS Request, unknow action: \"" + tokens[curIdx] + "\"");
        System.exit(654);
      }
      ++curIdx;

      this.consultByIp = tokens[curIdx++].equals("-ip");

      if((!this.consultByIp) && (tokens[curIdx].equals("-dns"))){
        System.err.println("Invalid request format. Unknown search type: " + tokens[curIdx]);
        System.exit(20);
      }

      // if it's a consult, then read the type (i/d) and the consults to do
      if(!tokens[curIdx].equals("-i")){
        if(!tokens[curIdx].equals("-d")){
          System.err.println("Invalid request format. Unknow search mode: " + tokens[curIdx]);
          System.exit(90);
        }
        this.howDeep = Integer.parseInt(tokens[++curIdx]);
      }
      ++curIdx;

      if(tokens[curIdx++].equals("-n")){
        try{
          int j = Integer.parseInt(tokens[curIdx]);

          for(int i=0 ; i<=j ; ++i){
            line = this.in.readLine();
            if((!this.consultByIp) && (!validateIp(line))){
              System.err.println("(" + i + "/" + j + ") The consult request a DNS for a non-valid IP (\"" + line + "\")");
              System.exit(545);
            }
            this.requestList.add(line);
          }
        }catch(NumberFormatException numE){
          System.err.println("Could not load the number of request in that message.");
          numE.printStackTrace();
          System.exit(5);
        }
      }


    }catch(IOException ioE){
      System.err.println("Could not read from the message.");
      ioE.printStackTrace();
      System.exit(56);
    }
  }

  @Override
  public String toString(){
    throw new UnsupportedOperationException("Not implemented yet! DEBUG: " + this.isConsult + " " + consultByIp + " " + howDeep);
  }
}
