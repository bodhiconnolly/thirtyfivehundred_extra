package cs3500.music.view;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import cs3500.music.controller.KeyboardHandler;

/**
 * Represents a composite music editor view that includes both a GUI view and a MIDI view.
 * Given a GUI and MIDI view when constructed. Add a note by using renderNote. This adds the note
 * to the view and renders it in the GUI. When renderNote is called with all arguments set to -1,
 * the notes will be rendered in MIDI. This is done (instead of splitting it into two methods)
 * so that the original super interface, IMusicEditorView, does not need to be changed to support
 * this implementation.
 */
public class CompositeView implements GuiView {
  private GuiView guiView;
  private MidiViewImpl midiView;
  private ArrayList<ArrayList<Note>> notes;
  private int endOfSong;
  private int curBeat; // Current position in playback of song
  private boolean playing; // Whether or not song is playing

  /**
   * Initialize view class with two view classes.
   *
   * @param guiView  the visual class.
   * @param midiView the midi class.
   */
  public CompositeView(GuiView guiView, MidiViewImpl midiView) {
    this.guiView = guiView;
    this.midiView = midiView;
    this.notes = new ArrayList<>();
    notes.add(new ArrayList<>());
    this.endOfSong = 0;
    this.curBeat = 0;
    this.playing = false;
  }

  @Override
  public void renderNote(int rawPitch, int volume, int duration, int instrument, int beatnum) {
    if (rawPitch == -1 && volume == -1 && duration == -1 && instrument == -1 && beatnum == -1) {
      // Render the notes in the composite view as midi sounds. Advance the red line in the gui
      // view for each beat.
      this.playing = true;

      // Play the song starting from the current beat
      for (int i = curBeat; i < notes.size(); i++) {

        // If the song is not playing, sleep for a tenth of a second and check it it is playing.
        // Repeat indefinitely until the song is found to be playing. This is the pause/play
        // feature.
        while (!playing) {
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

        for (int j = 0; j < notes.get(i).size(); j++) {
          Note n = notes.get(i).get(j);
          this.midiView.renderNote(n.rawPitch, n.volume, n.duration, n.instrument, n.beatnum);
        }
        setBeat(curBeat);
        curBeat = curBeat + 1;
        try {
          Thread.sleep(this.getTempo() / 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      // Keep advancing the red line on the gui view
      while (curBeat <= endOfSong) {
        setBeat(curBeat);
        curBeat = curBeat + 1;
        try {
          Thread.sleep(this.getTempo() / 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

    } else {
      // Update end of song if necessary
      if (endOfSong < beatnum + duration) {
        endOfSong = beatnum + duration;
      }
      int lowestNote = guiView.getLowestNote();
      guiView.renderNote(rawPitch - lowestNote - 12, 1, duration, instrument, beatnum);
      int beatsToAdd = (beatnum + 1) - notes.size();
      for (int i = 0; i < beatsToAdd; i++) {
        notes.add(new ArrayList<>());
      }
      notes.get(beatnum).add(new Note(rawPitch, volume, duration, instrument, beatnum));
    }
  }

  @Override
  public void setTempo(int tempo) {
    midiView.setTempo(tempo);
  }

  @Override
  public int getTempo() {
    return midiView.getTempo();
  }

  @Override
  public void keyboardCallback(KeyboardHandler handler) {
    guiView.keyboardCallback(handler);
  }

  @Override
  public void setBeat(int beat) {
    this.guiView.setBeat(beat);
  }

  @Override
  public void scroll(int toScroll) {
    // Scroll is taken care of automatically here.
    throw new InvalidParameterException("Scroll is automatic in this view");
  }
  
  @Override
  public void jumpToStart(){
    // Scroll is taken care of automatically here.
    throw new InvalidParameterException("Scroll is automatic in this view");
  }
  
  @Override
  public void jumpToEnd(){
    // Scroll is taken care of automatically here.
    throw new InvalidParameterException("Scroll is automatic in this view");
	}

  @Override
  public int getLowestNote() {
    return 0;
  }

  @Override
  public String getTextInput() {
    return null;
  }

  @Override
  public void update(boolean noteChange) {
    // Scroll is taken care of automatically here.
    throw new InvalidParameterException("Update is automatic in this view");
  }

  @Override
  public void togglePlaying() {
    this.playing = !this.playing;
  }

  /**
   * Representation of a note for use in the composite view. Doesn't use the same represenation
   * as in the model to keep them decoupled. Notes could have been represented as a list of ints
   * or as a series of lists of ints, but this seems more comprehensible. Created it as a private
   * inner class because it doesn't need to be used nor should it be used anywhere other than in
   * the CompositeView class.
   */
  private class Note {
    int rawPitch;
    int volume;
    int duration;
    int instrument;
    int beatnum;

    Note(int rawPitch, int volume, int duration, int instrument, int beatnum) {
      this.rawPitch = rawPitch;
      this.volume = volume;
      this.duration = duration;
      this.instrument = instrument;
      this.beatnum = beatnum;
    }
  }
}
