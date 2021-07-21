package academy.kovalevskyi.javadeepdive.week2.day0;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Experiment {
  private final String[] inputHeader = new String[]{"id", "name"};
  private final  String[][] inputValues = new String[][]{{"0", "zero"}, {"1", "one"}, {"2", "two"}};
//  private byte[] bytesArray = new byte[]{1, 2, 3};
//  private short[] shortsArray = new short[]{1, 2, 3};
//  private int[] intArray = new int[]{99, 100, 50};
//  private Integer[][] integerArray = new Integer[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
//  private long[] longsArray = new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
//  private double[] dobArray = new double[]{1.1, 2.2, 3.3, 4.4};
//  private float[] floArray = new float[]{1.1F, 2.2F, 3.3F, 4.4F};
//  private boolean[] boolArray = new boolean[]{true, false, true};
  private final boolean bulTrue = true;
  Csv csv = new Csv.Builder()
      .header(new String[]{"id", "name"})
      .values(new String[][]{{"0", "zero"}, {"1", "one"}, {"2", "two"}}).build();
  public final int number = 5;
//  protected float myFloat = 1.234523523F;
//  private String justString = "test string";

//  final char someChar = 'a';
//  final String emptyString = null;
//  private final Object emptyObject = new Object();

  public static void main(String[] args)
      throws NoSuchFieldException, InvocationTargetException, IllegalAccessException,
      InstantiationException, NoSuchMethodException {
    Experiment experiment = new Experiment();
    var json = JsonHelper.toJsonString(experiment);
    JsonHelper.fromJsonString(json, Experiment.class);
    //testRegEx();

  }

  private static void testRegEx() {
    Experiment experiment = new Experiment();
    var stringResult = JsonHelper.toJsonString(null);
    System.out.println("JSON: " + stringResult);
    var replacedString = stringResult.replaceAll("^\\[|]$", "");
    System.out.println("Replaced String: " + replacedString);
    var splitString = replacedString.split("(],*\\[)");
    System.out.println("Split STRING: " + Arrays.toString(splitString));
    System.out.println("Split STRING LENGTH: " + splitString.length);
    System.out.println("replace one array: " + splitString[0].replaceAll("^\\[|]$", ""));
  }

  private static <T> void testFrom(T anObject) {
    String json = JsonHelper.toJsonString(anObject);
    System.out.println("### TO JSON ###\n" + json);
    try {
      var fromJson = JsonHelper.fromJsonString(json, anObject.getClass());
      System.out.println("### AFTER DESERIALIZING ###\n" + JsonHelper.toJsonString(fromJson));
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException
        | NoSuchFieldException | NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

}
