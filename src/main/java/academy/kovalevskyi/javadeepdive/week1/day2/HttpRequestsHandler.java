package academy.kovalevskyi.javadeepdive.week1.day2;

/*
Этот интерфейс будет реализовывать пользователи фреймворка, которые смогут добавлять свои
собственные контролы.
 */

public interface HttpRequestsHandler {
  String path();

  HttpMethod method();

  HttpResponse process(HttpRequest request);
}