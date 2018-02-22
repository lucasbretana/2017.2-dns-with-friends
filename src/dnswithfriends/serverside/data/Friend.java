package dnswithfriends.serverside.data;

import java.util.Calendar;
import java.util.Date;

public class Friend{
  private String ip = null;
  private int port;
  private Date lastUpdated = null;

  public Friend(String ip, int port){
    this.ip = ip;
    this.port = port;
    this.lastUpdated = Calendar.getInstance().getTime();
  }

  public String getIp(){
    return this.ip;
  }

  public Friend newest(Friend other){
    if(this.lastUpdated.after(other.getLastUpdate())){
      return this;
    }else{
      return other;
    }
  }

  public Date getLastUpdate(){
    return this.lastUpdated;
  }

  public void replaceIp(String ip){
    this.ip = ip;
    this.lastUpdated = Calendar.getInstance().getTime();
  }

  public int getPort(){
    return this.port;
  }

}
