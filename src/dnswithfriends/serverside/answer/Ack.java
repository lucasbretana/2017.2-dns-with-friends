package dnswithfriends.serverside.answer;

import dnswithfriends.protocol.IO;

/**
 * Simplemest answer, is just a acknoledge
 */
public class Ack implements Answerable {
  public final String header = "FRIEND";
  public Boolean haveFailed = false;

  public Ack(){
    this.haveFailed = false;
  }

  @Override
  public String toString(){
    return this.header + IO.ENTER + "ACK";
  }
}
