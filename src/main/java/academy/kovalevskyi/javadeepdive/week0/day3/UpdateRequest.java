package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.util.Arrays;

/**
 * Update some values in table.
 */
public class UpdateRequest extends AbstractRequest<Csv> {

  /** Указывает столбец, во все строки которого нужно вписать новое значение.
   */
  private final Selector updateSelector;

  private UpdateRequest(Csv target, Selector filterSelector, Selector updateToSelector)
      throws RequestException {
    super(target.clone(), filterSelector);
    this.updateSelector = updateToSelector;
  }

  /**
   * Do update.
   *
   * @return updated table.
   * @throws RequestException if request is broken.
   */
  @Override
  protected Csv execute() {
    if (updateSelector == null) {
      return target;
    }
    var updateColumnIndex = findColumnIndex(target, updateSelector.columnName());
    var newValue = updateSelector.value();

    // Меняем все значения в колонке без предварительной фильтрации строк.
    if (filterSelector == null) {
      for (String[] line : target.values()) {
        line[updateColumnIndex] = newValue;
      }
      return target; //cloned
    }

    int filterColumnIndex = findColumnIndex(target, filterSelector.columnName());
    String filterValue = filterSelector.value();

    for (String[] line : target.values()) {
      // Меняем значения в колонке по предварительной фильтрации строк.
      if (filterValue.equals(line[filterColumnIndex])) {
        line[updateColumnIndex] = newValue;
      }
    }
    return target; // cloned
  }

  public static class Builder {

    private Selector whereSelector;
    private Selector updateSelector;
    private Csv target;

    public Builder where(Selector whereSelector) {
      this.whereSelector = whereSelector;
      return this;
    }

    public Builder update(Selector updateSelector) {
      this.updateSelector = updateSelector;
      return this;
    }

    public Builder from(Csv target) {
      this.target = target;
      return this;
    }

    public UpdateRequest build() throws RequestException {
      return new UpdateRequest(target, whereSelector, updateSelector);
    }
  }
}