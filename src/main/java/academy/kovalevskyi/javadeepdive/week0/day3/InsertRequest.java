package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;

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
    // TODO
    return null;
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

    public InsertRequest build() {
      return new InsertRequest(target, line);
    }
  }
}