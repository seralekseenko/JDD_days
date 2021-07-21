package academy.kovalevskyi.javadeepdive.week1.day2.experiment;

import static java.util.Arrays.asList;

import academy.kovalevskyi.javadeepdive.week1.day2.ConcurrentHttpServerWithPath;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpMethod;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpRequest;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpRequestsHandler;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Experiment {
  protected static volatile Scanner in = new Scanner(System.in);



  List<Human> humans = asList(
      new Human("Sam", asList("Buddy", "Lucy")),
      new Human("Bob", asList("Frankie", "Rosie")),
      new Human("Marta", asList("Simba", "Tilly")));
  List<String> petNames = humans.stream()
      .flatMap(human -> human.pets().stream())
      .collect(Collectors.toList());


  public static void main(String[] args) {
    // testFlatMap();
    // testOptionalOrElse();
    // testSwitchCases();
    // testRecordAndReflection();

    testServer();


  }

  private static void testServer() {
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

  private static void testRecordAndReflection() {
    Experiment ex = new Experiment();
    System.out.println("test RECORD");
    for (Human human : ex.humans) {
      System.out.println("\nONE HUMAN:");
      System.out.println(human.getClass());
      System.out.println("Is a record: " + human.getClass().isRecord());
      System.out.println(Arrays.toString(human.getClass().getRecordComponents()));
    }
  }

  private static void testSwitchCases() {
    /*final var i = new Random().nextInt();
    String result =
    switch (i) {
      case 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 -> String.format("it is a figure: %d", i);
      case 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 -> String
          .format("It is a positive number: %d", i);
      default -> String.format("It is a negative number or figure: %d", i);
    };*/
  }

  private static void testOptionalOrElse() {
    var strings = new String[]{"trololo", "", null};
    for (String s : strings) {
      System.out.println(Optional.ofNullable(s).orElse("Empty, syka"));
    }
  }

  private static void testFlatMap() {
    Experiment ex = new Experiment();
    System.out.println(ex.petNames); // output [Buddy, Lucy, Frankie, Rosie, Simba, Tilly]
  }

}
