package academy.kovalevskyi.javadeepdive.week1.day2;

/*
Этот интерфейс будет реализовывать пользователи фреймворка, которые смогут добавлять свои
собственные контролы.
 */

public interface HttpRequestsHandler {

  default String path() {
    return null;
  }

  default HttpMethod method() {
    return null;
  }

  default HttpResponse process(HttpRequest request) {
    return HttpResponse.ERROR_404;
  }
}