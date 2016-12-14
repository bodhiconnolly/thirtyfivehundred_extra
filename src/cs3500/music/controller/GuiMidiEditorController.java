package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import cs3500.music.model.ANote;
import cs3500.music.model.GoToBeat;
import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.INoteType;
import cs3500.music.view.GuiView;

/**
 * A controller for simultaneous rendering of gui and midi views.
 */
public class GuiMidiEditorController implements IMusicEditorController {
  private IMusicEditorModel model;
  private GuiView view;

  /**
   * Initialises the controller object.
   *
   * @param model the model for the controller.
   * @param view  the view for the controller.
   */
  public GuiMidiEditorController(IMusicEditorModel model, GuiView view) {
    this.model = model;
    this.view = view;
    KeyboardHandler keyboardHandler;
    keyboardHandler = new KeyboardHandler();
    keyboardHandler.addRunnable(KeyEvent.VK_SPACE, () -> togglePlay());
    this.view.keyboardCallback(keyboardHandler);
  }

  @Override
  public void begin() {
    int numBeats = model.length();

    for (int i = 0; i < numBeats; i++) {
      ArrayList<ANote> notes = model.getAtBeat(i);
      for (int j = 0; j < notes.size(); ++j) {
        ANote note = notes.get(j);
        if (note.whichType() == INoteType.NOTE) {
          view.renderNote((note.getPitch() + note.getBaseInterval() * 12), 90,
                  note.getDuration(), note.getInstrument(), note.getBeat());
        }
      }

      ArrayList<Integer> startRepeats = new ArrayList<>();
      ArrayList<Integer> endRepeats = new ArrayList<>();
      ArrayList<ArrayList<Integer>> allRepeats = new ArrayList<>();

      for (GoToBeat repeat : model.getGoToBeats()){
        startRepeats.add(repeat.getGoToBeat());
        endRepeats.add(repeat.getLocation());
      }
      allRepeats.add(startRepeats);
      allRepeats.add(endRepeats);
      this.view.setRepeats(allRepeats);
    }

    // Wait for gui view to fully load
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Tell the view to start playing
    view.renderNote(-1, -1, -1, -1, -1);
  }

  private void togglePlay() {
    view.togglePlaying();
  }
}
