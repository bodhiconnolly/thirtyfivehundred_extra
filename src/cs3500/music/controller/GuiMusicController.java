package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import cs3500.music.model.ANote;
import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.INoteType;
import cs3500.music.view.GuiView;

/**
 * A controller object specifically for rendering a music model in a GUI.
 */
public class GuiMusicController implements IMusicEditorController {
  private IMusicEditorModel model;
  private GuiView view;

  /**
   * Initialises a Gui Controller object.
   *
   * @param model the model for the controller.
   * @param view  the view for the controller.
   */
  public GuiMusicController(IMusicEditorModel model, GuiView view) {
    this.model = model;
    this.view = view;

    KeyboardHandler keyboardHandler;
    keyboardHandler = new KeyboardHandler();
    keyboardHandler.addRunnable(KeyEvent.VK_ENTER, () -> actOnString(view.getTextInput()));
    keyboardHandler.addRunnable(KeyEvent.VK_LEFT, () -> view.scroll(-50));
    keyboardHandler.addRunnable(KeyEvent.VK_RIGHT, () -> view.scroll(50));
    this.view.keyboardCallback(keyboardHandler);


  }

  @Override
  public void begin() {

    // Uncomment the following line to render the music editor in the model
    //System.out.print(model.toString());

    renderNotes();
  }

  private void actOnString(String commandIn) {
    //System.out.print("act on string: " + commandIn);
    try {
      String[] data = commandIn.split(",");
      switch (data[0]) {
        case "a":
          addNote(Integer.parseInt(data[1]),
                  Integer.parseInt(data[2]), Integer.parseInt(data[3]),
                  Integer.parseInt(data[4]));
          break;
        case "d":
          removeNote(Integer.parseInt(data[1]),
                  Integer.parseInt(data[2]), Integer.parseInt(data[3]),
                  Integer.parseInt(data[4]));
          break;
        default:
          System.out.print("Not valid command");
          break;
      }
    } catch (Exception e) {
      System.err.println("Invalid command: " + e.getMessage());

    }
    view.update(true);
    renderNotes();
    view.update(false);


  }

  private void renderNotes() {
    int numBeats = model.length();

    for (int i = 0; i < numBeats; ++i) {
      ArrayList<ANote> notes = model.getAtBeat(i);
      for (int j = 0; j < notes.size(); ++j) {
        ANote note = notes.get(j);
        if (note.whichType() == INoteType.NOTE) {
          view.renderNote(j, 1, note.getDuration(), note.getInstrument(), i);
        }
      }
    }
  }

  private void addNote(int pitch, int baseInterval, int beat, int duration) {
    //System.out.print(pitch);
    //System.out.print(baseInterval);
    //System.out.print(beat);
    //System.out.print(duration);
    model.addNote(pitch - 1, baseInterval + 2, beat, duration, 1);
  }

  private void removeNote(int pitch, int baseInterval, int beat, int duration) {
    System.out.print(pitch);
    System.out.print(baseInterval);
    System.out.print(beat);
    System.out.print(duration);
  }

}
