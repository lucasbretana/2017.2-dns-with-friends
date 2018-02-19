package dnswithfriends.serverside.request;

import java.io.FileInputStream;


import dnswithfriends.serverside.request.Requestable;

public class RequestTester{
  static public void main(String...args) throws Exception{
    Requestable req = Requestable.createRequest(new FileInputStream(args[0]));
    req.toString();
    
  }
}
