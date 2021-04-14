package academy.kovalevskyi.javadeepdive.week0.day0;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * This is a tutorial class that lets you read lines from an incoming reader.
 */
public class StdBufferedReader implements Closeable {

  private final char[] buffer;
  private char[] oneLine;
  private final Reader reader;
  private int readerReadResult;
  private int startIndex;

  public StdBufferedReader(Reader reader, int bufferSize) throws IOException {
    if (bufferSize <= 0) {
      throw new IllegalArgumentException();
    }

    // TODO something with small buffer and huge strings.
    this.reader = reader;
    buffer = new char[bufferSize];
    readerReadResult = reader.read(buffer, 0, buffer.length);
  }

  public StdBufferedReader(Reader reader) throws IOException {
    this(reader, 8192);
  }

  // Returns true if there is something to read from the reader.
  // False if nothing is there 😀
  public boolean hasNext() throws IOException {
    if (startIndex < buffer.length && startIndex < readerReadResult) {
      return true;
    }
    return false;
  }

  /**
   * Reads a line of text. A line is considered to be terminated by any one of a line feed ('\n'),
   * a carriage return ('\r'), a carriage return followed immediately by a line feed,
   * or by reaching the end-of-file (EOF).
   *
   * @return a one String line in char array presentation.
   * @throws IOException  - If an I/O error occurs.
   */
  public char[] readLine() {
    if (isEndOfLine(startIndex)) {
      startIndex += 1;
      return new char[0];
    }
    int endIndex = findEndOfLine(startIndex);
    oneLine = new char[endIndex - startIndex];
    System.arraycopy(buffer, startIndex, oneLine, 0, endIndex - startIndex);
    startIndex = endIndex + 1;
    return oneLine;
  }

  @Override
  public void close() throws IOException {
    if (reader == null) {
      return;
    }
    reader.close();
  }

  /**
   * Searching end of String line in buffer.
   * If the method finds any of the special characters: '\n' and '\n', it means that the
   * previous character is the end of the current line.
   *
   * @param startIndex — the beginning of search in buffer.
   * @return the next index after last character in current line.
   */
  private int findEndOfLine(int startIndex) {
    int endIndex = startIndex;
    for (int i = startIndex; i <= buffer.length && i <= readerReadResult; i++) {
      endIndex = i;
      if (isEndOfLine(i)) {
        break;
      }
    }
    return endIndex;
  }

  private boolean isEndOfLine(int index) {
    char actual = buffer[index];
    return actual == '\n' || actual == '\r';
  }
}
