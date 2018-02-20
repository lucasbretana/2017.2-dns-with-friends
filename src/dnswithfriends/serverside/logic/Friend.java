package dnswithfriends.serverside.logic;

import java.util.Date;

class Friend {
  private String ip = null;
  private Date lstChat = null;
  private Boolean checked = null;

  public Friend(String ip){
    this.ip = ip;

    this.lstChat = new Date();
    this.checked = false;
  }

  public void setIp(String i){
    this.ip = i;
    this.lstChat = new Date();
  }
  public String getIp(){
    return this.ip;
  }

  public Date getLastChat(){
    return this.lstChat;
  }

  public boolean isChecked() {
    return this.checked;
  }
}
