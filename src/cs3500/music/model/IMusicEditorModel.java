package cs3500.music.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Represent the model for a music editor.
 * Models a song as a collection of notes (containing pitch, base interval (ex. octave),
 * and duration) which begin at specific beats (time steps). Provides methods for adding, accessing,
 * and editing notes. Additionally, provides methods for getting information about the track, such
 * as the highest note, lowest note, and tempo (which can be edited).
 */
public interface IMusicEditorModel {
  /**
   * Adds a note.
   * @param pitch        Pitch of note to be added
   * @param baseInterval Base interval of note to be added
   * @param beat         Beat of note to be added
   * @param duration     Duration of note to be added
   */
  void addNote(int pitch, int baseInterval, int beat, int duration, int instrument);

  /**
   * Change pitch and/or interval of the note at given pitch, interval, and beat.
   * @param newPitch        New pitch of note
   * @param newBaseInterval New base interval of note
   * @param curPitch        Old pitch of note
   * @param curBaseInterval Old base interval of note
   * @param curBeat         Starting beat of note
   */
  void editPitchBaseInterval(int newPitch, int newBaseInterval,
                         int curPitch, int curBaseInterval, int curBeat, int instrument);

  /**
   * Change starting beat of the note at given pitch, interval, and beat.
   * @param newBeat         New beat of note
   * @param curPitch        Pitch of note
   * @param curBaseInterval Base interval of note
   * @param curBeat         Old beat of note
   */
  void editStartBeat(int newBeat,
                     int curPitch, int curBaseInterval, int curBeat, int instrument);

  /**
   * Change duration of the note at given pitch, interval, and beat.
   * @param newDuration     New duration of note
   * @param curPitch        Pitch of note
   * @param curBaseInterval Base interval of note
   * @param curBeat         Beat of note
   */
  void editDuration(int newDuration,
                    int curPitch, int curBaseInterval, int curBeat, int instrument);

  /**
   * Deletes a note.
   * @param pitch        Pitch of note
   * @param baseInterval Base interval of note
   * @param beat         Beat of note
   */
  void deleteNote(int pitch, int baseInterval, int beat, int instrument);

  /**
   * Return list of all notes that start at a beat.
   * @param beatNumber Beat to search for
   * @return List of all notes that start at beat
   */
  ArrayList<ANote> getAtBeat(int beatNumber);

  /**
   * Set the tempo of this piece.
   * @param tempo The tempo
   */
  void setTempo(int tempo);

  /**
   * Get the tempo of this piece.
   */
  int getTempo();

  /**
   * Returns the length of this piece of cs3500.music.music.
   * @return Length of cs3500.music.music piece
   */
  int length();

  /**
   * Get highest note in this editor.
   * @return Highest note
   */
  int getHighestNote();

  /**
   * Get lowest note in this editor.
   * @return The lowest note
   */
  int getLowestNote();

  /**
   * Retruns a model loaded from the given file name.
   * @param fileName Name of the file to load from
   * @return Model with song from file
   */
  IMusicEditorModel fromFile(String fileName) throws FileNotFoundException;

  /**
   * Add a repeat to the track.
   * @param activationBeat the beat the repeat is activated at
   * @param goBackToBeat the beat the repeats points to
   */
  void addRepeat(int activationBeat, int goBackToBeat);

  /**
   * Add an alternate ending.
   * @param startEnd1 Start beat of the first ending.
   * @param startEnd2 Start beat of the second ending.
   */
  void addAltEnd(int startEnd1, int startEnd2);

  /**
   * Get GoToBeats for this track.
   * @return GoToBeats
   */
  ArrayList<GoToBeat> getGoToBeats();

  //////////////////////////////
  // POTENTIAL FUTURE UPDATES //
  //////////////////////////////

  // Loop a segment of a track
  // void loop(int startBeat, int endBeat);

  // Change pitch of all notes by a certain amount
  // void transpose(int modifier);

  // Edit volume of a note
  // void editVolume(int volume, int pitch, int baseInterval, int beat);

  // Edit dynamics at a particular beat
  // void editDynamics(int dynamics, int beat);
}

