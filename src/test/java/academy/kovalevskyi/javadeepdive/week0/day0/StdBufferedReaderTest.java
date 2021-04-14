package academy.kovalevskyi.javadeepdive.week0.day0;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runners.Parameterized.Parameters;

class StdBufferedReaderTest {
  String oneInputString = "Пощупай эту сочную строку без лишних символов переноса строки и перевода каретки.";
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
  public void testReadLineWithOneSimpleLine() throws IOException {
    char[] expectedResult =
        new BufferedReader(new StringReader(oneInputString)).readLine().toCharArray();
    char[] actualResult =
        assertDoesNotThrow(() -> new StdBufferedReader(new StringReader(oneInputString)).readLine());
    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  public void testReadLineWithFourLines() throws IOException {
    BufferedReader bufR = new BufferedReader(new StringReader(fourLines));
    StdBufferedReader stdBufR = new StdBufferedReader(new StringReader(fourLines));
    char[] expectedResult;
    char[] actualResult;

    for (int i = 0; i < 4; i++) {
      expectedResult = bufR.readLine().toCharArray();
      actualResult = assertDoesNotThrow(() -> stdBufR.readLine());
      assertThat(actualResult).isEqualTo(expectedResult);
    }
  }

  @Test
  public void testReadLineWithEmptyLine() {
    char[] actualResult =
        assertDoesNotThrow(() -> new StdBufferedReader(new StringReader("")).readLine());
    assertWithMessage("Empty line input:")
        .that(actualResult)
        .isEmpty(); // тут поведение не совпадает с BufferedReader — тот возвращает null!
  }
}