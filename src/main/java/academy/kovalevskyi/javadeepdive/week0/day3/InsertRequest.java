package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Вставляет данные в таблицу. Таблица может быть пустой или непустой.
 */
public class InsertRequest extends AbstractRequest<Csv> {

  private final String[] line;

  private InsertRequest(Csv target, String[] line) throws RequestException {
    super(target);
    this.line = line;
  }

  @Override
  protected Csv execute() {
    var resultBuilder = new Csv.Builder().header(target.header());
    ArrayList<String[]> values = new ArrayList<>(Arrays.asList(target.values()));
    values.add(line);
    resultBuilder.values(values.toArray(String[][]::new));
    return resultBuilder.build();
  }

  public static class Builder {

    private Csv target;
    private String[] line;

    public Builder insert(String[] line) {
      if (line == null) {
        throw new NullPointerException("New line cant be empty!");
      }
      this.line = line;
      return this;
    }

    public Builder to(Csv target) {
      if (target == null) {
        throw new NullPointerException("You cant insert anything in null!");
      }
      this.target = target;
      return this;
    }

    public InsertRequest build() throws RequestException {
      if (line.length != target.header().length) {
        throw new RequestException(String.format("The new line is different in length from the "
            + "header! Header length: %d, New line's length: %d",
            target.header().length,
            line.length));
      }
      return new InsertRequest(target, line);
    }
  }
}