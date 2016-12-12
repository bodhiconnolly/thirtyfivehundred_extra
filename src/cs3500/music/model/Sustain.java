package cs3500.music.model;

/**
 * Represent a sustain.
 * Represented as a class in case it becomes necessary to add properties
 * to the sustains, such as how fast they diminish.
 */
class Sustain extends ANote {
  /**
   * Construct a sustain.
   */
  Sustain() {
    // Empty because a sustain doesn't have any fields.
  }

  @Override
  public int getPitch() {
    throw new IllegalArgumentException("Invalid ANote given: A sustain does not have a pitch.");
  }

  @Override
  public int getBaseInterval() {
    throw new IllegalArgumentException("Invalid ANote given: "
            + "A sustain does not have a base interval.");
  }

  @Override
  public int getBeat() {
    throw new IllegalArgumentException("Invalid ANote given: A sustain does not have a beat.");
  }

  @Override
  public int getDuration() {
    return 1;
  }

  @Override
  void changePitch(int newPitch, AScale scale) {
    throw new IllegalArgumentException("Invalid ANote given: A sustain does not have a pitch.");
  }

  @Override
  void changeBaseInterval(int newBaseInterval, AScale scale) {
    throw new IllegalArgumentException("Invalid ANote given: "
            + "A sustain does not have a base interval.");
  }

  @Override
  void changeBeat(int newBeat, AScale scale) {
    throw new IllegalArgumentException("Invalid ANote given: A sustain does not have a beat.");
  }

  @Override
  void changeDuration(int newDuration, AScale scale) {
    throw new IllegalArgumentException("Invalid ANote given: "
            + "A sustain's duration cannot be changed.");
  }

  @Override
  public INoteType whichType() {
    return INoteType.SUSTAIN;
  }

  @Override
  public int getRawPitchNumber(AScale scale) {
    throw new IllegalArgumentException("Invalid ANote given: A rest does not have a raw pitch"
            + "number.");
  }

  @Override
  public int getInstrument() {
    throw new IllegalArgumentException("Invalid ANote given: A sustain does not have an "
            + "instrument");
  }

  public String toString(AScale scale) {
    throw new IllegalArgumentException("Invalid ANote given: A sustain cannot be converted to a "
            + "conventional form string.");
  }

  public String toString() {
    return "  |  ";
  }
}
