package academy.kovalevskyi.javadeepdive.week0.day0;

import static com.google.common.truth.Truth.assertThat;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class StdBufferedReaderTest {
  String oneInputString = "Очень простая строка.";
  String fourLines = """
      Этих несколько строк будут щупать ваш мозг не один час!
      Но необходимо верить в свои силы!
      Сделайте перерыв, погладьте зверушку и расскажите ей о своей проблеме.
      Все у вас получиться!
      """;

  @Test
  public void testWithNullReader() {
    assertThrows(NullPointerException.class, () -> new StdBufferedReader(null),
        "Input reader was null!");
  }

  @Test
  public void testWithIllegalBufferSize() {
    assertThrows(IllegalArgumentException.class,
        () -> new StdBufferedReader(InputStreamReader.nullReader(), -1),
        "Buffer size should be > 0!");
    assertThrows(IllegalArgumentException.class,
        () -> new StdBufferedReader(InputStreamReader.nullReader(), 0),
        "Buffer size should be > 0!");
    assertDoesNotThrow(() -> new StdBufferedReader(InputStreamReader.nullReader(), 1),
        "Buffer size was > 0!");
  }

  @Test
  public void testReadLineWithEmptyLine() {
    char[] actualResult =
        assertDoesNotThrow(
            () -> new StdBufferedReader(new StringReader(""), 10).readLine());
    assertWithMessage("It was one empty line.")
        .that(actualResult)
        .isEmpty(); // тут поведение не совпадает с BufferedReader — тот возвращает null!
  }

  @Test
  public void testReadLineWithOneSimpleLine() throws IOException {
    char[] expectedResult =
        new BufferedReader(new StringReader(oneInputString), 21).readLine().toCharArray();
    char[] actualResult =
        assertDoesNotThrow(
            () -> new StdBufferedReader(new StringReader(oneInputString), 21).readLine());
    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @ValueSource(ints = {99999, 100000000, 5555555, 6666666, 8974525})
  public void testReadLineWithSimpleLineAndHugeBuffer(int bufSize) throws IOException {
    char[] expectedResult =
        new BufferedReader(new StringReader(oneInputString), bufSize).readLine().toCharArray();
    char[] actualResult =
        assertDoesNotThrow(
            () -> new StdBufferedReader(new StringReader(oneInputString), bufSize).readLine());
    assertThat(actualResult).isEqualTo(expectedResult);
  }
  
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
  public void testReadLineWithSmallBufferAndFourLines(int bufSize) throws IOException {
    char[] expectedResult =
        new BufferedReader(new StringReader(fourLines), bufSize).readLine().toCharArray();
    char[] actualResult = assertDoesNotThrow(
            () -> new StdBufferedReader(new StringReader(fourLines), bufSize).readLine());
    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  public void readLineWithLongLineAndSmallBuffer() throws Exception {
    var bufferedReader = new StdBufferedReader(new StringReader(fourLines), 2);
    var javaBufferedReader = new BufferedReader(new StringReader(fourLines));
    var expectedResult = javaBufferedReader.readLine().toCharArray();
    assertThat(bufferedReader.readLine())
        .isEqualTo(expectedResult);
  }


  @Test
  public void testReadLineWithFourLines() throws IOException {
    BufferedReader bufR = new BufferedReader(new StringReader(fourLines));
    StdBufferedReader stdBufR = new StdBufferedReader(new StringReader(fourLines));
    char[] expectedResult;
    char[] actualResult;

    for (int i = 0; i < 4; i++) {
      expectedResult = bufR.readLine().toCharArray();
      actualResult = assertDoesNotThrow(stdBufR::readLine);
      assertThat(actualResult).isEqualTo(expectedResult);
    }
  }

  @Test
  public void testReadLineWithEmptyLinesBeforeString() throws IOException {
    checkWithDifficultString("\n\n\n\n" + oneInputString, 5);
  }

  @Test
  public void testReadLineWithEmptyLinesBetweenStrings() throws IOException {
    checkWithDifficultString(oneInputString + "\n\n\n\n" + oneInputString, 5);
  }

  @Test
  public void testReadLineWithEmptyLinesAfterString() throws IOException {
    checkWithDifficultString(oneInputString + "\n\n\n\n", 4);
  }

  private void checkWithDifficultString(String inputString, int numberOfLines) throws IOException {
    BufferedReader bufR = new BufferedReader(new StringReader(inputString), 100);
    StdBufferedReader stdBufR = new StdBufferedReader(new StringReader(inputString), 100);
    char[] expectedResult;
    char[] actualResult;
    for (int i = 0; i < numberOfLines; i++) {
      expectedResult = bufR.readLine().toCharArray();
      actualResult = assertDoesNotThrow(stdBufR::readLine);
      assertWithMessage("Number of the reading string: " + (i + 1))
          .that(actualResult)
          .isEqualTo(expectedResult);
    }
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
  public void hasNextWhenEmpty() throws IOException {
    var bufferedReader = new StdBufferedReader(new StringReader(""), 10);
    assertWithMessage("How many lines in empty file?")
        .that(bufferedReader.hasNext())
        .isFalse();
  }

  @Test
  public void hasNextWithSimpleLine() throws Exception {
    var stdBufR = new StdBufferedReader(new StringReader(oneInputString), 50);
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
    var stdBufR = new StdBufferedReader(new StringReader(oneInputString), bufSize);
    assertWithMessage("Has next, when file contains one line and huge buffer?")
        .that(stdBufR.hasNext())
        .isTrue();
    stdBufR.readLine();
    assertWithMessage("Has next after `readLine()`, "
        + "when file contains one line and huge buffer?")
        .that(stdBufR.hasNext())
        .isFalse();
  }

  // TODO make a test with four lines and very smallest buffer
  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
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
      assertWithMessage("Hase next after `readLine()`, when file contains four line?")
          .that(stdBufR.hasNext())
          .isTrue();
    }
    stdBufR.readLine();
    assertWithMessage("It is really has next after N iteration of `readLine()`, "
            + "when file contains only four lines?!")
        .that(stdBufR.hasNext())
        .isFalse();
  }

  @Test // баганутый тест
  public void readLineWith5EmptyLiner() throws Exception {
    var bufferedReader = new StdBufferedReader(new StringReader("\n\n\n\n"), 256);
    var text = "Text in file: \\n\\n\\n\\n";
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
}