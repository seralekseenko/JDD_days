package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.util.Objects;

/**
 * Выбирает что-то из одной конкретной таблицы.
 */
public class SelectRequest extends AbstractRequest<String[][]> {

  private final Selector selector;
  private final String[] columns;

  private SelectRequest(Csv target, Selector selector, String[] columns) {
    super(target);
    this.selector = selector;
    this.columns = columns;
  }

  @Override
  protected String[][] execute() throws RequestException {
    // TODO
    return null;
  }

  public static class Builder {

    private Csv target;
    private Selector selector;
    private String[] columns;

    public Builder where(Selector selector) {
      this.selector = selector;
      return this;
    }

    public Builder select(String[] columns) {
      this.columns = columns;
      return this;
    }

    public Builder from(Csv target) {
      this.target = target;
      return this;
    }

    public SelectRequest build() {
      // Objects.nonNull(target); // Зачем это? Результат игнориться!
      return new SelectRequest(target, selector, columns);
    }
  }
}