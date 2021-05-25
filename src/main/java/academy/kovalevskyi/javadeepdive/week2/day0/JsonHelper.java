package academy.kovalevskyi.javadeepdive.week2.day0;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class JsonHelper {

  public static <T> String toJsonString(T target) {
    if (target == null) {
      return "null"; // просто требование тестов
    }
    var clazz = target.getClass();
    if (clazz.equals(String.class)) {
      return String.format("\"%s\"", target);
    }
    if (isPrimitive(clazz)) {
      return Objects.toString(target);
    }
    if (clazz.isArray()) {
      return arrayToJson(target);
    }
    if (clazz.getDeclaredFields().length == 0) {
      return "{ }";
    } else {
      return objectToJson(target);
    }
  }

  private static <T> String objectToJson(T target) {

    StringBuilder sb = new StringBuilder("{\n\t");
    Field[] fields = target.getClass().getDeclaredFields();
    for (var i = 0; i < fields.length; i++) {
      fields[i].setAccessible(true);
      // field name
      sb.append("\"").append(fields[i].getName()).append("\": ");
      // field value
      try {
        sb.append(toJsonString(fields[i].get(target)));
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      if (i != fields.length - 1) {
        sb.append(",\n\t");
      }
    }
    return sb.append("\n}").toString();

  }

  private static <T> String arrayToJson(T array) {
    var arrayLength = Array.getLength(array);

    if (arrayLength == 0) {
      return "[ ]";
    }
    StringBuilder sb = new StringBuilder("[");
    for (var i = 0; i < arrayLength; i++) {
      var element = Array.get(array, i);
      if (element.getClass().isArray()) {
        sb.append(arrayToJson(element));
      } else {
        sb.append(toJsonString(element));
      }
      if (i != arrayLength - 1) {
        sb.append(",");
      }
    }
    sb.append("]");
    return sb.toString();
  }

  private static boolean isPrimitive(Class<?> clazz) {
    return clazz.equals(Integer.class) || clazz.equals(Long.class) || clazz.equals(Byte.class)
            || clazz.equals(Short.class) || clazz.equals(Double.class) || clazz.equals(Float.class)
            || clazz.equals(Boolean.class) || clazz.equals(Character.class)
            || clazz.equals(int.class) || clazz.equals(long.class) || clazz.equals(byte.class)
            || clazz.equals(short.class) || clazz.equals(double.class) || clazz.equals(float.class)
            || clazz.equals(boolean.class) || clazz.equals(char.class);
  }


  public static <T> T fromJsonString(String json, Class<T> cls)
      throws IllegalAccessException, InvocationTargetException, InstantiationException,
      NoSuchFieldException, NoSuchMethodException {
    if (isNullObject(json)) {
      return null;
    }
    // если просто строка
    if (cls.equals(String.class)) {
      return cls
          .getDeclaredConstructor(String.class)
          .newInstance(json.replace("\"", "").trim());
    }
    // если примитив
    if (isPrimitive(cls)) {
      return null;
    }

    // если массив
    if (cls.isArray()) {
      return null;
    }

    // просто объект
    // TODO magic

    var instance = cls.getDeclaredConstructor(String.class).newInstance();
    return instance;
  }

  protected static boolean isNullObject(String json) {
    return json == null // * - ноль и более раз!
        || json.matches("^\\s*(\\{\\s*null\\s*}|null)*\\s*$");
  }


}
