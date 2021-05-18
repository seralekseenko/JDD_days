package academy.kovalevskyi.javadeepdive.week1.day0;

import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public record HttpRequestsHandler(Socket socket) {

  public void executeRequest() {

    try (StdBufferedReader bufferedReader =
        new StdBufferedReader(new InputStreamReader(socket.getInputStream()));
        OutputStream out = socket.getOutputStream()) {

      while (bufferedReader.hasNext()) {
        System.out.println(bufferedReader.readLine());
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
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}