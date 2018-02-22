package dnswithfriends.clientside.client;

import java.io.File;
import java.io.IOException;

import dnswithfriends.clientside.client.Client;

public class ClientTester{
  public static void main(String...args) throws IOException{
    Client c = new Client("localhost", 2018);

    c.setMessage(new File(args[0]));
    c.send();
  }
}
