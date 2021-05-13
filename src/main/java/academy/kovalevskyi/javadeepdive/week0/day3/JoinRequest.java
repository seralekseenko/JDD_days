package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;

/** This query joins two different tables into one by key field.
 */
public class JoinRequest extends AbstractRequest<Csv> {

  private final Csv leftTable;
  private final Csv rightTable;
  private final String keyField;

  private JoinRequest(Csv leftTable, Csv rightTable, String keyField) {
    this.leftTable = leftTable;
    this.rightTable = rightTable;
    this.keyField = keyField;
  }

  @Override
  protected Csv execute() throws RequestException {
    // TODO
    return null;
  }

  public static class Builder {

    private Csv leftTable;
    private Csv rightTable;
    private String keyField;

    public Builder from(Csv leftTable) {
      this.leftTable = leftTable;
      return this;
    }

    public Builder on(Csv rightTable) {
      this.rightTable = rightTable;
      return this;
    }

    public Builder by(String keyField) {
      this.keyField = keyField;
      return this;
    }

    public JoinRequest build() {
      return new JoinRequest(leftTable, rightTable, keyField);
    }
  }
}