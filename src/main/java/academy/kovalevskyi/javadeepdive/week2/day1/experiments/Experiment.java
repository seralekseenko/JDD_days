package academy.kovalevskyi.javadeepdive.week2.day1.experiments;

import academy.kovalevskyi.javadeepdive.week2.day1.Controller;
import academy.kovalevskyi.javadeepdive.week2.day1.Path;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import org.reflections.Reflections;

public class Experiment {

  private static Set<Class<?>> annotatedClasses =
      new Reflections("academy.kovalevskyi.javadeepdive.week2.day1.experiments")
          .getTypesAnnotatedWith(Controller.class);


  public static void main(String[] args) {

//    annotatedClasses.forEach(clazz -> Arrays.stream(clazz.getMethods())
//        .filter(method -> method.isAnnotationPresent(Path.class))
//        .forEach(method -> {
//          method.
//        }));

  }

  private static void makeAndAddHandler() {

  }


}
