package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.util.HashSet;
import java.util.Set;

/** This query joins two different tables into one by key field.
 * Данный запрос делает join.  На CSV, с которыми делается join, накладываются следующие требования:
 *  - оба CSV должны быть с headers DONE.
 *  - записи в поле (столбце), по которым делается join, должны быть уникальными DONE.
 *  - количество строк в обоих CSV должно быть равным DONE.
 *  - на каждый уникальный ключ в одном CSV должна быть запись в другом CSV !!!NOT DONE!!!!!.
 */
public class JoinRequest extends AbstractRequest<Csv> {

  private final Csv leftTable;
  private final Csv rightTable;
  private final String keyColumn;
  private  int keyColumnIndexLeft;
  private  int keyColumnIndexRight;

  private JoinRequest(Csv leftTable, Csv rightTable, String keyColumn) {
    super();
    this.leftTable = leftTable;
    this.rightTable = rightTable;
    this.keyColumn = keyColumn;
  }

  @Override
  protected Csv execute() throws RequestException {
    // проверка наличия хедеров
    checkHeader(leftTable);
    checkHeader(rightTable);

    keyColumnIndexLeft = findColumnIndex(leftTable, keyColumn);
    keyColumnIndexRight = findColumnIndex(rightTable, keyColumn);
    // дополнительные проверки
    checkRequest();

    String[] mergedHeader = mergeLine(leftTable.header(), rightTable.header());
    var tempLeftVal = leftTable.values();
    var tempRightVal = rightTable.values();
    String[][] mergedValues = new String[tempLeftVal.length][];
    for (int i = 0; i < mergedValues.length; i++) {
      mergedValues[i] = mergeLine(tempLeftVal[i], tempRightVal[i]);
    }

    return new Csv.Builder().header(mergedHeader).values(mergedValues).build();
  }

  private String[] mergeLine(String[] left, String[] right) {
    var result = new String[left.length + right.length - 1];
    System.arraycopy(left, 0, result, 0, left.length);
    System.arraycopy(right, 0, result, left.length, keyColumnIndexRight);
    System.arraycopy(right, keyColumnIndexRight + 1, result,
        left.length + keyColumnIndexRight, right.length - keyColumnIndexRight - 1);
    return result;
  }

  private void checkHeader(Csv csv) throws RequestException {
    if (!csv.withHeader()) {
      throw new RequestException("The csv must have a header!");
    }
  }

  private void checkRequest() throws RequestException {
    // Есть ли нужная колонка в обеих таблицах?
    if (findColumnIndex(leftTable, keyColumn) == -1
        || findColumnIndex(rightTable, keyColumn) == -1) {
      throw new RequestException("One of tables haven't required column!");
    }

    // проверка количества строк
    if (leftTable.values().length != rightTable.values().length) {
      throw new RequestException();
    }

    // Проверка уникальности записей в ключевом столбце
    if (haveMatchesInSelectedColumn(leftTable, keyColumnIndexLeft)
        || haveMatchesInSelectedColumn(rightTable, keyColumnIndexRight)) {
      throw new RequestException();
    }
  }

  private boolean haveMatchesInSelectedColumn(Csv csv, int columnIndex) {
    Set<String> valInColumn = new HashSet<>();
    var allValues = csv.values();
    for (String[] line : allValues) {
      if (!valInColumn.add(line[columnIndex])) {
        return  true;
      }
    }
    return false;
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