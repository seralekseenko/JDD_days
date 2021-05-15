package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.util.Arrays;

/**
 * Выбирает что-то из одной конкретной таблицы.
 * Selector задает колонку и значение для отбора.
 * Если Selector пуст, то выводиться вся таблица.
 * String[] columns задает колонки, которые нужно вернуть.
 */
public class SelectRequest extends AbstractRequest<String[][]> {

  private final String[] specificColumns;

  private SelectRequest(Csv target, Selector selector, String[] specificColumns) {

    super(target, selector);
    this.specificColumns = specificColumns;
  }

  @Override
  protected String[][] execute() throws RequestException {
    checkRequest();
    if (specificColumns == null) {
      return selectLines();
    }

    if (filterSelector == null) {
      return selectColumns(selectLines());
    }
    // selector != null!
    String[][] selectedLines = selectLines();
    return selectColumns(selectedLines);
  }

  private String[][] selectColumns(String[][] selectedLines) {
    int[] columnIds = Arrays.stream(specificColumns)
        .mapToInt(column -> findColumnIndex(target, column))
        .toArray();
    return Arrays.stream(selectedLines)
        .map(line -> {
          var newLine = new String[columnIds.length];
          for (int i = 0; i < newLine.length; i++) {
            newLine[i] = line[columnIds[i]];
          }
          return newLine;
        })
        .toArray(String[][]::new);
  }

  private void checkRequest() throws RequestException {
    if (!target.withHeader() && (specificColumns != null || filterSelector != null)) {
      throw new RequestException("No header — no select!");
    }
  }

  /** Выбирает линии по параметрам селектора.
   *
   * @return values with selected lines.
   */
  protected String[][] selectLines() {
    if (filterSelector == null) {
      return target.values();
    }
    var columnIndex = findColumnIndex(target, filterSelector.columnName());
    var searchingValue = filterSelector.value();
    return Arrays.stream(target.values())
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

    public SelectRequest build() throws RequestException {
      return new SelectRequest(target, selector, columns);
    }
  }
}