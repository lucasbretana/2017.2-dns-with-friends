package dnswithfriends.serverside.logic;

import java.io.*;
import java.net.*;

public class ClientSimulator{
  static public void main(String...args) throws Exception{
    PrintStream out = new PrintStream(new Socket("localhost", 2018).getOutputStream());
    out.println(new BufferedReader(new FileReader(new File(args[0]))).lines().reduce((a, b) -> a + "\n" + b).get());
    out.close();
  }
}

