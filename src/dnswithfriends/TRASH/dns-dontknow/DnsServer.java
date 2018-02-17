package dnswithfriends.serverside.dns;

import dnswithfriends.serverside.dns.config.Config;

import java.util.function.Function;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.lang.UnsupportedOperationException;

public class DnsServer implements Runnable {

  // Sets the default configuration
  static private Config<Integer> n_friends = new Config<Integer>("numer of friends to call", new Integer(-1), s -> Integer.parseInt(s));
  static private Config<String> depth_or_iterative = new Config<String>("search in depth or return friends name", "depth", Function.identity());
  static private Config<String> ip_or_name = new Config<String>("search by IP on name", "IP", Function.identity());
  //static private Config<Long> n_consults = new Config("number of consults per message", new Long(1));
  static private Config<Boolean> single_ans = new Config<Boolean>("answer in single message", true, s -> Boolean.parseBoolean(s));

  private ArrayList<Config> configs = null;

  public DnsServer() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Not supported yet");
    //this.configs.add(new Config<Integer>("depth", DnsServer.n_friends)); // NO IDEIA IF THIS IS THE BEST SOLUTION
    //this.configs.add(new Config<
  }

  public void run() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Not supported yet");
  }

  public static void listConfigs(java.io.PrintStream out) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Not supported yet");
  }
  
  public static void forEachConfig(Consumer<Config> action) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Not supported yet");
  }

}
