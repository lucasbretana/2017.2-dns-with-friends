package dnswithfriends.serverside.dns.config;

import java.util.function.Function;

import java.lang.UnsupportedOperationException;

public class Config<T> {
  private String name               = null;
  private T value                   = null;
  private Function<String, T> cast  = null;

  public Config(String n, T t, Function<String, T> convertToConfig) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Not supported yet");
  }

  /**
   * @param n the new name of the configuration
   */
  public void setName(String n) {
    this.name = n;
  }

  /**
   * @return the name of the configuration
   */
  public String name(){ return this.name; }

  /**
   * @param v sets the value of the configuration
   */
  @Deprecated
  public void set(T v) {
    this.value = v;
  }

  /**
   * @param s it's the configuration raw format type
   */
  public void set(String s) {
    this.value = this.cast.apply(s);
  }

  /**
   * @return the value of this configuration
   */
  public T value() { return this.value; };
}

