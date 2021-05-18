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
//      System.out.println("\r\n");
//      System.out.println("\r\n");

      var response = "HTTP/1.1 200 OK\r\n"
          + "Content-Length: 50\r\n"
          + "Content-Type: text/html\r\n"
          + "\r\n"
          + "<b>It works!</b>"
          + "\r\n"
          + "\r\n";
      out.write(response.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}