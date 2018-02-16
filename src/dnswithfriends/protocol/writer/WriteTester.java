package dnswithfriends.protocol.writer;

/**
 * Documentation
 */
public class WriteTester {
  public static void main(String...args){
    try{

      DnsRequest write = new DnsRequest(new java.io.FileOutputStream(new java.io.File(args[0])));
      write.write();
    }catch(Exception e){
      e.printStackTrace();
    }
  }
}

