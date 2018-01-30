package edu.dnswithfriends.serverside.dns;

import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.lang.UnsupportedOperationException;

public class DnsServer implements Runnable {

  // Sets the default configuration
  static private int n_friends = -1;
//  static private int depth_or_iterative = DEPTH; -- maybe using Enum
//  static private int ip_or_name = IP; -- maybe using Enum
  static private long n_consult = 1;
  static private boolean single_ans = true;

  private ArrayList<Config> configs = null;

  public DnsServer() throws UnsupportedOperationException{
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
