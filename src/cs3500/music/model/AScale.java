package cs3500.music.model;

/**
 * Represent a scale of pitches. Provides methods for handling a note with respect to this scale.
 */
public abstract class AScale {
  final int[] pitches; // Pitches in scale

  /**
   * Construct a scale.
   */
  AScale() {
    this.pitches = this.getPitches();
  }

  /**
   * Get the pitches in this scale.
   * @return A list of pitches in this scale
   */
  abstract int[] getPitches();

  /**
   * Get a String representing the given pitch int.
   * @param pitch The pitch to be converted to a string
   * @return A string representing the pitch
   */
  public abstract String toString(int pitch);

  /**
   * Checks whether or not a pitch is valid.
   * @param pitch The pitch to be checked
   */
  abstract void validPitch(int pitch);

  /**
   * Checks whether or not a base interval is valid.
   * @param baseInterval The base interval to be checked
   */
  abstract void validBaseInterval(int baseInterval);
}
