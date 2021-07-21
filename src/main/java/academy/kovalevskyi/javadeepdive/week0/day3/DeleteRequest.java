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
    var valuesAfterDelete = new ArrayList<>();
    var valueToDelete = whereSelector.value();
    if (valueToDelete == null) {
      return csv.clone(); // перерасход памяти или распиши зачем оно.
    }
    for (var line : csv.values()) {
      if (valueToDelete.equals(line[columnIndex])) {
        continue;
      }
      valuesAfterDelete.add(line);
    }

    return new Csv.Builder()
        .header(csv.header())
        .values(valuesAfterDelete.toArray(String[][]::new))
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