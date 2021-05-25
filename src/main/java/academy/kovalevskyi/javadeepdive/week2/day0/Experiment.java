package academy.kovalevskyi.javadeepdive.week2.day0;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.lang.constant.ClassDesc;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Experiment {
  private final String[] inputHeader = new String[]{"id", "name"};
  private String[][] inputValues = new String[][]{{"0", "zero"}, {"1", "one"}, {"2", "two"}};
  private byte[] bytesArray = new byte[]{1, 2, 3};
  private short[] shortsArray = new short[]{1, 2, 3};
  private int[] emptyArray = new int[]{};
  private int[] integerArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
  private long[] longsArray = new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
  private double[] dobArray = new double[]{1.1, 2.2, 3.3, 4.4};
  private float[] floArray = new float[]{1.1F, 2.2F, 3.3F, 4.4F};
  private boolean[] boolArray = new boolean[]{true, false, true};
  Csv csv = new Csv.Builder()
      .header(inputHeader)
      .values(inputValues).build();
  public final int number = 5;
  protected float myFloat = 1.234523523F;
  private String justString = "test string";
  private final boolean bulTrue = true;
  final char someChar = 'a';
  final String emptyString = null;
  private final Object emptyObject = new Object();

  public static void main(String[] args) {
    String json = "";
    System.out.println(JsonHelper.isNullObject(json));
    /*Experiment experiment = new Experiment();
    System.out.println(JsonHelper.toJsonString(experiment));*/

    /*try {
      printReflection(csv);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }*/
  }

  private static <T> void printReflection(T target) throws IllegalAccessException {
    Class<?> clazz = target.getClass();
    var classFields = clazz.getDeclaredFields();
    for (var field : classFields) {
      field.setAccessible(true);
      System.out.println("\n###NEW FIELD\ndeclared field name: " + field.getName());

      var fullTypeName = field.getGenericType().getTypeName().split("\\.");
      var shortTypeName = fullTypeName[fullTypeName.length - 1];
      System.out.println("Type name: " + shortTypeName);

      var fieldValueDescriptor = field.get(target).getClass().describeConstable().get();
      var isArray2 = fieldValueDescriptor.isArray();
      var isClassOrInterface = fieldValueDescriptor.isClassOrInterface();
      var isPrimitive = fieldValueDescriptor.isPrimitive();
      System.out.println("fieldValueDescriptor: " + fieldValueDescriptor);
      System.out.println("isArray2: " + isArray2);
      System.out.println("isClassOrInterface: " + isClassOrInterface);
      System.out.println("isPrimitive: " + isPrimitive);
      var typename = "String[]" + ".class";
      System.out.println("isInstance(typename): "
          + field.get(target).getClass().isInstance(typename));
      System.out.println("FIELD VALUE: " + field.get(target));
      System.out.println("fieldValueDescriptor.displayName(): "
          + fieldValueDescriptor.displayName());
    }
  }
}
