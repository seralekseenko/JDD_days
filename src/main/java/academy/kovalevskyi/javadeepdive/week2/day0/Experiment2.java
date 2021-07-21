package academy.kovalevskyi.javadeepdive.week2.day0;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Experiment2 {

  public static void main(String[] args) {
    var input = "name line1,   name line2,      line3, {\"lineX:\", \"lineY\",  \"lineZ\", "
        + "{lineA, lineB, "
        + "{\"deepLine\": \"[super, deep, line]\"}}},"
        + "\nline4, line5, name: {value1, value2, value3}";
    var pattern = Pattern.compile("\\{.*},?|\\w+,?|\\W+,+");
    var matcher = pattern.matcher(input);
    var result = matcher.results()
        .map(entry -> {
          var text = entry.group().strip();
          var index = text.lastIndexOf(',');
          if (index != -1) {
            text = text.substring(0, index + 1);
          }
          return text;
        })
        .toArray(String[]::new);
    System.out.println(Arrays.toString(result) + "length=" + result.length);
    for (String str : result) {
      System.out.println(str);
    }
  }

}
