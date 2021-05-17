package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.util.Arrays;
import java.util.List;

/** Вставляет данные в таблицу. Таблица может быть пустой или непустой.
 */
public class InsertRequest extends AbstractRequest<Csv> {

  private final String[] line;

  private InsertRequest(Csv target, String[] line) {
    super(target);
    this.line = line;
  }

  @Override
  protected Csv execute() throws RequestException {
    checkRequest();

    List<String[]> values = Arrays.asList(csv.values());
    values.add(line);

    return new Csv.Builder()
        .header(csv.header())
        .values(values.toArray(String[][]::new))
        .build();
  }

  private void checkRequest() throws RequestException {
    if (line.length != csv.header().length) {
      throw new RequestException(String.format("The new line is different in length from the "
              + "header! Header length: %d, New line's length: %d",
          csv.header().length,
          line.length));
    }
  }

  public static class Builder {

    private Csv target;
    private String[] line;

    public Builder insert(String[] line) {
      this.line = line;
      return this;
    }

    public Builder to(Csv target) {
      this.target = target;
      return this;
    }

    public InsertRequest build() throws RequestException {
      return new InsertRequest(target, line);
    }
  }
}