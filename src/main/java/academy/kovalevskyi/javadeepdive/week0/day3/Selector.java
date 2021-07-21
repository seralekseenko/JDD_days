package academy.kovalevskyi.javadeepdive.week0.day3;

/** Задает значения по которым будут отбираться записи в Csv для запросов.
 */
public record Selector(String columnName, String value) {

  public static class Builder {
    private String columnName;
    private String value;

    public Builder fieldName(String columnName) {
      if (columnName == null) {
        throw new NullPointerException("Column name cant be null!");
      }
      this.columnName = columnName;
      return this;
    }

    public Builder value(String value) {
      if (value == null) {
        throw new NullPointerException("Value cant be null!");
      }
      this.value = value;
      return this;
    }

    public Selector build() {
      return new Selector(columnName, value);
    }
  }
}
