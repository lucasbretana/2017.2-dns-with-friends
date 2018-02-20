package dnswithfriends.serverside.request;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.text.ParseException;

public class FriendRequest extends Requestable{
  static final public String HEADER = "FRIEND";

  private BufferedReader in = null;
  private List<String> requestList = null;
  private Boolean isAdd = null;
  private Date lastUpdated = null;

  public FriendRequest(InputStream str){
    this.in = new BufferedReader(new InputStreamReader(str));
    this.requestList = new ArrayList<String>();

    String firstLine = null;
    try{
      this.in.readLine();

      if(!firstLine.equalsIgnoreCase(HEADER)){
        System.err.println("Cannot create a FRIEND request (invalid header: \"" + firstLine + "\")");
        System.exit(987);
      }
    }catch(IOException ioE){
      System.err.println("Error reading from the request.");
      ioE.printStackTrace();
      System.exit(58);
    }

    read();
  }

  public FriendRequest(BufferedReader in){
    this.requestList = new ArrayList<String>();

    read();
  }

  @Override
  public List<String> getList(){
    return this.requestList;
  }

  @Override
  public void read(){
    String line = null;

    try{
      line = this.in.readLine();

      this.isAdd = false;
      if(line.equalsIgnoreCase("ADD")){
        line = this.in.readLine();
        this.requestList.add(line);
        this.isAdd = true;
      }else if(line.equalsIgnoreCase("UPDATE")){
        line = this.in.readLine();
        this.requestList.add(line);

        line = this.in.readLine();
        this.requestList.add(line);

        line = this.in.readLine();
        try{
          this.lastUpdated = new SimpleDateFormat().parse(line);
        }catch(ParseException pE){
          System.err.println("Invalid date on updte request.");
          pE.printStackTrace();
          System.exit(234);
        }

      }else if(line.equalsIgnoreCase("RM")){
        line = this.in.readLine();
        this.requestList.add(line);
      }else{
        System.err.println("Friend Request, invalid action: " + line);
        System.exit(911);
      }
    }catch(IOException ioE){
      System.err.println("Error reading from the request.");
      ioE.printStackTrace();
      System.exit(100);
    }
  }

  @Override
  public String toString(){
    throw new UnsupportedOperationException("Not implemeneted yet (DEUG: " + this.isAdd);
  }
}
