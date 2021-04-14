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

  public StdBufferedReader(Reader reader, int bufferSize) {
    if (bufferSize < 3) {
      throw new IllegalArgumentException();
    }
    this.reader = reader;
    buffer = new char[bufferSize];
    try {
      readerReadResult = reader.read(buffer, 0, buffer.length);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public StdBufferedReader(Reader reader) {
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
  public char[] readLine() throws IOException {
    int endIndex = findEndOfLine(startIndex) + 1;
    oneLine = new char[endIndex];
    System.arraycopy(buffer, startIndex, oneLine, 0, endIndex);
    shiftStartIndex(endIndex);
    


//    System.out.println("ready: " + reader.ready());
//    System.out.println("readerReadResult: " + readerReadResult);
//    System.out.println(String.copyValueOf(buffer));
//    System.out.println(buffer[readerReadResult]);

    
    return oneLine;
  }

  @Override
  public void close() throws IOException {
    if (reader == null) {
      return;
    }
    try {
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void shiftStartIndex(int lengthOfCurrentLine) {
    startIndex = lengthOfCurrentLine;
    while (startIndex < buffer.length && startIndex < readerReadResult && isEndOfLine(startIndex)) {
      startIndex += 1;
    }
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
    for (int i = startIndex; i < buffer.length && i < readerReadResult; i++) {
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
