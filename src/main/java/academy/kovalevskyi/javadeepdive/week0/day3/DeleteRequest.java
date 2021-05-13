package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;


/** DeleteRequest создает новый CSV, в который копирует все записи из оригинального CSV,
 * кроме тех, которые должны быть удалены. Строки для удаления определяются через
 * селектор (Selector).
 */
public class DeleteRequest extends AbstractRequest<Csv> {

  private final Selector whereSelector;

  private DeleteRequest(Csv target, Selector whereSelector) {
    super(target);
    this.whereSelector = whereSelector;
  }

  @Override
  protected Csv execute() throws RequestException {
    // TODO
    return null;
  }

  public static class Builder {

    private Csv target;
    private Selector whereSelector;

    public Builder where(Selector whereSelector) {
      this.whereSelector = whereSelector;
      return this;
    }

    public Builder from(Csv target) {
      this.target = target;
      return this;
    }

    public DeleteRequest build() {
      return new DeleteRequest(target, whereSelector);
    }
  }
}