package academy.kovalevskyi.javadeepdive.week1.day2;

public enum HttpMethod {
  NO_NAME, POST, PUT, GET, DELETE;

  public static HttpMethod parseMethod(String methodName) {
    for (HttpMethod method : HttpMethod.values()) {
      if (method.toString().equals(methodName)) {
        return method;
      }
    }
    return NO_NAME;
  }
}
