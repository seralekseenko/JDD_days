package academy.kovalevskyi.javadeepdive.week2.day0;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonHelper {

  public static <T> String toJsonString(T target) {
    if (target == null) {
      return "null"; // просто требование тестов
      // а как в JSON отображается NULL Object?
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
    // просто объект
    return objectToJson(target);
  }

  private static <T> String objectToJson(T target) {
    var clazz = target.getClass();
    Field[] fields = clazz.getDeclaredFields();
    if (fields.length == 0) {
      return "{ }";
    }

    StringBuilder sb = new StringBuilder("{\n\t");
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
      sb.append(toJsonString(Array.get(array, i)));
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


  public static <T> T fromJsonString(String json, Class<T> clazz)
      throws IllegalAccessException, InvocationTargetException, InstantiationException,
      NoSuchFieldException, NoSuchMethodException {
    if (isNullObject(json)) {
      return null;
    }
    // если просто строка
    if (clazz.equals(String.class)) {
      return clazz
          .getDeclaredConstructor(String.class)
          .newInstance(json.replace("\"", "").strip());
    }
    // если примитив
    if (isPrimitive(clazz)) {
      return parseAndGetPrimitives(json, clazz);
    }

    // если массив
    if (clazz.isArray()) {
      return parseAndGetArrays(json, clazz);
    } else { // если это объект
      // просто объект
      return parseObject(json, clazz);
    }

    // TODO is a record?
  }

  private static boolean isNullObject(String json) {
    return json == null
        //|| json.isEmpty() || json.equals("null") || json.equals("\"null\"") || json.equals("{}")
        // * это ноль и более раз! + это один и более раз!
        || json.matches("^\\s*(\\{\\s*}|\"null\"|null)*\\s*$");
  }

  private static <T> T parseObject(String json, Class<T> clazz)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException,
      IllegalAccessException, NoSuchFieldException {
    // 2 как-то парсим строки друг от друга (например по ",)
    String[] parsedObjectFields = jsonSplit(json);
    /*for (var i = 1; i <= parsedObjectFields.length; i++) {
      System.out.printf("LINE IS: \t%s\n", parsedObjectFields[i - 1]);
    }*/
    // 2.5 создаем пустой инстанс, для дальнейшего его наполнения чепухой

    var resultConstructor = clazz.getDeclaredConstructor();
    resultConstructor.setAccessible(true);
    var result = resultConstructor.newInstance();
    int lineNumber = 1;
    for (String line : parsedObjectFields) {
      // 3 как-то парсим каждую строку на имя и на значение
      var nameAndValue = line.split("\\s*:\\s*", 2);
      String fieldName = nameAndValue[0].replaceAll("\"", "");

      // 4 соотносим типы/имена с полем и записываем туда значение
      Field oneField = result.getClass().getDeclaredField(fieldName);
      // И тут я сдался!
      var valueString = nameAndValue[1].strip().replaceAll("\"", "");
      oneField.setAccessible(true);
      var extendedValue = fromJsonString(valueString, oneField.getType());
      // TODO ПОЧЕНИТЬ РАБОТУ С ВДЛЖЕННЫМИ МАССИВАМИ ВО ВЛОЖЕННЫХ КЛАССАХ
      oneField.set(result, extendedValue);
    }
    return result;
  }

  private static String[] jsonSplit(String json) {
    var cookingString = json.strip().replaceAll("^\\{|}$", "").strip();
    var wrongSplitString = cookingString.split(",\\s+");
    StringBuilder sb = new StringBuilder();
    List<String> nameAndValues = new ArrayList<>();
    var openBraces = 0;
    for (String s : wrongSplitString) {
      if (!s.contains("{") && !s.contains("}")) {
        if (sb.isEmpty()) {
          nameAndValues.add(s);
        } else {
          sb.append(",\n\t").append(s);
        }
        continue;
      }
      if (s.contains("{")) {
        sb.append(s);
        openBraces++;
        continue;
      }
      if (s.contains("}")) {
        sb.append(",\n\t").append(s);
        openBraces--;
      }
      if (openBraces == 0 && !sb.isEmpty()) {
        nameAndValues.add(sb.toString());
        sb = new StringBuilder();
      }
    }
    return nameAndValues.toArray(new String[]{});
  }

  private static <T> T parseAndGetArrays(String json, Class<T> clazz)
      throws NoSuchFieldException, InvocationTargetException, IllegalAccessException,
      InstantiationException, NoSuchMethodException {
    var cookedString = json
        .replaceAll("^\\[|]$", "")
        .strip();
    String[] splitString;
    if (clazz.getComponentType().isArray()) {
      splitString = cookedString.split("],*");
    } else {
      splitString = cookedString.split(",");
    }
    // если массив пуст, то и вернуть надо нулячий массив
    if (splitString.length == 1 && splitString[0].isEmpty()) {
      splitString = new String[]{};
    }

    var array = Array.newInstance(clazz.getComponentType(), splitString.length);
    for (var i = 0; i < splitString.length; i++) {
      var takenObject = fromJsonString(splitString[i], clazz.getComponentType());
      Array.set(array, i, takenObject);
    }
    return (T) array;
  }

  private static <T> T parseAndGetPrimitives(String json, Class<T> clazz) {
    var typeName = clazz.getSimpleName();
    return switch (typeName) {
      case "Integer", "int" -> (T) Integer.valueOf(json);
      //case "int" -> (int) Integer.parseInt(json);
      case "Long", "long" -> (T) Long.valueOf(json);
      case "Double", "double" -> (T) Double.valueOf(json);
      case "Float", "float" -> (T) Float.valueOf(json);
      case "Byte", "byte" -> (T) Byte.valueOf(json);
      case "Short", "short" -> (T) Short.valueOf(json);
      case "Boolean", "boolean" -> (T) Boolean.valueOf(json);
      case "Character", "char" -> (T) Character.valueOf(json.charAt(0));

      default -> throw new IllegalStateException("Unexpected primitive name: " + typeName);
    };
  }
}
