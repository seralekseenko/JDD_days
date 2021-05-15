package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.util.Arrays;
import java.util.Objects;

/** Base functionality for any request.
 *
 * @param <T> type of request's result.
 */
public abstract class AbstractRequest<T> {

  /** The table on which the work will be done.
   */
  Csv target;
  /** Указывает в каком столбце какое совпадение искать, что поможет отсортировать строки.
   */
  Selector filterSelector;

  protected AbstractRequest(Csv target, Selector filterSelector) {
    this.filterSelector = filterSelector;
    this.target = target;
  }

  protected AbstractRequest(Csv target) {
    this.target = target;
  }

  protected AbstractRequest() {
  }

  /** Make some request. Override required.
   *
   * @return the type that matches the request.
   * @throws RequestException if the request is broken.
   */
  protected abstract T execute() throws RequestException;

  protected int findColumnIndex(Csv csv, String columnName) {
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