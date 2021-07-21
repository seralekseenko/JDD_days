package academy.kovalevskyi.javadeepdive.week1.day2;

import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpRequest.Builder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public class ServerHelper {


  public static HttpRequestsHandler selectHandler(List<HttpRequestsHandler> handlers,
      HttpRequest request) {
    /*for (var handler : handlers) {
      if (handler.path().equals(request.path()) && handler.method().equals(request.httpMethod())) {
        return handler;
      }
    }
    return new HttpRequestsHandler() {
      @Override
      public String path() {
        return null;
      }

      @Override
      public HttpMethod method() {
        return null;
      }

      @Override
      public HttpResponse process(HttpRequest request) {
        return HttpResponse.ERROR_404;
      }
    };*/

    return handlers.parallelStream()
        .filter(handler ->
            request.httpMethod().equals(handler.method()) && request.path().equals(handler.path()))
        .findAny()
        .orElse(new HttpRequestsHandler() {});
  }

  // TODO REFACTOR
  public static HttpRequest parseRequest(Socket socket) throws IOException {
    HttpRequest.Builder resultBuilder = new Builder();
    StdBufferedReader bufferedReader =
        new StdBufferedReader(new InputStreamReader(socket.getInputStream()));

    var line = new String(bufferedReader.readLine());
    var splitLine = line.split(" ");
    resultBuilder
        .method(HttpMethod.valueOf(splitLine[0]))
        .path(splitLine[1])
        .httpVersion(HttpVersion.valueOf(splitLine[2].replaceAll("[/.]", "_")));
    int nullLineCounter = 0;

    while (bufferedReader.hasNext()) {
      line = new String(bufferedReader.readLine());
      if (line.length() == 0) {
        nullLineCounter++;
        continue;
      }
      if (line.contains("Content-Type: ")) {
        resultBuilder.contentType(ContentType.valueOf(line
            .substring(14)
            .replaceAll("/", "_")
            .toUpperCase()));
      }
      if (nullLineCounter > 1) {
        resultBuilder.body(line);
      }
      nullLineCounter = 0;
    }

    return resultBuilder.build();
  }

  public static void writeResponse(Socket socket, HttpResponse httpResponse) throws IOException {
    socket.getOutputStream().write(httpResponse.toString().getBytes());
    //System.out.println(httpResponse);
    socket.close();
  }

}
