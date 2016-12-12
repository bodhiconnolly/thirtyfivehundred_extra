package cs3500.music.model;

/**
 * An object that has a location in the music piece and points to another location. Used to
 * set playback from one location to another (such as in repeats).
 */
public class GoToBeat {
  private int location;
  private int goToBeat;

  /**
   * Construct a GoToBeat.
   * @param location Where the GoToBeat should be invoked
   * @param goToBeat Where the GoToBeat leads to
   */
  GoToBeat(int location, int goToBeat) {
    this.location = location;
    this.goToBeat = goToBeat;
  }

  /**
   * Get the location of this GoToBeat.
   * @return
   */
  public int getLocation() {
    return this.location;
  }

  /**
   * Get the goToBeat.
   * @return
   */
  public int getGoToBeat() {
    return this.goToBeat;
  }
}
