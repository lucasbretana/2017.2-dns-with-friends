package dnswithfriends.serverside.logic;

import java.util.Date;

public class Entry {
  private String name = null;
  private String ip = null;
  private Date last = null;

  public Entry(String name, String ip){
    if((name == null) || (ip == null))
      throw new NullPointerException("Cannot have null elements");

    name = name;
    ip = ip;
    last = new Date();
  }

  @Override
  public boolean equals(Entry e){
    return (e != null) && (e.consultIp().equals(this.ip)) && (e.consultName().equals(this.name));
  }

  public String consultIp(){
    return new String(this.ip);
  }

  public String consultName(){
    return new String(this.name);
  }
}
