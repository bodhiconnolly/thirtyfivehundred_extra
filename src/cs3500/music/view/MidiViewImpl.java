package cs3500.music.view;


import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

/**
 * Represents a MIDI view.
 * Has a MIDI synthesizer, MIDI receiver, the tempo for playback (in
 * microseconds per beat).
 * Provides two constructors, one that just takes in the desired tempo and another that also takes
 * in a synth (only for testing purposes).
 * Provides a method for rendering a note as a sound (through MIDI.
 * Provides methods for updating and getting the tempo.
 */
public class MidiViewImpl implements IMusicEditorView {
  private Synthesizer synth;
  private Receiver receiver;
  private int tempo;

  /**
   * Build a MIDI view with the given tempo.
   *
   * @param tempo Tempo of the view
   */
  public MidiViewImpl(int tempo) {
    try {
      this.synth = MidiSystem.getSynthesizer();
      this.receiver = synth.getReceiver();
      this.synth.open();
      this.tempo = tempo;
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

  /**
   * Build a MIDI view with given tempo and synth. ONLY for use with testing.
   *
   * @param tempo Tempo of the view
   * @param synth Synth to be used for testing.
   */
  public MidiViewImpl(int tempo, Synthesizer synth) {
    try {
      this.synth = synth;
      this.receiver = synth.getReceiver();
      this.synth.open();
      this.tempo = tempo;
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void renderNote(int rawPitch, int volume, int duration, int instrument, int beatnum) {
    try {
      this.playNote(rawPitch, volume, duration, instrument);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setTempo(int tempo) {
    this.tempo = tempo;
  }

  @Override
  public int getTempo() {
    return this.tempo;
  }

  void playNote(int rawPitch, int velocity, int duration, int instrument)
          throws InvalidMidiDataException {
    MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, instrument - 1, rawPitch, velocity);
    MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, instrument - 1, rawPitch, velocity);
    this.receiver.send(start, this.synth.getMicrosecondPosition());
    this.receiver.send(stop, this.synth.getMicrosecondPosition() + duration * this.tempo);
  }
}
