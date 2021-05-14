package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import academy.kovalevskyi.javadeepdive.week0.day2.Csv.Builder;
import java.util.ArrayList;


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
    if (!target.withHeader()) {
      throw new RequestException("The csv must have a header!");
    }
    var resultBuilder = new Csv.Builder().header(target.header());
    // find a column index
    var columnIndex = 0;
    for (String currentColumnName : target.header()) {
      if (whereSelector.columnName().equals(currentColumnName)) {
        break;
      }
      columnIndex++;
    }

    // find all matches
    ArrayList<String[]> newValues = new ArrayList<>();
    var searchingValue = whereSelector.value();
    for (String[] line : target.values()) {
      if (searchingValue != null && searchingValue.equals(line[columnIndex])) {
        continue;
      }
      newValues.add(line);
    }
    resultBuilder.values(newValues.toArray(new String[][]{}));

    return resultBuilder.build();
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