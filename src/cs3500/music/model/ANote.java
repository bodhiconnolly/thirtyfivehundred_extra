package cs3500.music.model;

/**
 * Represent a note space. Provides methods to get information (such as pitch, beat, duration, etc.)
 * about the note and to edit the note.
 * An ANote can be a Note, Sustain, or Rest.
 */
public abstract class ANote {
  /**
   * Gets the pitch of the given cs3500.music.model.ANote, if there is one.
   * Throws exception if there is none.
   * @return Pitch of given cs3500.music.model.ANote
   */
  public abstract int getPitch();

  /**
   * Gets the base interval of the given cs3500.music.model.ANote, if there is one.
   * Throws exception if there is none.
   * @return Base interval of given cs3500.music.model.ANote
   */
  public abstract int getBaseInterval();

  /**
   * Gets the beat of the given cs3500.music.model.ANote, if there is one.
   * Throws exception if there is none.
   * @return Beat of given cs3500.music.model.ANote
   */
  public abstract int getBeat();

  /**
   * Get duration of the given cs3500.music.model.ANote.
   * @return the duration.
   */
  public abstract int getDuration();

  /**
   * Changes a note's pitch to given pitch. Ensures pitch remains valid.
   * @param newPitch The new pitch
   * @param scale    The scale of this note
   */
  abstract void changePitch(int newPitch, AScale scale);

  /**
   * Change a note's base interval to the given base interval. Ensures base interval remains valid.
   * @param newBaseInterval The new base interval
   * @param scale           The scale of this note
   */
  abstract void changeBaseInterval(int newBaseInterval, AScale scale);

  /**
   * Changes a note's starting beat to given beat. Ensures beat remains valid.
   * @param newBeat The new beat
   * @param scale   The scale of this note
   */
  abstract void changeBeat(int newBeat, AScale scale);

  /**
   * Changes a note's duration to given duration. Ensure duration remains valid.
   * @param newDuration The new duration
   * @param scale       The scale of this note
   */
  abstract void changeDuration(int newDuration, AScale scale);

  /**
   * Converts a note to a string in a more conventional format (ex. A#2).
   * @param scale The scale the note is in
   * @return A string representing a note
   */
  abstract String toString(AScale scale);

  /**
   * Figure out what type of cs3500.music.model.ANote this cs3500.music.model.ANote is.
   * @return The type of the cs3500.music.model.ANote
   */
  public abstract INoteType whichType();

  /**
   * Get's the raw pitch number (1-127) of the given note.
   * @param scale Scale this note is in
   * @return Raw pitch number
   */
  abstract int getRawPitchNumber(AScale scale);

  /**
   * Get this notes intsrument.
   * @return This note's instrument
   */
  public abstract int getInstrument();
}