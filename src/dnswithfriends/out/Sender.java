package dnswithfriends.out;

import dnswithfriends.protocol.Protocolable;

abstract public class Sender implements Protocolable {

  private String msg = null;

  abstract public void write();

  //abstract public void send();

  @Deprecated
  final public void setRawMessage(String m){
    this.msg = m;
  }
}

