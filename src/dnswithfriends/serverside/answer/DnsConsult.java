package dnswithfriends.serverside.answer;

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import dnswithfriends.protocol.Protocolable;
import dnswithfriends.protocol.IO;

public class DnsConsult implements Answerable, Protocolable {
  static public final String HEADER = "DNS";
  static private final String FOUND = "OK";
  static private final String ERROR = "ERROR";
  static protected final String listID[] = {FOUND, ERROR, "RETRY"};

  public Boolean haveFailed = null;
  protected List<String> answer = null;
  private Boolean isIp = null;
  private Integer qtd = null;

  BufferedReader in = null;

  public DnsConsult(BufferedReader in){
    this.in = in;
    this.answer = new ArrayList<String>();

    read();
  }

  public DnsConsult(InputStream sender){
    if(sender == null) throw new NullPointerException("Got nothing from sender");

    this.in = new BufferedReader(new InputStreamReader(sender));
    this.answer = new ArrayList<String>();

    read();
  }

  private void read(){
    int curIdx = 0;
    String line = null;
    String tokens[] = null;

    try{
      line = in.readLine();
    }catch(IOException ioE){
      System.err.println("Error reading from the answer.");
      ioE.printStackTrace();
      System.exit(2);
    }

    tokens = line.split(IO.SPACE.toString());

    // tokens[0] == "DNS"
    ++curIdx;

    this.haveFailed = !tokens[curIdx].equalsIgnoreCase(FOUND);
    ++curIdx;

    if(this.haveFailed){
      // TODO
      // now there are two possibilities
      // ERROR: witch means that there are no consults left
      if(tokens[curIdx].equalsIgnoreCase(ERROR)){
      }else{
      // TODO
      // RETRY: witch means is a iteractive consult, so the anwer gonna be a ASK THIS GUY
      }
    }else{
      readAnswers(line, tokens, ++curIdx);
    }
  }

  private void readAnswers(String line, String tokens[], int curIdx){
    try{
      this.qtd = Integer.parseInt(tokens[curIdx]);
    }catch(Exception e){
      System.err.println("Badly formed message. Expected: <" + HEADER + "> <" + FOUND + "> <number>number, found \"" + line + "\"");
      e.printStackTrace();
      System.exit(5);
    }

    for(int i=0 ; i<=this.qtd ; ++i){
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
    return "DnsConsult (Not done yet)";
  }
}
