package dnswithfriends.serverside.answer;

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import dnswithfriends.protocol.IO;

public class DnsConsult implements Answerable {
  static public final String HEADER = "DNS";
  static private final String FOUND = "OK";
  static private final String ERROR = "ERROR";
  static protected final String listID[] = {FOUND, ERROR, "RETRY"};

  public Boolean haveFailed = null;
  protected List<String> answer = null;
  private Boolean isIp = null;
  private Integer qtd = null;

  BufferedReader in = null;

  /**
   * This constructor *should* probably be called only by the #createAnswer method from the Answerable
   * Creates a DnsConsult and read the message passed
   * NOTE: this class should not be used to return to a txt message!
   */
  public DnsConsult(BufferedReader in){
    this.in = in;

    read();
  }

  /**
   * This constructor should be called everywhere else
   * NOTE: this class should not be used to return to a txt message!
   */
  public DnsConsult(InputStream sender){
    if(sender == null) throw new NullPointerException("Got nothing from sender");

    this.in = new BufferedReader(new InputStreamReader(sender));
    try{
      if(!in.readLine().equalsIgnoreCase(HEADER)){
        System.err.println("A not DNS request cannot be used to create e DNS Consult class.");
        System.exit(11);
      }
    }catch(IOException ioE){
      System.err.println("Could not read from the message.");
      ioE.printStackTrace();
      System.exit(65463);
    }

    read();
  }

  /**
   * EXAMPLE MESSAGE
   *
   *
   * CASE 1: all fine, ip consult || dns consult
   * DNS
   * OK
   * 2
   * 172.45.123.12 || lucasbretana.servebeer.com
   * 192.168.25.1 || menescraft.servemenescraft.net
   *
   * CASE 2: nothing found, aka ERROR
   * DNS
   * ERROR
   *
   * CASE 3: ask this guy
   * DNS
   * RETRY
   * 8.8.8.8
   */
  private void read(){
    String line = null;

    try{
      line = in.readLine();
    }catch(IOException ioE){
      System.err.println("Error reading from the answer.");
      ioE.printStackTrace();
      System.exit(2);
    }

    this.haveFailed = !line.equalsIgnoreCase(FOUND);

    if(this.haveFailed){
      // now there are two possibilities
      if(line.equalsIgnoreCase(ERROR)){
        // ERROR: witch means that there are no consults left
        this.answer = null;
      }else{
        // RETRY: witch means is a iteractive consult, so the anwer gonna be a ASK THIS GUY
        try{
          line = in.readLine();
        }catch(IOException ioE){
          System.err.println("Error reading from the answer. Expected a server's ip");
          ioE.printStackTrace();
          System.exit(6);
        }
        if(!validateIp(line)){
          System.err.println("Problem reading from the answer. Expected a server's ip got :" + line);
          System.exit(7);
        }
        this.answer = new ArrayList<String>();
        this.answer.add(line);
      }
    }else{
      readAnswers();
    }
  }

  private void readAnswers(){
    String line = null;
    int qtd = 0;
    try{
      line = this.in.readLine();
      qtd = Integer.parseInt(line.trim());
    }catch(IOException ioE){
      System.err.println("Could not read from the answer.");
      ioE.printStackTrace();
      System.exit(9);
    }catch(Exception e){
      System.err.println("Badly formed message. Expected: <" + HEADER + "> <" + FOUND + ">" + IO.LINE_SEPARATOR.toString() + "<number>\nFound numer ==\"" + line + "\"");
      e.printStackTrace();
      System.exit(5);
    }

    if(qtd == 0){
      System.err.println("This makes no sense. Explicit!! ZERO messagens from the server!");
      System.exit(666);
    }
    this.answer = new ArrayList<String>();
    for(int i=0 ; i<=qtd ; ++i){
      try{
        this.answer.add(this.in.readLine());
      }catch(IOException ioE){
        System.err.println("Something went wrong with the answer.");
        ioE.printStackTrace();
        System.exit(4);
      }
    }

    this.isIp = validateIp(this.answer.get(0));

  }

  private void readReconsult(){
  }

  public String toString(){
    throw new UnsupportedOperationException("Not implemented!(" + this.haveFailed + " " + this.isIp + " " + ((this.answer != null) ? this.answer.toArray().length : "null") + ")");
  }
}
