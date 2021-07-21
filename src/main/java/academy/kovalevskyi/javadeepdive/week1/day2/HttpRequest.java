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
    private String path;
    private HttpMethod httpMethod;
    private ContentType contentType;
    private HttpVersion httpVersion;
    private String body;

    public Builder path(String path) {
      if (path != null && !path.isEmpty()) {
        this.path = path;
      }
      return this;
    }

    public Builder method(HttpMethod method) {
        this.httpMethod = method;
      return this;
    }

    public Builder body(String body) {
        this.body = body;
      return this;
    }

    public Builder contentType(ContentType contentType) {
        this.contentType = contentType;
      return this;
    }

    public Builder httpVersion(HttpVersion httpVersion) {
        this. httpVersion = httpVersion;
      return this;
    }

    public HttpRequest build() {
      return new HttpRequest(Optional.ofNullable(path).orElse("/"),
          Optional.ofNullable(httpMethod).orElse(HttpMethod.GET),
          Optional.ofNullable(body),
          Optional.ofNullable(contentType).orElse(ContentType.TEXT_HTML),
          Optional.ofNullable(httpVersion).orElse(HttpVersion.HTTP_1_1));
    }
  }
}
