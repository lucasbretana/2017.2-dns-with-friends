package dnswithfriends.util;

import java.util.function.Function;

public class Util<T> {
    static public <T> boolean anyMatch(T array[], Function<T, Boolean> apply){
    for(T o : array) { 
      if(apply.apply(o)) return true;
    }
    return false;
  }

  static public <T> boolean allMatch(T array[], Function<T, Boolean> apply){
    for(T o : array){
      boolean res = true;
      res &= apply.apply(o);
      if(!res) return false;
    }
    return true;
  }

}
