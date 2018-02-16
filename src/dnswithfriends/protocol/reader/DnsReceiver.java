package dnswithfriends.protocol.reader;

import dnswithfriends.protocol.IO;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

/**
 * This class is responsable for doing all the Dns related requests
 * Such as:  consult and update
 */
public class DnsReceiver extends Receiver {

  public static final String CONSULT = "CONSULT";
  public static final String UPDATE = "UPDATE";
  public static final String IP_CONSULT = "-ip";
  public static final String DEEPTH = "-d";
  public static final String ITERACTIVE = "-i";
  public static final String NUM_FRIEND = "-n";

  private Boolean consult = null;
  private Boolean ip_consult = null;
  private Integer deepth = null;
  private Integer n_consult = null;

  List<String> consultList = null;

  private BufferedReader in = null;


  public DnsReceiver(InputStream sender){
    if(sender == null) throw new NullPointerException("Invalid stream");

    this.in = new BufferedReader(new InputStreamReader(sender));
  }

  /**
   * Shall read the message
   */
  @Override
  public void read() {
    int i = 0;
    String line = null;

    try {
      line = this.in.readLine();
    }catch (IOException ioE){
      System.out.println("Error reading from the message!");
      ioE.printStackTrace();
    }
    // the first part can be ignored, it is a DNS
    String tokens[] = line.split(IO.SPACE.toString());
    System.out.println("DEBUG: in DNS class, found \"" + tokens[i] + "\" wich is " + ( tokens[i].equals("DNS") ? " OK " : " NOT OK "  ));
    if(!tokens[0].equals("DNS")) {
      System.err.println("DNS RECEIVER: Error!\nUnknow request. Identification is: " + tokens[0].toString() + "\tUnkown! Killing it.");
      System.exit(10);
    }
    ++i;
    this.consult = tokens[i].equals(DnsReceiver.CONSULT);
    ++i;
    this.ip_consult = tokens[i].equalsIgnoreCase(DnsReceiver.IP_CONSULT);
    ++i;

    if(tokens[i].equalsIgnoreCase(DnsReceiver.DEEPTH)) {
      ++i;
      this.deepth = Integer.parseInt(tokens[i]);
      ++i;
    }else if(tokens[i].equalsIgnoreCase(DnsReceiver.ITERACTIVE)) {
      ++i;
      this.deepth = null;
    }

    if(tokens[i].equalsIgnoreCase(DnsReceiver.NUM_FRIEND)) {
      ++i;
      this.n_consult = Integer.parseInt(tokens[i]);
    }else{
      this.n_consult = null;
    }
    
    this.consultList = new ArrayList<String>();
    for(i=1 ; i<=this.n_consult ; ++i){
      try {
        this.consultList.add(this.in.readLine());
      }catch (IOException ioE){
        System.out.println("Error reading from the message!");
        ioE.printStackTrace();
        System.exit(1);
      }
    }
    
  }


  @Override
  public String toString(){
    return "consult: " + this.consult + "\tconsult by: " + ( this.ip_consult ? "ip" : "dns") + "\tsearch in: " + ( this.deepth != null ? "deepth (" + this.deepth.toString() + ")" : "iterative" ) + "\tand " + this.n_consult + " consults";
  }


}
