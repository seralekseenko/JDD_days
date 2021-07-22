package academy.kovalevskyi.javadeepdive.week1.day2;

import static academy.kovalevskyi.javadeepdive.week1.day2.ServerHelper.parseRequest;
import static academy.kovalevskyi.javadeepdive.week1.day2.ServerHelper.selectHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/* Важно, что все хендлеры должны быть добавлены до запуска сервера.*/
public class ConcurrentHttpServerWithPath extends Thread {

  private static final int DEFAULT_PORT = 8080;
  private final ExecutorService executor;
  private final List<HttpRequestsHandler> handlers = new CopyOnWriteArrayList<>();
  private ServerSocket serverSocket;
  private boolean isAlive = false;

  public ConcurrentHttpServerWithPath() {
    this(null);
  }

  public ConcurrentHttpServerWithPath(ServerSocket serverSocket) {
    this(serverSocket, Executors.newWorkStealingPool());
  }

  public ConcurrentHttpServerWithPath(ServerSocket serverSocket, ExecutorService executor) {
    this.executor = executor;
    try {
      this.serverSocket = serverSocket != null ? serverSocket : new ServerSocket(DEFAULT_PORT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void addHandler(HttpRequestsHandler handler) {
    if (isAlive) {
      throw new UnsupportedOperationException(
          "When the server is running, you cannot add a handler.");
    }
    handlers.add(handler);
  }

  public void run() {
    System.out.println("Start server");
    isAlive = true;
    while (isLive()) {
      // в сокете найдем потоки ввода/вывода
      try (Socket socket = serverSocket.accept()) {
        Future<HttpResponse> futureResponse = executor.submit(() -> {
          // из socket.getInputStream() распарсим запрос
          HttpRequest request = parseRequest(socket);
          // Тут подбираем запросу соответствующий хендлер
          // и пихаем этот хендлер на исполнение в executor
          return selectHandler(handlers, request).process(request);
        });
        // записываем ответ в исходящий поток
        socket.getOutputStream().write(futureResponse.get().toString().getBytes());
      } catch (IOException | InterruptedException | ExecutionException e) {
        // БРЕД
        if (isLive()) {
          e.printStackTrace();
        }
        return;
      }
    }
  }


  public void stopServer() {
    isAlive = false;
    try {
      serverSocket.close();
      executor.shutdown();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.interrupt();
    System.out.println("Stop server");
  }

  public boolean isLive() {
    return isAlive;
  }
}