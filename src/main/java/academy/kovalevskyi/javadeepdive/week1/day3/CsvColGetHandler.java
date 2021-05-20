package academy.kovalevskyi.javadeepdive.week1.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import academy.kovalevskyi.javadeepdive.week0.day3.RequestException;
import academy.kovalevskyi.javadeepdive.week0.day3.SelectRequest;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpMethod;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpRequest;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpRequestsHandler;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpResponse;

public class CsvColGetHandler implements HttpRequestsHandler {

  private final String path;
  private String[][] selectedLines;


  public CsvColGetHandler(Csv csv, String colName, String path) {
    var selector = new SelectRequest.Builder()
        .select(new String[]{colName})
        .from(csv)
        .build();
    this.path = path;

    try {
      selectedLines = selector.execute();
    } catch (RequestException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String path() {
    return path;
  }

  @Override
  public HttpMethod method() {
    return HttpMethod.GET;
  }

  @Override
  public HttpResponse process(HttpRequest request) {
    HttpResponse.Builder responseBuilder = new HttpResponse.Builder();
    responseBuilder
        .contentType(request.contentType())
        .httpVersion(request.httpVersion());

    var body = makeBody();
    responseBuilder.body(body);
    return responseBuilder.build();
  }

  private String makeBody() {
    StringBuilder result = new StringBuilder("[");
    for (var i = 0; i < selectedLines.length; i++) {
      result.append(selectedLines[i][0]);
      if (i != selectedLines.length - 1) {
        result.append(',');
      }
    }
    result.append("]");
    return result.toString();
  }
}