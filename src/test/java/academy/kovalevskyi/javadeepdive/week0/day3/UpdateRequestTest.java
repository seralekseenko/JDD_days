package academy.kovalevskyi.javadeepdive.week0.day3;


import academy.kovalevskyi.javadeepdive.week0.day2.Csv;
import org.junit.jupiter.api.Test;

class UpdateRequestTest extends AbstractRequestTest {

  @Test
  public void experiments() throws RequestException {
    AbstractRequest<Csv> abstractRequest = new UpdateRequest.Builder()
        .from(csv)
        .where(new Selector.Builder().fieldName("surname").value("Smokey").build())
        .update(new Selector.Builder().fieldName("surname").value("Smokey").build())
        .build();
    var selectionResult = abstractRequest.selectLines();
    for (int i = 0; i < selectionResult.length; i++) {
      selectionResult[i] = null;
    }
    //System.out.println(csv);
  }
}