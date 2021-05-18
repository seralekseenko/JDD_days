package academy.kovalevskyi.javadeepdive.week1.day0;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class HttpServer implements Runnable {

  private boolean live;
  private ServerSocket serverSocket;

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
    try (ServerSocket serverSocket = new ServerSocket(8080)) {
      while (isLive()) {
        this.serverSocket = serverSocket;
        try (Socket socket = serverSocket.accept()) {
          HttpRequestsHandler handler = new HttpRequestsHandler(socket);
          handler.executeRequest();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    try {
      live = false;
      this.serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean isLive() {
    return live;
  }
}