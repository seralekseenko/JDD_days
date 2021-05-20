package academy.kovalevskyi.javadeepdive.week1.day2;

import java.util.Scanner;

public class Experiment {

  protected static volatile Scanner in = new Scanner(System.in);

  public static void main(String[] args) {


    var serverThread = new ConcurrentHttpServerWithPath();

    serverThread.addHandler(new HttpRequestsHandler() {
      @Override
      public String path() {
        return "/hello";
      }

      @Override
      public HttpMethod method() {
        return HttpMethod.GET;
      }

      @Override
      public HttpResponse process(HttpRequest request) {
        return new HttpResponse.Builder().body("<h1>hi</h1>").build();
      }
    });
    serverThread.addHandler(new HttpRequestsHandler() {
      @Override
      public String path() {
        return "/";
      }

      @Override
      public HttpMethod method() {
        return HttpMethod.GET;
      }

      @Override
      public HttpResponse process(HttpRequest request) {
        return HttpResponse.OK_200;
      }
    });
    System.out.println("Input 'stop' to stop the server: ");

    serverThread.start();

    String command = in.nextLine();
    while (!"stop".equals(command)) {
      command = in.nextLine();
    }
    in.close();
    serverThread.stopServer();
  }
}
