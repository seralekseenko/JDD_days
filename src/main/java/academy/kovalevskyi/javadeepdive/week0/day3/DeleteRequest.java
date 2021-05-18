package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.util.ArrayList;


/** DeleteRequest создает новый CSV, в который копирует все строки из оригинального CSV,
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
  protected Csv execute() {
    // find a column index
    var columnIndex = findColumnIndex(csv, whereSelector.columnName());

    // find all matches TODO maybe 'flatMap'??
    ArrayList<String[]> newValues = new ArrayList<>();
    var searchingValue = whereSelector.value();
    if (searchingValue == null) {
      return csv.clone();
    }
    for (String[] line : csv.values()) {
      if (searchingValue.equals(line[columnIndex])) {
        continue;
      }
      newValues.add(line);
    }

    return new Csv.Builder()
        .header(csv.header())
        .values(newValues.toArray(String[][]::new))
        .build();
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

    public DeleteRequest build() throws RequestException {
      return new DeleteRequest(target, whereSelector);
    }
  }
}