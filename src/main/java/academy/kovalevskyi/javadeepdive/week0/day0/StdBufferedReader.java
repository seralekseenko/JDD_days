package academy.kovalevskyi.javadeepdive.week0.day0;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * This is a tutorial class that lets you read lines from an incoming reader.
 */
public class StdBufferedReader implements Closeable {

  private Reader reader;
  private final int buffSize;

  public StdBufferedReader(Reader reader, int bufferSize) {
    this.reader = reader;
    this.buffSize = bufferSize;
  }

  public StdBufferedReader(Reader reader) {
    this(reader, 8192);
  }

  // Returns true if there is something to read from the reader.
  // False if nothing is there
  public boolean hasNext() throws IOException {
    return false;
  }

  // Returns a line (everything till the next line)
  public char[] readLine() throws IOException {
    char[] resultCharArray = new char[0];
    return resultCharArray;
  }

  @Override
  public void close() throws IOException {
    if (reader == null)
      return;
    try {
      reader.close();
    } finally {
      reader = null;
    }
  }
}
