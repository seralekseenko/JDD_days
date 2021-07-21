package academy.kovalevskyi.javadeepdive.week1.day2;

import static academy.kovalevskyi.javadeepdive.week1.day2.RequestParser.parseRequest;

import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpRequest.Builder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

  private ServerSocket serverSocket;
  private boolean isAlive = false;
  private StdBufferedReader bufferedReader;
  private OutputStream out;
  private final ExecutorService executor;


  private final List<HttpRequestsHandler> handlers = new CopyOnWriteArrayList<>();

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

      Socket socket = null;
      HttpRequest request = null;
      try {
        // в сокете найдем потоки ввода/вывода
        socket = serverSocket.accept();
        // из socket.getInputStream() распарсим запрос
        request = parseRequest(socket);
      } catch (IOException e) {
        // БРЕД
        if (isLive()) {
          e.printStackTrace();
        }
        return;
      }
      // Тут подбираем запросу соответствующий хендлер
      HttpRequestsHandler currentHandler = selectHandler(request);
      // Тут пихаем хендлер на исполнение в executor
      HttpRequest finalRequest = request;
      Future<HttpResponse> workedHandler = executor.submit(() -> currentHandler.process(
          finalRequest));
      // записываем ответ в исходящий поток
      try {
        writeResponse(socket, workedHandler.get());
      } catch (IOException | InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
  }

  private void writeResponse(Socket socket, HttpResponse httpResponse) throws IOException {
    out = socket.getOutputStream();
    out.write(httpResponse.toString().getBytes());
    //System.out.println(httpResponse);
    socket.close();
  }

  private HttpRequestsHandler selectHandler(HttpRequest request) {
    /*for (var handler : handlers) {
      if (handler.path().equals(request.path()) && handler.method().equals(request.httpMethod())) {
        return handler;
      }
    }
    return new HttpRequestsHandler() {
      @Override
      public String path() {
        return null;
      }

      @Override
      public HttpMethod method() {
        return null;
      }

      @Override
      public HttpResponse process(HttpRequest request) {
        return HttpResponse.ERROR_404;
      }
    };*/

    return handlers.parallelStream()
        .filter(handler ->
            request.httpMethod().equals(handler.method()) && request.path().equals(handler.path()))
        .findAny()
        .orElse(new HttpRequestsHandler() {
          @Override
          public String path() {
            return null;
          }

          @Override
          public HttpMethod method() {
            return null;
          }

          @Override
          public HttpResponse process(HttpRequest request) {
            return HttpResponse.ERROR_404;
          }
        });
  }

  public void stopServer() {
    isAlive = false;
    try {
      serverSocket.close();
      executor.shutdown();
      if (bufferedReader != null) {
        bufferedReader.close();
      }
      if (out != null) {
        out.close();
      }
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