package dnswithfriends.out;

import java.lang.UnsupportedOperationException;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import dnswithfriends.protocol.IO;

public class DnsRequest extends Sender {

  public static final String CONSULT = "CONSULT";
  public static final String UPDATE = "UPDATE";
  public static final String IP_CONSULT = "-ip";
  public static final String DNS_CONSULT = "-dns";
  public static final String DEEPTH = "-d";
  public static final String ITERACTIVE = "-i";
  public static final String NUM_FRIEND = "-n";

  private Boolean consult = null;
  private Boolean ip_consult = null;
  private Integer deepth = null;
  private Integer n_consult = null;

  private List<String> consultList = null;

  private BufferedWriter out = null;

  public DnsRequest(OutputStream server) throws NullPointerException{
    if(server == null) throw new NullPointerException();

    this.out = new BufferedWriter(new OutputStreamWriter(server));
  }

  public void write() {
    try {
      out.write("DNS ");
      if(this.consult) out.write(DnsRequest.CONSULT + " ");
      else throw new UnsupportedOperationException("Cannot do DNS updates yet");

      if(this.ip_consult) out.write(DnsRequest.IP_CONSULT + " ");
      else out.write(DnsRequest.DNS_CONSULT + " ");

      if(this.deepth != null) out.write(DnsRequest.DEEPTH + " " + this.deepth.toString());
      else out.write(DnsRequest.ITERACTIVE + " ");

      if(this.n_consult != null) this.out.write(DnsRequest.NUM_FRIEND + " " + this.n_consult);

      this.out.write(IO.ENTER);
      for(String c : this.consultList){
        this.out.write(c);
      }

    } catch (IOException ioE) {
      System.err.println("Error writing the message");
      ioE.printStackTrace();
      System.exit(1);
    }
  }

  public List<String> requestList(){
    return this.consultList;
  }

}

