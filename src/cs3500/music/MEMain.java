package cs3500.music;

import java.io.FileNotFoundException;
import java.util.Scanner;

import cs3500.music.controller.ConsoleController;
import cs3500.music.controller.GuiMidiEditorController;
import cs3500.music.controller.GuiMusicController;
import cs3500.music.controller.IMusicEditorController;
import cs3500.music.controller.MidiMusicEditorController;
import cs3500.music.model.DiatonicScale;
import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.IMusicEditorModelBuilder;
import cs3500.music.model.MusicEditorType;
import cs3500.music.view.CompositeView;
import cs3500.music.view.ConsoleView;
import cs3500.music.view.GuiView;
import cs3500.music.view.GuiViewFrame;
import cs3500.music.view.IMusicEditorView;
import cs3500.music.view.MidiViewImpl;

/**
 * Represent the main that runs the program.
 */
public class MEMain {

  /**
   * Represent the main method for running the program.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    try {
      String songToPlay;
      String viewType;
      try {
        songToPlay = args[0];
        viewType = args[1];
      } catch (ArrayIndexOutOfBoundsException e) {
        System.out.print("No command line args given.");
        return;
      }

      // Build model
      IMusicEditorModel model = IMusicEditorModelBuilder.build(MusicEditorType.TRACK,
              new DiatonicScale(), 4).fromFile(songToPlay);

      IMusicEditorView view;
      GuiView view2;
      IMusicEditorController controller;
      switch (viewType) {
        case "midi":
          view = new MidiViewImpl(model.getTempo());
          controller = new MidiMusicEditorController(model, view);
          break;
        case "visual":
          view2 = new GuiViewFrame(
                  model.getHighestNote(), model.getLowestNote(), model.length(), true);
          controller = new GuiMusicController(model, view2);
          break;
        case "console":
          view = new ConsoleView(model);
          controller = new ConsoleController(model, view);
          break;
        case "guimidi":
          view2 = new CompositeView(new GuiViewFrame(
                  model.getHighestNote(), model.getLowestNote(), model.length(), false),
                  new MidiViewImpl(model.getTempo()));
          controller = new GuiMidiEditorController(model, view2);
          break;
        default:
          throw new IllegalArgumentException("Invalid view type given: Must be \"midi\", "
                  + "\"visual\", or \"console\".");
      }

      // Gooooooooo
      controller.begin();
    } catch (FileNotFoundException e) {
      System.out.print("Given file not found.");
    }
  }

  static void print(String s) {
    System.out.print(s);
  }
}
