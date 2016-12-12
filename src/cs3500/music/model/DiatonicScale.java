package cs3500.music.model;

/**
 * Represent a diatonic scale. Provides methods for handling a note with respect to this scale.
 */
public class DiatonicScale extends AScale {
  @Override
  int[] getPitches() {
    int[] pArray = new int[12];
    for (int i = 0; i < 12; i++) {
      pArray[i] = i + 1;
    }
    return pArray;
  }

  /**
   * Return the character representation of a note.
   * @param pitch The pitch to be converted to a string.
   * @return the character of the note.
   */
  public String toString(int pitch) {
    switch (pitch) {
      case 1: return "C";
      case 2: return "C#";
      case 3: return "D";
      case 4: return "D#";
      case 5: return "E";
      case 6: return "F";
      case 7: return "F#";
      case 8: return "G";
      case 9: return "G#";
      case 10: return "A";
      case 11: return "A#";
      case 12: return "B";
      default: throw new IllegalArgumentException("Invalid pitch (" + pitch + "): "
              + "Cannot convert to string.");
    }
  }

  @Override
  void validPitch(int pitch) {
    if (pitch < 1 || pitch > 12) {
      throw new IllegalArgumentException("Invalid pitch: must be >=1 or <=12. "
              + "Was given " + pitch + ".");
    }
  }

  @Override
  void validBaseInterval(int baseInterval) {
    if (baseInterval < 1 || baseInterval > 10) {
      throw new IllegalArgumentException("Invalid base interval: must be >=1 and <=10.");
    }
  }

  //////////////////////////////
  // POTENTIAL FUTURE UPDATES //
  //////////////////////////////

  // Builds the given type of chord at a specified beat.
  // cs3500.music.model.Note[] buildChord(Chord chord, int basePitch, int baseBaseInterval,
  // int duration, int beat)
  /*
  enum Chord {
    MAJOR,
    MINOR,
    M3,
    m3,
  };
  */

  // Add subclasses Major and Minor that can build their respective scales with a specified tonic.
}
