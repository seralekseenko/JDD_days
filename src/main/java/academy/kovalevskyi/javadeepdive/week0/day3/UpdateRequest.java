package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.Csv;

/**
 * Update some values in table.
 */
public class UpdateRequest extends AbstractRequest<Csv> {

  private final Selector whereSelector;
  private final Selector updateSelector;

  private UpdateRequest(Csv target, Selector whereSelector, Selector updateToSelector)
      throws RequestException {
    super(target);
    this.whereSelector = whereSelector;
    this.updateSelector = updateToSelector;
  }

  /**
   * Do update.
   *
   * @return updated table.
   * @throws RequestException if request is broken.
   */
  @Override
  protected Csv execute() throws RequestException {
    // TODO
    return null;
  }

  public static class Builder {

    private Selector whereSelector;
    private Selector updateSelector;
    private Csv target;

    public Builder where(Selector whereSelector) {
      this.whereSelector = whereSelector;
      return this;
    }

    public Builder update(Selector updateSelector) {
      this.updateSelector = updateSelector;
      return this;
    }

    public Builder from(Csv target) {
      this.target = target;
      return this;
    }

    public UpdateRequest build() throws RequestException {
      return new UpdateRequest(target, whereSelector, updateSelector);
    }
  }
}