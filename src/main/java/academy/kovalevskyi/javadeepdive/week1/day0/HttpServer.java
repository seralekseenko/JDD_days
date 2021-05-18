package academy.kovalevskyi.javadeepdive.week1.day0;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class HttpServer implements Runnable {

  static final int DEFAULT_PORT = 8080;
  static Scanner in = new Scanner(System.in);

  private volatile ServerSocket serverSocket;

  public HttpServer() {
    try {
      serverSocket = new ServerSocket(DEFAULT_PORT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    HttpServer server = new HttpServer();
    Thread thread = new Thread(server);
    thread.start();

    System.out.println("Input 'stop' to stop the server: ");

    String command = in.nextLine();

    while (!"stop".equals(command)) {
      command = in.nextLine();
    }
    in.close();
    server.stop();
  }

  @Override
  public void run() {
    while (isLive()) {
      HttpRequestsHandler handler;
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