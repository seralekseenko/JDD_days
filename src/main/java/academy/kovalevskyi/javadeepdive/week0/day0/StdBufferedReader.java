package academy.kovalevskyi.javadeepdive.week0.day0;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * This is a tutorial class that lets you read lines from an incoming reader.
 */
public class StdBufferedReader implements Closeable {

  private  char[] buffer;
  private final Reader reader;
  private int bufferSize;
  private int readerReadResult;
  private int startIndex;

  public StdBufferedReader(Reader reader, int bufferSize) throws IOException {
    if (bufferSize < 2) {
      throw new IllegalArgumentException();
    }
    if (reader == null) {
      throw new NullPointerException();
    }
    
    this.buffer = new char[bufferSize + 2];
    this.reader = reader;
    this.bufferSize = bufferSize;

    // первое чтение
    this.readerReadResult = reader.read(buffer, 0, bufferSize);
    if (readerReadResult > 0) {
      takeFullLine();
    }
  }

  public StdBufferedReader(Reader reader) throws IOException {
    this(reader, 8192);
  }

  /**
   * Giv an answer: "can reading one more line?".
   *
   * @return true if can & false is can't.
   */
  public boolean hasNext() {
    return startIndex < buffer.length && startIndex < readerReadResult + 1;
  }

  /**
   * Reads a line of text. A line is considered to be terminated by any one of a line feed ('\n'),
   * a carriage return ('\r'), a carriage return followed immediately by a line feed,
   * or by reaching the end-of-file (EOF).
   *
   * @return a one String line in char array presentation.
   */
  public char[] readLine() {
    if (readerReadResult == -1) {
      return null;
    }
    /*if (!hasNext()) {
      return null;
    }*/
    /*if (isEndOfFile(startIndex)) {
      return null;
    }*/

    int endIndex = findEndOfLine(startIndex);
    if (isEndOfLine(startIndex)) {
      startIndex += 1;
      return new char[0];
    }


    char[] oneLine = new char[endIndex - startIndex];
    System.arraycopy(buffer, startIndex, oneLine, 0, endIndex - startIndex);
    startIndex = endIndex + 1;
    return oneLine;
  }

  private boolean isEndOfFile(int startIndex) {
    return buffer[startIndex] == '\u0000';
  }

  @Override
  public void close() throws IOException {
    if (reader == null) {
      return;
    }
    reader.close();
    buffer = null;
  }

  private void takeFullLine() throws IOException {
    while (buffer[bufferSize - 1] != (char) 0) {
      var oldBufferSize = bufferSize;
      this.bufferSize = bufferSize * 2;
      var tempArray = new char[bufferSize + 1];
      System.arraycopy(buffer, 0, tempArray, 0, oldBufferSize + 1);
      this.buffer = tempArray;
      var tempReaderReadResult = reader.read(buffer, oldBufferSize, oldBufferSize);
      this.readerReadResult += Math.max(tempReaderReadResult, 0);
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
    for (int i = startIndex; i <= buffer.length && i <= readerReadResult; i++) {
      endIndex = i;
      if (isEndOfLine(i)) {
        break;
      }
    }
    return endIndex;
  }

  private boolean isEndOfLine(int index) {
    if (index >= buffer.length) {
      return true;
    }
    char actual = buffer[index];
    return actual == '\n' || actual == '\r';
  }
}
