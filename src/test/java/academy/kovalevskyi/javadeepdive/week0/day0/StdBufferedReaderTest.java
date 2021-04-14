package academy.kovalevskyi.javadeepdive.week0.day0;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class StdBufferedReaderTest {


  @Test
  public void testReadLineWithOneSimpleLine() {
    String inputString = "Пощупай эту сочную строку без лишних символов переноса строки и перевода каретки.";
    StringReader reader = new StringReader(inputString);
    char[] expectedResult = inputString.toCharArray();
    char[] actualResult =
        assertDoesNotThrow(() -> new StdBufferedReader(reader).readLine());

    reader.close();

    assertThat(actualResult).isEqualTo(expectedResult);

//    System.out.println(Arrays.toString(expectedResult));
  }

}