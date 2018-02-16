package dnswithfriends.protocol.reader;

import dnswithfriends.protocol.Protocolable;

abstract public class Receiver implements Protocolable {

  private String msg = null;

  abstract public void read();

  //abstract public void receive();

  public String getRawMessage(){
    return this.msg;
  }

}

