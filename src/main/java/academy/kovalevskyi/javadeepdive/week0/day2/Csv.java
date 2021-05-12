package academy.kovalevskyi.javadeepdive.week0.day2;

import java.util.Arrays;

public record Csv(String[] header, String[][] values) {

  /** Check only input values.
   *
   * @param header - ignore.
   * @param values - should be != null.
   */
  // Очень упрощенный конструктор для record.
  public Csv {
    if (values == null) {
      throw new NullPointerException();
    }
  }

  public boolean withHeader() {
    return header != null;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o == null || o.getClass() != this.getClass()) {
      return false;
    }

    Csv income = (Csv) o;
    return Arrays.equals(this.header, income.header)
        && Arrays.deepEquals(this.values, income.values);
  }

  @Override
  public int hashCode() {
    return 31 * Arrays.hashCode(header) + Arrays.deepHashCode(values);
  }
}
