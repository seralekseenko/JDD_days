package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.util.Objects;

/** Base functionality for any request.
 *
 * @param <T> type of request's result.
 */
public abstract class AbstractRequest<T> {

  /** The table on which the work will be done.
   * Где private? Почему не final?!
   */
  Csv csv;
  /** Указывает в каком столбце какое совпадение искать, что поможет отсортировать строки.
   */
  Selector filterSelector;

  protected AbstractRequest(Csv csv, Selector filterSelector) {
    this.filterSelector = filterSelector;
    this.csv = csv;
  }

  protected AbstractRequest(Csv csv) {
    this.csv = csv;
  }

  protected AbstractRequest() {
  }

  /** Make some request. Override required.
   *
   * @return the type that matches the request.
   * @throws RequestException if the request is broken.
   */
  protected abstract T execute() throws RequestException;

  /** Search an index of column.
   * If index was not found — return -1.
   *
   * @param csv — the object to search in.
   * @param columnName — desired column.
   * @return index of column if it is present or -1.
   */
  static int findColumnIndex(Csv csv, String columnName) {
    var columnIndex = -1;
    var columns = csv.header();

    for (var i = 0; i < columns.length; i++) {
      if (Objects.equals(columnName, columns[i])) {
        columnIndex = i;
        break;
      }
    }
    return columnIndex;
  }
}