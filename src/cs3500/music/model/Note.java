package cs3500.music.model;

import java.util.ArrayList;

/**
 * Represent a note that is hit. Provides methods to get information (such as pitch,
 * beat, duration, etc.) about the note and to edit the note.
 */
class Note extends ANote {
  int pitch; // Pitch
  int baseInterval; // Base interval of note (In a diatonic scale, this would be an octave)
  int beat; // Beat number
  int instrument; // Instrument
  ArrayList<Sustain> sustains; // Sustains corresponding to this note

  /**
   * Construct a note. Validation is not handled here because a note does not know its scale.
   *
   * @param pitch         Pitch of note
   * @param baseInterval  Base interval of note
   * @param beat          Beat number
   */
  Note(int pitch, int baseInterval, int beat, int duration, int instrument) {
    this.pitch = pitch;
    this.beat = beat;
    this.baseInterval = baseInterval;
    sustains = new ArrayList<Sustain>();
    // Add a number of sustains equal to the duration of the note
    // All notes have at least one sustain to indicate that they are played
    for (int i = 0; i < duration; i++) {
      sustains.add(new Sustain());
    }
    this.instrument = instrument;
  }

  /**
   * Check wether or not a note is valid.
   *
   * @param scale  The scale the note is in
   * @throws IllegalArgumentException If note is invalid
   */
  void validNote(AScale scale) throws IllegalArgumentException {
    // Validate the pitch
    scale.validPitch(this.pitch);

    // Validate the base interval
    scale.validBaseInterval(baseInterval);

    // Validate beat number
    if (this.beat < 0) {
      throw new IllegalArgumentException("Invalid beat number, must be >=0.");
    }

    // Validate duration
    if (this.sustains.size() < 1) {
      throw new IllegalArgumentException("Invalid duration/sustains: Must be >0");
    }
  }

  /**
   * Changes a note's pitch to given pitch. Ensures pitch remains valid.
   * @param newPitch The new pitch
   * @param scale    The scale of this note
   */
  void changePitch(int newPitch, AScale scale) {
    this.pitch = newPitch;
    this.validNote(scale);
  }

  /**
   * Change a note's base interval to the given base interval. Ensures base interval remains valid.
   * @param newBaseInterval The new base interval
   * @param scale           The scale of this note
   */
  void changeBaseInterval(int newBaseInterval, AScale scale) {
    this.baseInterval = newBaseInterval;
    this.validNote(scale);
  }

  /**
   * Changes a note's starting beat to given beat. Ensures beat remains valid.
   * @param newBeat The new beat
   * @param scale   The scale of this note
   */
  void changeBeat(int newBeat, AScale scale) {
    this.beat = newBeat;
    this.validNote(scale);
  }

  /**
   * Changes a note's duration to given duration. Ensure duration remains valid.
   * @param newDuration The new duration
   * @param scale       The scale of this note
   */
  void changeDuration(int newDuration, AScale scale) {
    sustains = new ArrayList<Sustain>();
    // Add a number of sustains equal to the duration of the note
    // All notes have at least one sustain to indicate that they are played
    for (int i = 0; i < newDuration; i++) {
      sustains.add(new Sustain());
    }
    this.validNote(scale);
  }

  /**
   * Gets the raw pitch number.
   * Raw pitch number = pitch number + ((base interval - 1) * pitches in one base interval)
   * @param scale The scale being used
   * @return The raw pitch number
   */
  int getRawPitchNumber(AScale scale) {
    return this.pitch + ((this.baseInterval - 1) * scale.pitches.length);
  }

  @Override
  public String toString() {
    return "  X  ";
  }

  /**
   * Gets a string for this note.
   * @param scale Scale the note is in
   * @return A string representing the this note
   */
  public String toString(AScale scale) {
    // Get string for note
    String noteString = scale.toString(this.pitch) + baseInterval;
    // Center note in 5 character long string depending on note length
    if (noteString.length() == 2) {
      return "  " + noteString + " ";
    }
    else if (noteString.length() == 3) {
      return " " + noteString + " ";
    }
    else if (noteString.length() == 4) {
      return " " + noteString;
    }
    else {
      return noteString;
    }
  }

  @Override
  public INoteType whichType() {
    return INoteType.NOTE;
  }

  @Override
  public int getPitch() {
    return this.pitch;
  }

  @Override
  public int getBaseInterval() {
    return this.baseInterval;
  }

  @Override
  public int getBeat() {
    return this.beat;
  }

  @Override
  public int getDuration() {
    return sustains.size();
  }

  @Override
  public int getInstrument() {
    return this.instrument;
  }
}