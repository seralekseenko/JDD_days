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

      System.out.println("\n####ПЕЧАТАЕМ ЗАПРОС####");
      System.out.println(parseFirstLine(bufferedReader.readLine()));
      int nullLineCounter = 0;
      while (bufferedReader.hasNext()) {
        var read = new String(bufferedReader.readLine());

        if (read.length() == 0) {
          nullLineCounter++;
          continue;
        }
        if (nullLineCounter == 1) {
          System.out.print(read);
        }
        //System.out.println(" ##null line counter=" + nullLineCounter);
        if (nullLineCounter > 1) {
          System.out.println("CONTENT HERE");
          System.out.println(read);
          System.out.println("CONTENT HERE");
        }

        nullLineCounter = 0;
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