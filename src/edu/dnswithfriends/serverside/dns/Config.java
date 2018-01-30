package edu.dnswithfriends.serverside.dns;

import java.lang.UnsupportedOperationException;

public class Config<T>{
  
  private String name = null;
  private T value = null;

  public Config(String n, T t) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Not supported yet");
  }

  public void setName(String n) {
    this.name = n;
  }

  public String name(){ return this.name; }

  public void setValue(T v) {
    this.value = v;
  }

  public T value() { return this.value; };


}

