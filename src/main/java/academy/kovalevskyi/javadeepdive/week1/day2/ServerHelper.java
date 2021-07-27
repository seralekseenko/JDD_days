package academy.kovalevskyi.javadeepdive.week1.day2;

import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpRequest.Builder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;


// Нужно сделать вложенным классом, но показывать будет неудобно.
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

  public static HttpRequest parseRequest(Socket socket) throws IOException {
    HttpRequest.Builder requestBuilder = new Builder();
    StdBufferedReader bufferedReader =
        new StdBufferedReader(new InputStreamReader(socket.getInputStream()));
    /*Где закрывать BufferedReader?  Или не надо? Почему?*/

    parseStartingLine(requestBuilder, bufferedReader);
    parseHeaders(requestBuilder, bufferedReader);

    return requestBuilder.build();
  }

  private static void parseStartingLine(Builder requestBuilder, StdBufferedReader bufferedReader) {
    var line = new String(bufferedReader.readLine());
    var splitLine = line.split(" ");
    requestBuilder
        .method(HttpMethod.parseMethod(splitLine[0]))
        .path(splitLine[1])
        .httpVersion(HttpVersion.valueOf(splitLine[2].replaceAll("[/.]", "_")));
  }


  private static void parseHeaders(Builder requestBuilder, StdBufferedReader bufferedReader) {
    var nullLineCounter = 0;

    while (bufferedReader.hasNext()) {
      var line = new String(bufferedReader.readLine());
      if (line.length() == 0) {
        nullLineCounter++;
        continue;
      }
      if (line.contains("Content-Type: ")) { // "Content-Type:".length = 13!
        requestBuilder.contentType(ContentType.valueOf(line.substring(14)
            .replaceAll("/", "_")
            .toUpperCase()));
      }
      if (nullLineCounter > 1) {
        requestBuilder.body(line);
      }
      nullLineCounter = 0;
    }
  }
}
