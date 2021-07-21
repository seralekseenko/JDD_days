package academy.kovalevskyi.javadeepdive.week0.day2;

import java.util.Arrays;

public record Csv(String[] header, String[][] values) {

  Csv() {
    this(null, null);
  }

  public static class Builder {

    private String[] inputHeader;
    private String[][] inputValues;

    public Builder header(String[] header) {
      this.inputHeader = header;
      return this;
    }

    public Builder values(String[][] values) {
      inputValues = values;
      return this;
    }

    public Csv build() {
      /*if (inputHeader == null) {
        throw new NullPointerException("Where is your header?!");
      }
      if (inputValues == null) {
        throw new NullPointerException("Where is your values?!");
      }*/
      return new Csv(inputHeader, inputValues);
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

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("csv(columns: %d, lines: %d)\n", header.length, values.length));
    sb.append("header=");
    sb.append(Arrays.toString(header));
    sb.append("\n");
    sb.append("values=").append(Arrays.toString(values[0]));
    sb.append('\n');
    for (var i = 1; i < values.length; i++) {
      sb.append("values=").append(Arrays.toString(values[i]));
      sb.append('\n');
    }
    return sb.toString();
  }

  @Override
  public Csv clone() {
    return new Csv(this.header.clone(), this.values.clone());
  }
}
