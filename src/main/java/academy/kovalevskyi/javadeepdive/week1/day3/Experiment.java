package academy.kovalevskyi.javadeepdive.week1.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import academy.kovalevskyi.javadeepdive.week1.day2.ConcurrentHttpServerWithPath;
import java.util.Scanner;

public class Experiment {

  protected static volatile Scanner in = new Scanner(System.in);
  static String csvWithoutHeader = """
      Slava,1987,ololo
      Gleb,1985,ololo2
      Serega,1987,ololo3
      """;
  static Csv csvWithHeader = new Csv.Builder()
      .header(new String[]{"name", "born", "comment"})
      .values(new String[][]{{"Slava", "1987", "ololo"},
                             {"Gleb", "1985", "ololo2"},
                             {"Serega", "1987", "ololo3"}})
      .build();


  public static void main(String[] args) {
     testServer();

  }

  private static void testServer() {
    var serverThread = new ConcurrentHttpServerWithPath();

    serverThread.addHandler(new CsvColGetHandler(csvWithHeader, "name", "/names"));

    System.out.println("Input 'stop' to stop the server: ");

    serverThread.start();

    String command = in.nextLine();
    while (!"stop".equals(command)) {
      command = in.nextLine();
    }
    in.close();
    serverThread.stopServer();
  }


}
