package academy.kovalevskyi.javadeepdive.week1.day0;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class HttpServer implements Runnable {

  protected static final int DEFAULT_PORT = 8080;
  protected final Scanner in;

  protected volatile ServerSocket serverSocket;

  public HttpServer() {
    try {
      serverSocket = new ServerSocket(DEFAULT_PORT);
    } catch (IOException e) {
      e.printStackTrace();
    }
    in = new Scanner(System.in);
  }

  public static void main(String[] args) {
    HttpServer server = new HttpServer();
    Thread thread = new Thread(server);
    thread.start();

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
    HttpRequestsHandler handler;
    while (isLive()) {
      try {
        handler = new HttpRequestsHandler(serverSocket.accept());
        handler.executeRequest();
      } catch (IOException e) {
        // БРЕД
        if (isLive()) {
          e.printStackTrace();
        }
      }
    }
  }

  public void stop() {
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean isLive() {
    return !serverSocket.isClosed();
  }
}