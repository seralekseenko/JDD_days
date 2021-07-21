package academy.kovalevskyi.javadeepdive.week2.day1;

import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpMethod;
import java.io.OutputStream;
import java.util.Set;
import org.reflections.Reflections;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestServer extends Thread {


  private final String packagePath;
  private final Set<Class<?>> annotatedClasses;

  public RestServer(String packagePath) {
    this.packagePath = packagePath;
    this.annotatedClasses = new Reflections(packagePath).getTypesAnnotatedWith(Controller.class);
  }


  public void run() {
    // TODO
  }
  public void stopServer() {
    // TODO
  }
  public boolean isLive() {
    // TODO
    return true;
  }
}