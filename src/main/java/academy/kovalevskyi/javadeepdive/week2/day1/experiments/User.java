package academy.kovalevskyi.javadeepdive.week2.day1.experiments;

public record User(String name, String surname, byte age, boolean isAlive) {

  /**
   * {@code User} builder static inner class.
   */
  public static final class Builder {

    private String name;
    private String surname;
    private byte age;
    private boolean isAlive;

    public Builder() {
    }

    /**
     * Sets the {@code name} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param name the {@code name} to set
     * @return a reference to this Builder
     */
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Sets the {@code surname} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param surname the {@code surname} to set
     * @return a reference to this Builder
     */
    public Builder surname(String surname) {
      this.surname = surname;
      return this;
    }

    /**
     * Sets the {@code age} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param age the {@code age} to set
     * @return a reference to this Builder
     */
    public Builder age(byte age) {
      this.age = age;
      return this;
    }

    /**
     * Sets the {@code isAlive} and returns a reference to this Builder so that the methods can be
     * chained together.
     *
     * @param isAlive the {@code isAlive} to set
     * @return a reference to this Builder
     */
    public Builder isAlive(boolean isAlive) {
      this.isAlive = isAlive;
      return this;
    }

    /**
     * Returns a {@code User} built from the parameters previously set.
     *
     * @return a {@code User} built with parameters of this {@code User.Builder}
     */
    public User build() {
      return new User(name, surname, age, isAlive);
    }
  }
}
