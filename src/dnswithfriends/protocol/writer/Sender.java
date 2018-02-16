package dnswithfriends.protocol.writer;

abstract public class Sender implements Protocolable {

  private String msg = null;

  abstract public void write();

  abstract public void send();

  public void setRawMessage(String m){
    this.msg = m;
  }
}

