package dnswithfriends.serverside.data;

/**
 * Describes one entry on the table of know hosts
 */
public class Entry{
  private String ip = null;
  private String name = null;

  public Entry(String ip, String name){
    this.ip = ip;
    this.name = name;
  }

  public String getIp(){
    return this.ip;
  }

  public String getName(){
    return this.name;
  }

  public void updateIp(String dns){
    this.name = dns;
  }

  public void updateName(String ip){
    this.name = ip;
  }
}
