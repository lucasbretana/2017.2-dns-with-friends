package dnswithfriends.serverside.request;

import java.io.InputStream;

import dnswithfriends.protocol.Protocolable;

public interface Requestable extends Protocolable {
  static public Requestable createRequest(InputStream client) {
    return null;
  }

  default public <A> boolean isA(){
    return false;
  }
      
}
