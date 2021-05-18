package academy.kovalevskyi.javadeepdive.week1.day0;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class HttpServer implements Runnable {

  private ServerSocket serverSocket;

  public HttpServer() {
    try {
      serverSocket = new ServerSocket(8080);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    HttpServer server = new HttpServer();
    Thread thread = new Thread(server);
    thread.start();
    Scanner in = new Scanner(System.in);

    System.out.println("Input 'stop' to stop the server: ");

    String command = in.nextLine();

    while (!"stop".equals(command)) {
      command = in.nextLine();
    }
    server.stop();
  }

  @Override
  public void run() {
    while (isLive()) {
      HttpRequestsHandler handler = null;
      try {
        handler = new HttpRequestsHandler(serverSocket.accept());
      } catch (IOException e) {
        e.printStackTrace();
      }
      if (handler != null) {
        handler.executeRequest();
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