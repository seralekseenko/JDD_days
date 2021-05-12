package academy.kovalevskyi.javadeepdive.week0.day2;

import java.util.Arrays;

public record Csv(String[] header, String[][] values) {

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
    sb.append("csv{header=");
    sb.append(Arrays.toString(header));
    sb.append(", ");
    sb.append("values=");
    sb.append(Arrays.deepToString(values));
    sb.append('}');
    return sb.toString();
  }
}
