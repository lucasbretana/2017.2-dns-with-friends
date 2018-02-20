package dnswithfriends.protocol;

import java.io.PrintStream;
import java.util.Scanner;

public interface IO {

  public static final PrintStream out = System.out;
  public static final PrintStream err = System.err;
  public static final Scanner keyboard = new Scanner(System.in);

  public static final Character Y = new Character('Y');
  public static final Character Y_ = new Character('y');
  public static final Character N = new Character('N');
  public static final Character N_ = new Character('n');

  public static final Character INLINE_SEPARATOR = ' ';
  public static final Character LINE_SEPARATOR = '\n';
}

