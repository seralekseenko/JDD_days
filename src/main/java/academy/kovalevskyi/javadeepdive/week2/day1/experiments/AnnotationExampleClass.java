package academy.kovalevskyi.javadeepdive.week2.day1.experiments;

import academy.kovalevskyi.javadeepdive.week2.day1.Controller;
import academy.kovalevskyi.javadeepdive.week2.day1.Path;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AnnotationExampleClass {

  public static void main(String[] args) {
  }

  @Override
  @MethodInfo(author = "Serg", comments = "Main method", date = "Aug 10 2015", revision = 1)
  public String toString() {
    return "Переопределили метод toString() ";
  }

  @Deprecated
  @MethodInfo(comments = "устаревший метод", date = "Aug 10 2015")
  public static void oldMethod() {
    System.out.println("Этот метод не стоит дальше использовать");
  }

  @SuppressWarnings({ "unchecked", "deprecation" })
  @MethodInfo(author = "Serg", comments = "Main method", date = "Aug 10 2015", revision = 4)
  @Path("/trololo")
  public static void genericsTest() throws FileNotFoundException {
    List l = new ArrayList();
    l.add("фыва");
    oldMethod();
  }

}