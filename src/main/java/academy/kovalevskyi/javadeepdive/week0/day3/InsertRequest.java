package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.util.ArrayList;
import java.util.Arrays;

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

    ArrayList<String[]> values = new ArrayList<>(Arrays.asList(target.values()));
    values.add(line);

    return new Csv.Builder()
        .header(target.header())
        .values(values.toArray(String[][]::new))
        .build();
  }

  private void checkRequest() throws RequestException {
    if (line.length != target.header().length) {
      throw new RequestException(String.format("The new line is different in length from the "
              + "header! Header length: %d, New line's length: %d",
          target.header().length,
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