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

  // later you can put here any protected methods that required in multiple requests
}