package dnswithfriends.serverside.request;

public class DnsRequest implements Requestable{
  @Override
  public <DnsRequest> boolean isA(){
    return true;
  }

}
