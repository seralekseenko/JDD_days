package academy.kovalevskyi.javadeepdive.week0.day2;

import java.io.IOException;
import java.io.StringReader;

public class Experiments {

  static String csvWithoutHeader = """
                                    Slava,1987,ololo
                                    Gleb,1985,ololo2
                                    Serega,1987,ololo3
                                    """;

  public static void main(String[] args) throws IOException {
    var testCsv = CsvHelper.parseFile(new StringReader(csvWithoutHeader));
  }

}
