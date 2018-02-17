package dnswithfriends.serverside.answer;

/**
 * Simplemest answer, is just a acknoledge
 */
public class Ack implements Answerable {
  public final String header = "FRIEND";

  public Ack(){
    this.haveFailed = false;
  }

  @Override
  public String toString(){
    return this.header + IO.ENTER + "ACK";
  }
}
