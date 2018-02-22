package dnswithfriends.clientside.client;

import java.net.Socket;

import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.IOException;
import java.util.List;

public class Client {
  private Socket connection = null;
  private PrintStream outputToServer = null;
  private BufferedReader inputFromServer = null;
  private String message = null;

  /**
   * Creates the connection and input/output to/from the server
   * @param port to connect on the server
   */
  public Client(String ip, int port) throws IOException{
    this.connection = new Socket(ip, port);
    this.outputToServer = new PrintStream(this.connection.getOutputStream());
    this.inputFromServer = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
  }

  public void setMessage(File f) throws IOException{
    List<String> lines = java.nio.file.Files.readAllLines(f.toPath());
    String message = "";
    for(String s : lines){
      message += s + "\n";
    }
    this.message = message;
  }

  public void send() throws IOException{
    this.outputToServer.println(this.message);
  }
}
