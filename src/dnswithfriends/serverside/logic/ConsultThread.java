package dnswithfriends.serverside.logic;

import java.net.Socket;
import java.io.InputStream;
import java.io.IOException;

import dnswithfriends.serverside.answer.Answerable;


public class ConsultThread extends Thread {
  private Answerable ans = null;
  
  public ConsultThread(Socket client) throws IOException, Exception{
    this.ans = Answerable.createAnswer(client.getInputStream());
  }

  @Override
  public void run(){
  }
}

