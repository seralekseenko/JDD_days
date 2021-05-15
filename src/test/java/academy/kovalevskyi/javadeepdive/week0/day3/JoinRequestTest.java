package academy.kovalevskyi.javadeepdive.week0.day3;

import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class JoinRequestTest extends AbstractRequestTest {

  static final String[] expectedHeaders =
      ("id, name, surname, sex, age, iq, wight, have a pet?, comment, city, Has a spaceship? , "
          + "Is a human?")
          .split(", ");
  static final String[][] expectedValues = {
      "01, Vasyl, Smokey, male, 54, -5, 70, -, grate idiot, Muhosransk, +, +".split(", "),
      "02, Peter, Smokey, male, 55, -1, 80, -, low idiot, Muhosransk, -, +".split(", "),
      "03, Veronica, Smokey, female, 51, 0, 57, -, aggressive idiot, Muhosransk, -, +".split(", "),
      "04, Rick, Sanchez, male, 70, 9999, 50, +, Alcoholic, ?, +, +".split(", "),
      "05, Morty, Smith, male, 14, 80, 45, -, Kid, ?, -, -".split(", "),
      "06, Cinderella, Thompson, female, 14, 80, 45, +, Just Cinderella, Gobi, +, -".split(", "),
      "07, Shao, Kan, male, 10000, 99, 120, -, The lord, External world, -, -".split(", "),
      "08, Karapet, Kamazikov, male, 30, 99, 120, -, Wild bum, New-York, -, -".split(", "),
      "09, Maurya, Terebonka, female, 30, 99, 60, -, Little doll, Odessa, +, +".split(", ")
  };
  static final Csv expectedCsv = new Csv.Builder().header(expectedHeaders).values(expectedValues)
      .build();


  @Test
  public void join_two_valid_tables() {
    String message = String.format("Left table was:\n%s\nRight table was:\n%s", csv1, csv2);
    JoinRequest request =
        assertDoesNotThrow(() -> new JoinRequest.Builder().from(csv1).on(csv2).by("id").build(),
            "");

    var actualCsv = assertDoesNotThrow(request::execute,
        "It was valid call od 'execute'! There should be no exception in this case! ");

    assertWithMessage(message).that(actualCsv).isEqualTo(expectedCsv);
    System.out.println(actualCsv);

  }

  @Disabled
  @Test
  void experiment() {
    Set<String> valInColumn = new HashSet<>();
    var a = "a, b, c, d, e, f, g".split(", ");
    var b = "t, p, k, l, j, z, a".split(", ");
    var keyColumnIndexRight = 6;
    var result = new String[a.length + b.length - 1];

    System.arraycopy(a, 0, result, 0, a.length);
    System.arraycopy(b, 0, result, a.length, keyColumnIndexRight);
    System.arraycopy(b, keyColumnIndexRight + 1, result, a.length + keyColumnIndexRight,
        b.length - keyColumnIndexRight - 1);

    System.out.println(Arrays.toString(result));
  }


}