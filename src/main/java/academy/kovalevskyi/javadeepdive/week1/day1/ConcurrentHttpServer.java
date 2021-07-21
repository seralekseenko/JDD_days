package academy.kovalevskyi.javadeepdive.week1.day1;

import academy.kovalevskyi.javadeepdive.week1.day0.HttpRequestsHandler;
import academy.kovalevskyi.javadeepdive.week1.day0.HttpServer;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentHttpServer extends HttpServer {

  static final ExecutorService EXECUTOR = Executors.newWorkStealingPool(2);

  public ConcurrentHttpServer() {
    super();
  }

  public static void main(String[] args) {
    ConcurrentHttpServer server = new ConcurrentHttpServer();
    new Thread(server).start();

    System.out.println("Input 'stop' to stop the server: ");

    String command = server.in.nextLine();
    while (!"stop".equals(command)) {
      command = server.in.nextLine();
    }
    server.in.close();
    server.stop();
  }

  @Override
  public void run() {
    while (isLive()) {
      try {
        EXECUTOR.execute(new HttpRequestsHandler(serverSocket.accept())::executeRequest);
      } catch (IOException e) {
        // БРЕД
        if (isLive()) {
          e.printStackTrace();
        }
      }
    }
  }

  @Override
  public void stop() {
    super.stop();
    EXECUTOR.shutdown();
  }
}