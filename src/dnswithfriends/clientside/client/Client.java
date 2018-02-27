package dnswithfriends.clientside.client;

import java.net.Socket;

import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class Client {
  private Socket connection = null;
  private PrintStream outputToServer = null;
  private BufferedReader inputFromServer = null;
  private String message = null;
  private List<String> lines = null;
  
  private List<String> answers = null;

  /**
   * Creates the connection and input/output to/from the server
   * @param port to connect on the server
   */
  public Client(String ip, int port) throws IOException{
    this.connection = new Socket(ip, port);
    this.outputToServer = new PrintStream(this.connection.getOutputStream());
    this.inputFromServer = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
    this.lines = new ArrayList<String>();
    this.answers = new ArrayList<String>();
  }

  /**
   * Set the message to send to the server
   * Shall be read from a file
   * @param f the file
   */
  public void setMessage(File f) throws IOException{
    List<String> lines = java.nio.file.Files.readAllLines(f.toPath());
    String message = "";
    for(String s : lines){
      message += s + "\n";
    }
    this.message = message;
  }

  public List<String> rawMessage(){
    return this.lines;
  }

  public void close(){
    try{
    this.connection.close();
    }catch(Exception e){
      ///shiiiii!!!
    }
  }

  private void clear(){
    this.lines = new ArrayList<String>();
    this.answers = new ArrayList<String>();
  }

  /**
   * Send the message to the server
   */
  public void send() throws IOException{
    if((this.message == null) || (this.message.trim().equals(""))){
      return;
    }
    this.outputToServer.println(this.message);
    this.message = null;
    clear();
    do{
      this.read();
    }while(this.lines.size() == 0);
  }

  /**
   * Read the answer from the server
   * Should be called after the send
   */
  public void read() throws IOException{
    String line = null;

    line = this.inputFromServer.readLine();
    if((line == null) || (line.trim().equals(""))){
      lines.clear();
      System.err.println("Unknow DNS header: " + line);
      System.err.println("Skipping.");
      return;
    }
    if(!line.equals("DNS")){
      System.err.println("Unkown header : " + line);
      System.err.println("Skipping.");
      return;
    }
    lines.add(line);

    line = this.inputFromServer.readLine();
    lines.add(line);
    if(line.equals("OK")){
      line = this.inputFromServer.readLine();
      lines.add(line);
      int qtd = Integer.parseInt(line);

      for(int i=0 ; i<qtd ; ++i){
        line = this.inputFromServer.readLine();
        lines.add(line);
        answers.add(line);
      }

      if(answers.size() != qtd)
        answers = answers.subList(0, qtd);
    }else if(line.equals("RETRY")){
      line = this.inputFromServer.readLine();
      answers.add(line);
      lines.add(line);
    }else if(line.equals("ERROR")){
      line = this.inputFromServer.readLine();
      lines.add(line);
      answers = null;
    }else{
      lines.clear();
      System.err.println("Unknow responde status : " + line);
      System.err.println("Skipping.");
    }
  }
}
