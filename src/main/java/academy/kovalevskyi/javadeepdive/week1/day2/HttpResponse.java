package academy.kovalevskyi.javadeepdive.week1.day2;

public record HttpResponse(ResponseStatus status, ContentType contentType, String body, HttpVersion httpVersion) {

  public static final HttpResponse ERROR_404 =
      new Builder().status(ResponseStatus.ERROR_404).build();
  public static final HttpResponse OK_200 =
      new Builder().status(ResponseStatus.OK).build();
  public static final HttpResponse ERROR_500 =
      new Builder().status(ResponseStatus.ERROR_500).build();

  /*
  Важно! По умолчанию билдер должен создавать ответ с:
  кодом 200
  Content type text/html
  Http версией 1.1
  */

  public static class Builder {
    private ResponseStatus status = ResponseStatus.OK;
    private ContentType contentType = ContentType.TEXT_HTML;
    private HttpVersion httpVersion = HttpVersion.HTTP_1_1;
    private String body;


    public Builder status(ResponseStatus status) {
      this.status = status;
      return this;
    }

    public Builder contentType(ContentType contentType) {
      this.contentType = contentType;
      return this;
    }

    public Builder body(String body) {
      this.body = body;
      return this;
    }

    public Builder httpVersion(HttpVersion version) {
      this.httpVersion = httpVersion;
      return this;
    }

    public HttpResponse build() {
      return new HttpResponse(status, contentType, body, httpVersion);
    }
  }

  public enum ResponseStatus {
    OK(200, "OK"), ERROR_404(404, "not found"), ERROR_500(500, "server error");
    final int code;
    final String description;

    ResponseStatus(int code, String description) {
      this.code = code;
      this.description = description;
    }

    @Override
    public String toString() {
      // TODO переделать в соответствии стандарту HTTP ответа!
      return code + " " + description;
    }
  }
}