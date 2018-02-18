package dnswithfriends.serverside.answer;

import java.io.FileInputStream;

import dnswithfriends.serverside.answer.Answerable;

public class AnswerTester {
  public static void main(String...args) throws Exception{
    Answerable ans = Answerable.createAnswer(new FileInputStream(args[0]));
    ans.toString();
  }
}
