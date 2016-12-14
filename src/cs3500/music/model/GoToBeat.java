package cs3500.music.model;

/**
 * An object that has a location in the music piece and points to another location. Used to
 * set playback from one location to another (such as in repeats).
 */
public class GoToBeat {
  private int location;
  private int goToBeat;
  // The ending number this GoToBeat represents (will be 0 if it is for a repeat)
  private int endingNum;


  /**
   * Construct a GoToBeat.
   * @param location Where the GoToBeat should be invoked
   * @param goToBeat Where the GoToBeat leads to
   */
  GoToBeat(int location, int goToBeat, int endingNum) {
    this.location = location;
    this.goToBeat = goToBeat;
    this.endingNum = endingNum;
  }

  /**
   * Get the location of this GoToBeat.
   * @return the location
   */
  public int getLocation() {
    return this.location;
  }

  /**
   * Get the goToBeat.
   * @return the goToBeat number
   */
  public int getGoToBeat() {
    return this.goToBeat;
  }

  /**
   * Get the ending number.
   * @return The ending number
   */
  public int getEndingNum() {
    return this.endingNum;
  }
}
