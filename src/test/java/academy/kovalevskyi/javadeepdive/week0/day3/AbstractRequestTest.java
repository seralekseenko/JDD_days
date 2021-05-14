package academy.kovalevskyi.javadeepdive.week0.day3;

import static org.junit.jupiter.api.Assertions.*;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;

class AbstractRequestTest {
  static final String[] header   =  "id, name, surname, sex, age, iq, wight, have a pet?".split(", ");
  static final String[][] values = {
      "01, Vasyl, Smokey, male, 54, -5, 70, -".split(", "),
      "02, Peter, Smokey, male, 55, -1, 80, -".split(", "),
      "03, Veronica, Smokey, female, 51, 0, 57, -".split(", "),
      "04, Rick, Sanchez, male, 70, 9999, 50, +".split(", "),
      "05, Morty, Smith, male, 14, 80, 45, -".split(", "),
      "06, Cinderella, Thompson, female, 14, 80, 45, +".split(", "),
      "07, Shao, Kan, male, 10000, 99, 120, -".split(", "),
      "08, Karapet, Kamazikov, male, 30, 99, 120, -".split(", "),
      "09, Maurya, Terebonka, female, 30, 99, 60, -".split(", ")
  };
  static final Csv csv = new Csv.Builder().header(header).values(values).build();

  static String[][] removeLineInValues(int indexOfRemovableLine) {
    var result = new String[values.length - 1][];
    for (int i = 0, i2 = 0; i < result.length; i++, i2++) {
      if (i2 == indexOfRemovableLine) {
        i--;
        continue;
      }
      result[i] = values[i2];
    }
    return result;
  }
}