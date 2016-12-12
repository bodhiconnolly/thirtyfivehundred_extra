package cs3500.music.controller;

import cs3500.music.model.IMusicEditorModel;
import cs3500.music.view.IMusicEditorView;


public class ConsoleController implements IMusicEditorController {
  private IMusicEditorView view;

  public ConsoleController(IMusicEditorModel model, IMusicEditorView view) {
    this.view = view;
  }

  @Override
  public void begin() {
    view.renderNote(0, 0, 0, 0, 0);
  }

}