package academy.kovalevskyi.javadeepdive.week0.day0;

import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class StdBufferedReaderTest {

  private final String empty;
  private final String smallInputString;
  private final String oneLine;
  private final String fiveEmptyLines;
  private final String fourLines;
  private final String longLine;
  private final String superLongLine;

  {
    var longString = new StringBuilder(300000);
    IntStream.range(0, 300000).forEach(i -> {
      if (new Random().nextInt() < 10) {
        longString.append("\n");
      } else {
        longString.append(i);
      }
    });
    empty = "";
    smallInputString = "Очень короткая строка.";
    oneLine = "asdfasdfsafasdfsad asfasdfsadfsdafsda";
    fiveEmptyLines = String.format("%n%n%n%n");
    fourLines = """
        Этих несколько строк будут щупать ваш мозг не один час!
        Но необходимо верить в свои силы!
        Сделайте перерыв, погладьте зверушку и расскажите ей о своей проблеме.
        Все у вас получиться!
        """;
    longLine = "sadfasdfsadfh;asfhda;sdsadfasdfsadfh;asfhda;sdsadfasdfsadfh;asfhda;sdsadfasdfsadfh;"
        + "asfhda;sdsadfasdfsadfh;asfhda;sdsadfasdfsadfh;asfhda;sdsadfasdfsadfh;asfhda;sdsadf"
        + "asdfsadfh;asfhda;sdsadfasdfsadfh;asfhda;sdsadfasdfsadfh;asfhda;sdsadfasdfsadfh;asf"
        + "hda;sdsadfasdfsadfh;asfhda;sdsadfasdfsadfh;asfhda;sdsadfasdfsadfh;asfhda;sdsadfasd"
        + "fsadfh;asfhda;sdsadfasdfsadfh;asfhda;sdsadfasdfsadfh;asfhda;sdsadfasdfsadfh;asfhda"
        + ";sdsadfasdfsadfh;asfhda;sdsadfasdfsadfh;asfhda;sdsadfasdfsadfh;asfhda;sd";
    superLongLine = longString.toString();
  }

  @Test
  public void testClose() throws IOException {
    var reader = mock(Reader.class);
    var bufferedReader = new StdBufferedReader(reader);
    bufferedReader.close();
    verify(reader).close();
  }

  @Test
  public void testCloseable() throws IOException {
    var reader = mock(Reader.class);
    try (var bufferedReader = new StdBufferedReader(reader)) {
      //
    }
    verify(reader).close();
  }

  @Test
  public void hasNextWhenEmpty() throws Exception {
    var bufferedReader = new StdBufferedReader(new StringReader(empty), 256);
    assertWithMessage("Checking if hasNext returns false for empty file")
        .that(bufferedReader.hasNext())
        .isFalse();
  }

  @Test
  public void hasNextWithSmallLine() throws Exception {
    var stdBufR = new StdBufferedReader(new StringReader(smallInputString), 50);
    assertWithMessage("Has next, when file contains one line?")
        .that(stdBufR.hasNext())
        .isTrue();
    stdBufR.readLine();
    assertWithMessage("Has next after `readLine()`, when file contains one line?")
        .that(stdBufR.hasNext())
        .isFalse();
  }

  @ParameterizedTest
  @ValueSource(ints = {99999, 100000000, 5555555, 6666666, 8974525})
  public void hasNextWithSmallLineAndHugeBuffer(int bufSize) throws IOException {
    var stdBufR = new StdBufferedReader(new StringReader(smallInputString), bufSize);
    assertWithMessage("Has next, when file contains one line and huge buffer?")
        .that(stdBufR.hasNext())
        .isTrue();
    stdBufR.readLine();
    assertWithMessage("Has next after `readLine()`, "
        + "when file contains one line and huge buffer?")
        .that(stdBufR.hasNext())
        .isFalse();
  }

  @Test
  public void hasNextWhen1Liner() throws Exception {
    var bufferedReader = new StdBufferedReader(new StringReader(oneLine), 256);
    assertWithMessage("Checking if hasNext returns false for file with 1 line")
        .that(bufferedReader.hasNext())
        .isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 4, 5, 6, 7, 8, 9, 10, 11})
  public void hasNextWithFourLinesAndSmallBuffer(int bufSize) throws IOException {
    var stdBufR = new StdBufferedReader(new StringReader(fourLines), bufSize);
    for (int i = 1; i < 6; i++) {
      assertWithMessage("Has next, when file contains four lines and small buffer?")
          .that(stdBufR.hasNext())
          .isTrue();
      stdBufR.readLine();
    }
    assertWithMessage("Has next after four `hasNext()`?")
        .that(stdBufR.hasNext())
        .isFalse();
  }

  @Test
  public void hasNextWithFourLines() throws Exception {
    var stdBufR = new StdBufferedReader(new StringReader(fourLines));
    assertWithMessage("Hase next, when file contains four line?")
        .that(stdBufR.hasNext())
        .isTrue();
    for (int i = 0; i < 4; i++) {
      stdBufR.readLine();
      assertWithMessage("Has next after `readLine()`, when file contains four line?")
          .that(stdBufR.hasNext())
          .isTrue();
    }
    stdBufR.readLine();
    assertWithMessage("It is really has next after four iteration of `readLine()`, "
        + "when file contains only four lines?!")
        .that(stdBufR.hasNext())
        .isFalse();
  }

  @Test
  public void callConstructorWithIllegalBufferSize() {
    String message = String.format(
        "Buffer size should be positive number and 2 bytes or above!%n"
            + "IllegalArgumentException should be thrown!");
    assertThrows(IllegalArgumentException.class,
        () -> new StdBufferedReader(InputStreamReader.nullReader(), 1), message);
    assertThrows(IllegalArgumentException.class,
        () -> new StdBufferedReader(InputStreamReader.nullReader(), 0), message);
    assertThrows(IllegalArgumentException.class,
        () -> new StdBufferedReader(InputStreamReader.nullReader(), -1), message);
  }

  @Test
  public void callConstructorWithNullReader() {
    assertThrows(NullPointerException.class,
        () -> new StdBufferedReader(null), String.format(
            "Reader instance should be non-null!%nNullPointerException should be thrown!"));
  }

  @Test
  public void testReadLineWithEmptyLine() throws IOException {
    char[] actualResult =
        assertDoesNotThrow(
            () -> new StdBufferedReader(new StringReader(empty), 10).readLine());
    assertWithMessage("It was one empty line.")
        .that(actualResult)
        .isNull(); // java BufferedReader возвращает null!
  }

  @Test
  public void readLineWithOneLine() throws Exception {
    var expected = oneLine.toCharArray();
    var bufferedReader = new StdBufferedReader(new StringReader(oneLine), 256);
    assertWithMessage(
        String.format("Text in file: %s%nReading one line", String.valueOf(expected)))
        .that(bufferedReader.readLine())
        .isEqualTo(expected);
  }

  @Test
  public void readLineWith5EmptyLines() throws Exception {
    var text = "Incoming text was: \"\\n\\n\\n\\n\" — Unix, \"\\r\\n\\r\\n\\r\\n\\r\\n\" — Windows";
    String message = String.format("%s, \nBuffer size is: DEFAULT",
        text);
    StdBufferedReader bufferedReader =
        assertDoesNotThrow(() -> new StdBufferedReader(new StringReader(fiveEmptyLines)), message);
    assertWithMessage(String.format("%s%nReading one line", text))
        .that(bufferedReader.readLine())
        .hasLength(0);
    assertWithMessage(String.format("%s%nChecking if second line is available", text))
        .that(bufferedReader.hasNext())
        .isTrue();
    assertWithMessage(String.format("%s%nReading second line", text))
        .that(bufferedReader.readLine())
        .hasLength(0);
    assertWithMessage(String.format("%s%nChecking if third line is available", text))
        .that(bufferedReader.hasNext())
        .isTrue();
    assertWithMessage(String.format("%s%nReading third line", text))
        .that(bufferedReader.readLine())
        .hasLength(0);
    assertWithMessage(String.format("%s%nChecking if fourth line is available", text))
        .that(bufferedReader.hasNext())
        .isTrue();
    assertWithMessage(String.format("%s%nReading fourth line", text))
        .that(bufferedReader.readLine())
        .hasLength(0);
    assertWithMessage(String.format("%s%nChecking if fifth line is available", text))
        .that(bufferedReader.hasNext())
        .isTrue();
    assertWithMessage(String.format("%s%nReading fifth line", text))
        .that(bufferedReader.readLine())
        .hasLength(0);
    assertWithMessage(String.format("%s%nChecking if six line is available", text))
        .that(bufferedReader.hasNext())
        .isFalse();
  }

  @Test
  public void readLineWithLongLineAndSmallBuffer() throws Exception {
    int bufSize = 2;
    String message = String.format("Line in file is: %n\"%s\", %nBuffer size is: %d",
        longLine, bufSize);

    var javaBufferedReader = assertDoesNotThrow(
        () -> new BufferedReader(new StringReader(longLine), bufSize),
        message);

    var bufferedReader = new StdBufferedReader(new StringReader(longLine), bufSize);

    char[] expectedResult = javaBufferedReader.readLine().toCharArray();
    char[] actualResult = assertDoesNotThrow(bufferedReader::readLine, message);

    assertWithMessage(message).that(actualResult).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
  public void testReadLineWithSmallBufferAndFourLines(int bufSize) throws IOException {
    String message = String.format("Lines in file was: \n\"%s\", \nBuffer size is: %d",
        fourLines, bufSize);
    char[] expectedResult =
        new BufferedReader(new StringReader(fourLines), bufSize).readLine().toCharArray();
    char[] actualResult = assertDoesNotThrow(
        () -> new StdBufferedReader(new StringReader(fourLines), bufSize).readLine(),
        message);
    assertWithMessage(message).that(actualResult).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @ValueSource(ints = {99999, 100000000, 5555555, 6666666, 8974525})
  public void testReadLineWithSmallLineAndHugeBuffer(int bufSize) throws IOException {
    String message = String.format("Line in file is: \n\"%s\", \nBuffer size is: %d",
        smallInputString, bufSize);
    char[] expectedResult =
        new BufferedReader(new StringReader(smallInputString), bufSize).readLine().toCharArray();
    char[] actualResult =
        assertDoesNotThrow(
            () -> new StdBufferedReader(new StringReader(smallInputString), bufSize).readLine(),
            message);
    assertWithMessage(message).that(actualResult).isEqualTo(expectedResult);
  }

  @Test
  public void testReadLineWithOneSmallLine() throws IOException {
    int bufSize = 22;
    String message = String.format("Line in file is: \n\"%s\", \nBuffer size is: %d",
        smallInputString, bufSize);
    char[] expectedResult =
        new BufferedReader(new StringReader(smallInputString), bufSize).readLine().toCharArray();
    char[] actualResult =
        assertDoesNotThrow(
            () -> new StdBufferedReader(new StringReader(smallInputString), bufSize).readLine(),
            message);
    assertWithMessage(message).that(actualResult).isEqualTo(expectedResult);
  }

  @Test
  public void testReadLineWithFourLinesAndDefaultBufferSize() throws IOException {
    String message = String.format("Lines in file are: \n\"%s\", \nBuffer size is: DEFAULT",
        fourLines);
    BufferedReader bufR = new BufferedReader(new StringReader(fourLines));
    StdBufferedReader stdBufR =
        assertDoesNotThrow(() -> new StdBufferedReader(new StringReader(fourLines)), message);

    char[] expectedResult;
    char[] actualResult;

    for (int i = 0; i < 4; i++) {
      expectedResult = bufR.readLine().toCharArray();
      actualResult = assertDoesNotThrow(stdBufR::readLine);
      assertWithMessage(message).that(actualResult).isEqualTo(expectedResult);
    }
  }

  @Test
  public void readLineWithWarAndPeace() throws Exception {
    var stdReader =
        new StdBufferedReader(new StringReader(superLongLine));
    var javaReader =
        new BufferedReader(new StringReader(superLongLine));
    var line = 0;
    while (javaReader.ready()) {
      char[] expectedResult = null;
      var stringExpectedLine = javaReader.readLine();
      if (stringExpectedLine != null) {
        expectedResult = stringExpectedLine.toCharArray();
      }
      var actualResult = stdReader.readLine();
      assertWithMessage(String.format("Reading war and peace, line number: %d", line++))
          .that(actualResult)
          .isEqualTo(expectedResult);
    }
  }

  @Test
  public void testReadLineWithEmptyLinesBeforeString() throws IOException {
    checkWithDifficultString("\n\n\n\n" + smallInputString, 5);
  }

  @Test
  public void testReadLineWithEmptyLinesBetweenStrings() throws IOException {
    checkWithDifficultString(smallInputString + "\n\n\n\n" + smallInputString, 5);
  }

  @Test
  public void testReadLineWithEmptyLinesAfterString() throws IOException {
    checkWithDifficultString(smallInputString + "\n\n\n\n", 4);
  }

  private void checkWithDifficultString(String inputString, int numberOfLines) throws IOException {
    String message = String.format("Lines in file are: \n\"%s\", \nBuffer size is: DEFAULT",
        inputString);
    BufferedReader bufR = new BufferedReader(new StringReader(inputString), 50);
    StdBufferedReader stdBufR =
        assertDoesNotThrow(() -> new StdBufferedReader(new StringReader(inputString), 50), message);
    char[] expectedResult;
    char[] actualResult;
    for (int i = 0; i < numberOfLines; i++) {
      expectedResult = bufR.readLine().toCharArray();
      actualResult = assertDoesNotThrow(stdBufR::readLine, message);
      assertWithMessage("Number of the reading string: " + (i + 1))
          .that(actualResult)
          .isEqualTo(expectedResult);
    }
  }
}