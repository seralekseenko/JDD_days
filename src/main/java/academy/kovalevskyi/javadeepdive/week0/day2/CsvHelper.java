package academy.kovalevskyi.javadeepdive.week0.day2;

import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Serialize & deserialize Csv.java.
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
  public static Csv parseFile(Reader reader, boolean withHeader, char delimiter)throws IOException {
    StdBufferedReader bufRead = new StdBufferedReader(reader);
    String[] header = withHeader ? parseOneLine(bufRead, delimiter) : null;

    ArrayList<String[]> values = new ArrayList<>();

    while (bufRead.hasNext()) {
      var temp = parseOneLine(bufRead, delimiter);
      if (!temp[0].isEmpty()) {
        values.add(temp);
      }
    }
    reader.close();
    return new Csv.Builder().header(header).values(values.toArray(String[][]::new)).build();
  }

  public static void writeCsv(Writer writer, Csv csv, char delimiter) throws IOException {
    if (csv.withHeader()) {
      String[] header = csv.header();
      writeOneString(writer, header, delimiter);
    }

    // переписать на стрим
    Arrays.stream(csv.values()).forEach(e -> {
      try {
        writeOneString(writer, e, delimiter);
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    });
    writer.close();
  }

  private static void writeOneString(Writer writer, String[] strings, char delimiter)
      throws IOException {

    var result = String.join(String.valueOf(delimiter), List.of(strings));
    writer.write(result);
    writer.write(System.lineSeparator());
  }

  private static String[] parseOneLine(StdBufferedReader bufRead, char delimiter) {
    return new String(bufRead.readLine()).split(String.valueOf(delimiter));
  }
}