package dnswithfriends.util;

import java.util.regex.Pattern;

public class Util{
  public static final String ipv4_format = "\\b((25[0–5]|2[0–4]\\d|[01]?\\d\\d?)(\\.)){3}(25[0–5]|2[0–4]\\d|[01]?\\d\\d?)\\b";
  public static final int port_top = 65535;

  /**
   *    * Just check if the IP is in the right format (0-255].[0-255].[0-255].[0-255]
   *       * @param ip to check
   *          */
  static public boolean validateIp(String ip){
    return Pattern.matches(ipv4_format, ip);
  }

  /**
   *    * Just check if the port it the right range (0,65535]
   *       * @param port to check
   *          */
  static public boolean validatePort(int port){
    return (port <= port_top) && (port > 0);
  }
}
