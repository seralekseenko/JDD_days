package academy.kovalevskyi.javadeepdive.week2.day0;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ToJSONTest {

  @Test
  void toJsonString() {
    System.out.println(JsonHelper.toJsonString(ObjectCollection.nullObject));
  }
}