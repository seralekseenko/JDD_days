package academy.kovalevskyi.javadeepdive.week1.day2;

public enum ContentType {
  TEXT_HTML,
  TEXT_CSV,
  APPLICATION_JSON,
  MULTIPART_MIXED,
  IMAGE_PNG,
  MESSAGE_HTTP;

  @Override
  public String toString() {
    return this.name().toLowerCase().replace('_', '/');
  }
}