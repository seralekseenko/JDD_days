package academy.kovalevskyi.javadeepdive.week0.day2;
import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Serialize & deserialize Csv.java.
 */
public class CsvHelper {

  public static Csv parseFile(Reader reader) throws IOException {
    return parseFile(reader, false, ',');
  }

  /** Deserializer CSV.
   *
   * @param reader reading from file.
   * @param withHeader talk about header in CSV.
   * @param delimiter setting delimiter for parsing CSV.
   * @return an CSV-object.
   * @throws IOException if something wrong with process of reading from file.
   */
  public static Csv parseFile(Reader reader, boolean withHeader, char delimiter) throws IOException {
    StdBufferedReader bufRead = new StdBufferedReader(reader);
    String[] header = withHeader ? parseOneLine(bufRead, delimiter) : null;

    ArrayList<String[]> values = new ArrayList<>();

    while (bufRead.hasNext()) {
      values.add(parseOneLine(bufRead, delimiter));
    }
    return new Csv(header, values.toArray(new String[][]{}));
  }

  public static void writeCsv(Writer writer, Csv csv, char delimiter) throws IOException {
    if (csv.withHeader()) {
      // переписать на стрим
      for (String headValue : csv.header()) {
        writer.write(headValue);
        writer.write(delimiter);
      }
    }

    // переписать на стрим
    for (String[] mainArray : csv.values()) {
      for (String str : mainArray) {
        writer.write(str);
      }
    }

  }

  private static String[] parseOneLine(StdBufferedReader bufRead, char delimiter) {
    return new String(bufRead.readLine()).split(String.valueOf(delimiter));
  }
}