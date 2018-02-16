package dnswithfriends.protocol.reader;

/**
 * Documentation
 */
public class ReadTester {
  public static void main(String...args){
    try{

      DnsReceiver read = new DnsReceiver(new java.io.FileInputStream(new java.io.File(args[0])));
      read.read();

      for(String s : read.consultList){
        System.out.println(s);
      }

      System.out.println("DEBUG: " + read); 

    }catch(Exception e){
      e.printStackTrace();
    }
  }
}

