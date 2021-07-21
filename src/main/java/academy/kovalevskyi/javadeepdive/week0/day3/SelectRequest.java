package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.util.Arrays;

/**
 * Выбирает что-то из одной конкретной таблицы. Selector задает колонку и значение для отбора. Если
 * Selector пуст, то выводиться вся таблица. String[] columns задает колонки, которые нужно
 * вернуть.
 */
public class SelectRequest extends AbstractRequest<String[][]> {

  private final String[] specificColumns;

  private SelectRequest(Csv csv, Selector selector, String[] specificColumns) {
    super(csv, selector);
    this.specificColumns = specificColumns;
  }

  @Override // TODO переделать возврат на Csv!
  public String[][] execute() throws RequestException {
    checkRequest();
    // Нет заданных колонок — просто возвращаем значения по селектору!
    if (specificColumns == null || specificColumns.length == 0) {
      return selectLinesWithSelector();
    }

    if (filterSelector == null) {
      return getValuesWithSpecifiedColumns(csv.values());
    }
    // selector != null && specificColumns != null
    return getValuesWithSpecifiedColumns(selectLinesWithSelector());
  }

  private String[][] getValuesWithSpecifiedColumns(String[][] values) {
    int[] columnIds = Arrays.stream(specificColumns)
        .mapToInt(column -> findColumnIndex(csv, column))
        .toArray();
    return Arrays.stream(values)
        .map(line -> rebuildLine(line, columnIds))
        .toArray(String[][]::new);
  }

  private String[] rebuildLine(String[] line, int[] columnIds) {
    var newLine = new String[columnIds.length];
    for (int i = 0; i < newLine.length; i++) {
      newLine[i] = line[columnIds[i]];
    }
    return newLine;
  }


  private void checkRequest() throws RequestException {
    if (!csv.withHeader() && (specificColumns != null || filterSelector != null)) {
      throw new RequestException("No header — no select!");
    }
  }

  /**
   * Выбирает линии по параметрам селектора.
   *
   * @return values with selected lines.
   */
  protected String[][] selectLinesWithSelector() {
    if (filterSelector == null) {
      return csv.values();
    }
    var columnIndex = findColumnIndex(csv, filterSelector.columnName());
    var searchingValue = filterSelector.value();
    return Arrays.stream(csv.values())
        .filter(line -> searchingValue.equals(line[columnIndex]))
        .toArray(String[][]::new);
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
      return new SelectRequest(target, selector, columns);
    }
  }
}