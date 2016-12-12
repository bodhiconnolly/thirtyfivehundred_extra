package cs3500.music.util;

import cs3500.music.model.DiatonicScale;
import cs3500.music.model.IMusicEditorModel;
import cs3500.music.model.IMusicEditorModelBuilder;
import cs3500.music.model.MusicEditorType;

/**
 * Represent a composition builder.
 */
public class CompositionBuilderImpl implements CompositionBuilder<IMusicEditorModel> {
  IMusicEditorModel model;

  public CompositionBuilderImpl() {
    this.model = IMusicEditorModelBuilder.build(MusicEditorType.TRACK, new DiatonicScale(), 4);
  }

  @Override
  public IMusicEditorModel build() {
    return this.model;
  }

  // TODO: Return more than just this?
  @Override
  public CompositionBuilder<IMusicEditorModel> setTempo(int tempo) {
    this.model.setTempo(tempo);
    return this;
  }

  // TODO: Support instrument and volume?
  // TODO: Return more than just this?
  @Override
  public CompositionBuilder<IMusicEditorModel> addNote(int start, int end, int instrument,
                                                       int pitch, int volume) {
    int pitchWithinBaseInterval = pitch % 12;
    if (pitchWithinBaseInterval == 0) {
      pitchWithinBaseInterval = 12;
    }
    int baseInterval = ((pitch - pitchWithinBaseInterval) / 12) + 1;
    // + 1 to account for the fact that minumum note duration is 1
    int duration = (end - start) + 1;
    this.model.addNote(pitchWithinBaseInterval, baseInterval, start, duration, instrument);
    return this;
  }
}
