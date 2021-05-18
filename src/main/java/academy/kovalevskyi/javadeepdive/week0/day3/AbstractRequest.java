package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.util.Objects;

/** Base functionality for any request.
 *
 * @param <T> type of request's result.
 */
public abstract class AbstractRequest<T> {

  /** The table on which the work will be done.
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

  static int findColumnIndex(Csv csv, String columnName) {
    var columnIndex = -1;
    var columns = csv.header();

    for (int i = 0; i < columns.length; i++) {
      if (Objects.equals(columnName, columns[i])) {
        columnIndex = i;
        break;
      }
    }
    return columnIndex;
  }
}