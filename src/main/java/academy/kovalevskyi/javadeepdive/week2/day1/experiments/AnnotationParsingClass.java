package academy.kovalevskyi.javadeepdive.week2.day1.experiments;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationParsingClass {

  public static void main(String[] args) {

    try {
      for (Method method : AnnotationExampleClass.class
//          .getClassLoader()
//          .loadClass("academy.kovalevskyi.javadeepdive.week2.day1.experiments.AnnotationExampleClass")
          .getDeclaredMethods()) {
        // проверяем, присутствует ли аннотация MethodInfo в методе
        if (method.isAnnotationPresent(MethodInfo.class)) {
          try {
            // проходим по всем доступным аннотациям в методе
            for (Annotation annot : method.getDeclaredAnnotations()) {
              System.out.println("Аннотация в методе '"
                  + method + "' : " + annot);
            }
            MethodInfo methodAnno = method.getAnnotation(MethodInfo.class);
            if (methodAnno.revision() == 1) {
              System.out.println("Метод с ревизией номер 1 = "
                  + method);
            }

          } catch (Throwable ex) {
            ex.printStackTrace();
          }
        }
      }
    } catch (SecurityException /*| ClassNotFoundException*/ e) {
      e.printStackTrace();
    }
  }
}
