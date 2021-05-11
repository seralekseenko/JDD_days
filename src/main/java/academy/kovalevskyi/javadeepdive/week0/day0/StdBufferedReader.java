package academy.kovalevskyi.javadeepdive.week0.day0;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * This is a tutorial class that lets you read lines from an incoming reader.
 */
public class StdBufferedReader implements Closeable {


  private static int defaultCharBufferSize = 8192;


  private  char[] buffer;
  private final Reader reader;
  private int bufferSize;
  private int readerReadResult;
  private int startIndex = 0;

  public StdBufferedReader(Reader reader, int bufferSize) throws IOException {
    if (bufferSize <= 0) {
      throw new IllegalArgumentException();
    }
    if (reader == null) {
      throw new NullPointerException();
    }
    
    this.buffer = new char[bufferSize];
    this.reader = reader;
    this.bufferSize = bufferSize;

    fillBuffer();
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
    if (isEndOfLine(startIndex)) {
      startIndex += 1;
      return new char[0];
    }

    // Is end of file?
    if (buffer[startIndex] == (char) 0) {
      return null;
    }
    char[] oneLine;
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
    buffer = null;
  }

  private void fillBuffer() throws IOException {
    // Первое чтение.
    this.readerReadResult = reader.read(buffer, 0, bufferSize);
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
    char actual = buffer[index];
    return actual == '\n' || actual == '\r';
  }
}
