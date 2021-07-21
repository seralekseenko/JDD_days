package academy.kovalevskyi.javadeepdive.week2.day1;

import academy.kovalevskyi.javadeepdive.week1.day2.HttpMethod;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Put {
  String value = HttpMethod.PUT.toString();
}
