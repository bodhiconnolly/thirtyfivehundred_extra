package cs3500.music.view;

import cs3500.music.model.IMusicEditorModel;

/**
 * A class for viewing a song in the console.
 */
public class ConsoleView implements IMusicEditorView {
  private IMusicEditorModel model;

  public ConsoleView(IMusicEditorModel model) {
    this.model = model;
  }

  @Override
  public void renderNote(int rawPitch, int volume, int duration, int instrument, int beatnum) {
    System.out.print(model.toString());
  }

  @Override
  public void setTempo(int tempo) {
    // no tempo for visual view
  }

  @Override
  public int getTempo() {
    return 0;
  }

}
