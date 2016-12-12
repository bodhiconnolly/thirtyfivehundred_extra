package cs3500.music.view;

/**
 * An interface that provides a representation for a piece of music, which can be
 * represented with visuals or audio. It allows for the displaying of notes in a song.
 */
public interface IMusicEditorView {
  /**
   * Render a particular note in the view.
   *
   * @param rawPitch Raw pitch number of note
   * @param volume   volume of note
   * @param duration Duration of note
   */
  void renderNote(int rawPitch, int volume, int duration, int instrument, int beatnum);

  /**
   * Set the tempo of the song.
   * @param tempo the tempo to set.
   */
  void setTempo(int tempo);

  /**
   * Get the tempo of the song.
   * @return the tempo.
   */
  int getTempo();
}
