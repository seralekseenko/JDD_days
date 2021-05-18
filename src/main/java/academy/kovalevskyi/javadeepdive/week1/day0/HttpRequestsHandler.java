package academy.kovalevskyi.javadeepdive.week1.day0;

import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HttpRequestsHandler {

  private final Socket socket;

  public HttpRequestsHandler(Socket socket) {
    this.socket = socket;
  }

  public void executeRequest() {

    try (StdBufferedReader bufferedReader =
        new StdBufferedReader(new InputStreamReader(socket.getInputStream()));
        OutputStream out = socket.getOutputStream()) {

      var firstReading = true;
      System.out.println("####ПЕЧАТАЕМ ЗАПРОС####");
      while (bufferedReader.hasNext()) {
        var read = bufferedReader.readLine();

        if (read.length != 0 && !firstReading) {
          System.out.println(read);
        }

        if (firstReading) {
          System.out.println(parseFirstLine(read));
          firstReading = false;
        }
      }

      String response = """
          HTTP/1.1 200 OK\r
          Content-Length: 20\r
          Content-Type: text/html\r
          \r
          <b>It works!</b>\r
          \r
          """;
      out.write(response.getBytes());
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String parseFirstLine(char[] read) {
    var splitLine = new String(read).split(" ");
    return String.format("Method: %s\nPath: %s\nProtocol version: %s",
        splitLine[0],
        splitLine[1],
        splitLine[2]);
  }
}