package cs3500.music.model;

/**
 * Class for building a IMusicEditorModel. Provides one static method for building.
 */
public class IMusicEditorModelBuilder {
  /**
   * A class for building a music model.
   * @param met the type of music model.
   * @param scale the scale of the model.
   * @param measures the size of the model.
   * @return the model.
   */
  public static IMusicEditorModel build(MusicEditorType met, AScale scale, int measures) {
    if (met == MusicEditorType.TRACK) {
      return new Track(scale, measures);
    } else {
      throw new IllegalArgumentException("Invalid cs3500.music.model type.");
    }
  }
}
