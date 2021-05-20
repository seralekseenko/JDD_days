package academy.kovalevskyi.javadeepdive.week1.day2;

import java.util.Optional;

public record HttpRequest(String path,
                          HttpMethod httpMethod,
                          Optional<String> body,
                          ContentType contentType,
                          HttpVersion httpVersion) {

  /*
  Важно! По умолчанию билдер должен создавать запрос с:
  Http версией 1.1
  Content type text/html
  Method GET
  path “/”
  ПОЛЕЗНАЯ СТАТЬЯ ПРО Optional https://vertex-academy.com/tutorials/ru/java-8-optional/
  */

  public static class Builder {
    private String path = "/";
    private HttpMethod httpMethod = HttpMethod.GET;
    private ContentType contentType = ContentType.TEXT_HTML;
    private HttpVersion httpVersion = HttpVersion.HTTP_1_1;
    private Optional<String> body = Optional.empty();

    public Builder path(String path) {
      if (path != null && !path.isEmpty()) {
        this.path = path;
      }
      return this;
    }

    public Builder method(HttpMethod method) {
      if (method != null) {
        this.httpMethod = method;
      }
      return this;
    }

    public Builder body(String body) {
      if (body != null && !body.isEmpty()) {
        this.body = Optional.<String>of(body);
      }
      return this;
    }

    public Builder contentType(ContentType contentType) {
      if (contentType != null) {
        this.contentType = contentType;
      }
      return this;
    }

    public Builder httpVersion(HttpVersion httpVersion) {
      if (httpVersion != null) {
        this. httpVersion = httpVersion;
      }
      return this;
    }

    public HttpRequest build() {
      return new HttpRequest(path, httpMethod, body, contentType, httpVersion);
    }
  }
}
