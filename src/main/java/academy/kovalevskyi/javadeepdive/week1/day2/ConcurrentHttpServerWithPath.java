package academy.kovalevskyi.javadeepdive.week1.day2;

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
        System.out.println(socket.isClosed());
        // из socket.getInputStream() распарсим запрос
        // TODO нужно не закрыть умудриться сокет
        request = parseRequest(socket);
        socket = serverSocket.accept();
        System.out.println(socket.isClosed());
      } catch (IOException e) {
        // БРЕД
        if (isLive()) {
          e.printStackTrace();
        } else {
          return;
        }
      }
      // Тут подбираем запросу соответствующий хендлер
      HttpRequestsHandler currentHandler = selectHandler(request);
      // Тут пихаем хендлер на исполнение в executor
      HttpRequest finalRequest = request;
      Future<HttpResponse> workedHandler = executor.submit(() -> currentHandler.process(
          finalRequest));
      try {
        // записываем ответ в исходящий поток
        System.out.println(socket.isClosed());
        writeResponse(socket, workedHandler.get());
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
  }

  private void writeResponse(Socket socket, HttpResponse httpResponse) {
    System.out.println(socket.isClosed());
    try (OutputStream out = socket.getOutputStream()) {
      out.write(httpResponse.toString().getBytes());
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(socket.isClosed());
  }

  private HttpRequestsHandler selectHandler(HttpRequest request) {
    HttpRequestsHandler result;
    for (var handler : handlers) {
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
    };

    /*return handlers.parallelStream()
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
        });*/
  }

  private HttpRequest parseRequest(Socket socket) {
    HttpRequest.Builder resultBuilder = new Builder();
    try (StdBufferedReader bufferedReader =
        new StdBufferedReader(new InputStreamReader(socket.getInputStream()))) {

      var line = new String(bufferedReader.readLine());
      var splitLine = line.split(" ");
      resultBuilder
          .method(HttpMethod.valueOf(splitLine[0]))
          .path(splitLine[1])
          .httpVersion(HttpVersion.valueOf(splitLine[2].replaceAll("[/.]", "_")));
      int nullLineCounter = 0;

      while (bufferedReader.hasNext()) {
        line = new String(bufferedReader.readLine());
        if (line.length() == 0) {
          nullLineCounter++;
          continue;
        }
        if (line.contains("Content-Type: ")) {
          resultBuilder.contentType(ContentType.valueOf(line
              .substring(14)
              .replaceAll("/", "_")
              .toUpperCase()));
        }
        if (nullLineCounter > 1) {
          resultBuilder.body(line);
        }
        nullLineCounter = 0;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return resultBuilder.build();
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