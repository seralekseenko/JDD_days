package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;

/** Base functionality for any request.
 *
 * @param <T> type of request's result.
 */
public abstract class AbstractRequest<T> {

  /** The table on which the work will be done.
   */
  Csv target;

  protected AbstractRequest(Csv target) throws RequestException {
    checkHeader(target);
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

  protected void checkHeader(Csv csv) throws RequestException {
    if (!csv.withHeader()) {
      throw new RequestException("The csv must have a header!");
    }
  }

  protected int findColumnIndex(Csv csv, String columnName) {
    var columnIndex = 0;
    for (String currentColumnName : csv.header()) {
      if (columnName.equals(currentColumnName)) {
        break;
      }
      columnIndex++;
    }
    return columnIndex;
  }
  // later you can put here any protected methods that required in multiple requests
}