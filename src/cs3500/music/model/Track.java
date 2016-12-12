package cs3500.music.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import cs3500.music.util.CompositionBuilder;
import cs3500.music.util.CompositionBuilderImpl;
import cs3500.music.util.MusicReader;

// TODO: Support overlapping.

/**
 * Represent a track.
 * Models a song as a collection of notes (containing pitch, base interval (ex. octave),
 * and duration) which begin at specific beats (time steps). Provides methods for adding, accessing,
 * and editing notes. Additionally, provides methods for getting information about the track, such
 * as the highest note, lowest note, and tempo (which can be edited).
 * Columns represent all notes at a beat, rows represent all notes
 * of a particular pitch/base interval.
 */
public class Track implements IMusicEditorModel {
  ArrayList<ArrayList<ANote>> track;
  AScale scale;
  int beatsPerMeasure;
  String noteHeaderString;
  // Invariant: The following three variables are always true (they are set in the constructor and
  // ensured/updated by all that can change them.
  int highestNote;
  int lowestNote;
  int highestBeat;
  int tempo;
  // GoToBeats that enable repeats and multiple endings
  // Invariant: This list is always sorted by GoToBeat location from low to high.
  private ArrayList<GoToBeat> listGoToBeats;

  /**
   * Construct a new track with a given measure length in the given scale.
   */
  public Track(AScale scale, int beatsPerMeasure) {
    this.scale = scale;
    this.beatsPerMeasure = beatsPerMeasure;
    this.track = new ArrayList<ArrayList<ANote>>();
    noteHeaderString = "";
    this.highestNote = 0;  // 0 indicates there is no highest note yet.
    this.lowestNote = 0;   // 0 indicates there is no lowest note yet.
    this.highestBeat = -1; // -1 indicates there is no highest beat yet.
    this.listGoToBeats = new ArrayList<GoToBeat>();
  }

  private static ArrayList<ANote> removeRests(ArrayList<ANote> notes) {
    ArrayList<ANote> notes2 = new ArrayList<ANote>();
    for (int i = 0; i < notes.size(); i++) {
      if (notes.get(i).whichType() == INoteType.NOTE) {
        notes2.add(notes.get(i));
      }
    }
    return notes2;
  }

  @Override
  public void addNote(int pitch, int baseInterval, int beat, int duration, int instrument) {
    Note newNote = new Note(pitch, baseInterval, beat, duration, instrument);
    newNote.validNote(this.scale);
    this.expandTrack(pitch, baseInterval, beat, duration);
    // Now, track is guaranteed to have space for note
    this.track.get(newNote.getBeat()).set((newNote.getRawPitchNumber(this.scale) - 1)
            - (this.lowestNote - 1), newNote);
  }

  @Override
  public void editPitchBaseInterval(int newPitch, int newBaseInterval, int curPitch,
                                    int curBaseInterval, int curBeat, int instrument) {
    // Get raw pitch number
    int curRawPitchNumber = new Note(curPitch, curBaseInterval, curBeat, 1,
            instrument).getRawPitchNumber(this.scale);
    // Get note to be edited
    ANote curNote = this.track.get(curBeat).get((curRawPitchNumber - 1) - (this.lowestNote - 1));
    // Don't allow editing if cs3500.music.model.ANote isn't a cs3500.music.model.Note
    if (curNote.whichType() != INoteType.NOTE) {
      throw new IllegalArgumentException("Can't edit pitch/base interval of a Note ANote.");
    }
    // Place a rest at the old location
    this.track.get(curBeat).set((curRawPitchNumber - 1) - (this.lowestNote - 1), new Rest());
    // Place cs3500.music.model.Note in its new location
    this.addNote(newPitch, newBaseInterval, curNote.getBeat(), curNote.getDuration(), instrument);
  }

  @Override
  public void editStartBeat(int newBeat, int curPitch, int curBaseInterval, int curBeat,
                            int instrument) {
    // Get raw pitch number
    int curRawPitchNumber = new Note(curPitch, curBaseInterval, curBeat, 1,
            instrument).getRawPitchNumber(this.scale);
    // Get note to be edited
    ANote curNote = this.track.get(curBeat).get((curRawPitchNumber - 1) - (this.lowestNote - 1));
    // Don't allow editing if cs3500.music.model.ANote isn't a cs3500.music.model.Note
    if (curNote.whichType() != INoteType.NOTE) {
      throw new IllegalArgumentException("Can't edit starting beat of non-Note ANote.");
    }
    // Place a rest at the old location
    this.track.get(curBeat).set((curRawPitchNumber - 1) - (this.lowestNote - 1), new Rest());
    // Place cs3500.music.model.Note in its new location
    this.addNote(curPitch, curBaseInterval, newBeat, curNote.getDuration(), instrument);
  }

  @Override
  public void editDuration(int newDuration, int curPitch, int curBaseInterval, int curBeat,
                           int instrument) {
    // Get raw pitch number
    int curRawPitchNumber = new Note(curPitch, curBaseInterval, curBeat, 1,
            instrument).getRawPitchNumber(this.scale);
    // Get note to be edited
    ANote curNote = this.track.get(curBeat).get((curRawPitchNumber - 1) - (this.lowestNote - 1));
    // Don't allow editing if cs3500.music.model.ANote isn't a cs3500.music.model.Note
    if (curNote.whichType() != INoteType.NOTE) {
      throw new IllegalArgumentException("Can't edit starting beat of non-Note ANote.");
    }
    // Place cs3500.music.model.Note in its new location
    this.addNote(curPitch, curBaseInterval, curBeat, newDuration, instrument);
  }

  @Override
  public void deleteNote(int pitch, int baseInterval, int beat, int instrument) {
    // Get raw pitch number
    int curRawPitchNumber = new Note(pitch, baseInterval, beat, 1,
            instrument).getRawPitchNumber(this.scale);
    // Check if it is a note
    if (this.track.get(beat).get((curRawPitchNumber - 1) - (this.lowestNote - 1)).whichType()
            != INoteType.NOTE) {
      throw new IllegalArgumentException("Only Notes can be deleted - Attempted to delete some"
              + " other kind of ANote.");
    }

    // Replace note with a rest
    this.track.get(beat).set((curRawPitchNumber - 1) - (this.lowestNote - 1), new Rest());

    this.contractTrack();
  }

  private void expandTrack(int pitch, int baseInterval, int beat, int duration) {
    int rawPitchNumber = pitch
            + (baseInterval * this.scale.pitches.length)
            - this.scale.pitches.length;

    // Handle first frame resizing
    if (this.highestNote == 0 && this.lowestNote == 0 && this.highestBeat == -1) {
      this.highestNote = rawPitchNumber;
      this.lowestNote = rawPitchNumber;
      this.highestBeat = 0;
      track.add(new ArrayList<ANote>());
      track.get(0).add(new Rest());
    }

    // Resize up
    // Confirm there are enough beats in track. Resize if necessary.
    int endOfNote = beat + duration;
    int beatDifference = endOfNote - this.highestBeat;
    if (beatDifference > 0) {
      for (int i = 0; i < beatDifference; i++) {
        this.track.add(new ArrayList<ANote>());
        int noteRange = this.track.get(0).size();
        for (int j = 0; j < noteRange; j++) {
          this.track.get(this.track.size() - 1).add(new Rest());
        }
      }
      this.highestBeat = endOfNote; // Update highest beat
    }
    // Confirm the pitch/note is within track's range. Resize if necessary.
    int notesRawPitchNumber = rawPitchNumber;
    if (notesRawPitchNumber < this.lowestNote) {
      for (int i = 0; i < (this.lowestNote - notesRawPitchNumber); i++) {
        for (ArrayList<ANote> aln : track) {
          aln.add(0, new Rest());
        }
      }
      this.lowestNote = notesRawPitchNumber; // Update lowest note
    } else if (notesRawPitchNumber > this.highestNote) {
      for (int i = 0; i < (notesRawPitchNumber - this.highestNote); i++) {
        for (ArrayList<ANote> aln : track) {
          aln.add(new Rest());
        }
      }
      this.highestNote = notesRawPitchNumber;
    }
  }

  private void contractTrack() {

    // Resize down
    // Remove empty beats
    for (int i = 0; i < track.size(); i++) {
      for (ANote a : track.get(i)) {
        if (a.whichType() == INoteType.NOTE) {
          this.highestBeat = i + (a.getDuration() + 2);
          break;
        }
      }
    }
    for (int i = (this.highestBeat); i < track.size(); i++) {
      track.remove(highestBeat);
    }
    // Remove empty pitch/base intervals above highest filled pitch/base interval
    // Remove empty pitch/base intervals below lowest filled pitch/base interval
    // Reset highest beat, etc.
  }

  @Override
  public String toString() {
    String trackString = "";
    int highestBeatDigits = Integer.toString(this.highestBeat).length();

    // Add spaces in row 1, column 1
    for (int i = 0; i < highestBeatDigits; i++) {
      trackString += " ";
    }

    // Update the note header
    this.updateNoteHeaderString();

    trackString += noteHeaderString;

    int beat = 0;
    // Loop through beats
    int curIdx = 0; // Current index (only used for one part, so for loop is easier)
    for (ArrayList<ANote> alin : this.track) {
      // Add preceding spaces
      int curBeatDigits = Integer.toString(curIdx).length();
      int precedingSpacesNeeded = highestBeatDigits - curBeatDigits;
      for (int i = 0; i < precedingSpacesNeeded; i++) {
        trackString += " ";
      }
      curIdx += 1; // Advance current index

      trackString = trackString + beat; // Add beat number
      // Add cs3500.music.model.ANote strings
      for (ANote n : alin) {
        trackString = trackString + n.toString();
        for (int i = 1; i < n.getDuration(); i++) {
          ArrayList<ANote> currentBeat = this.track.get(n.getBeat() + i);
          if (currentBeat.get(n.getRawPitchNumber(this.scale) - 1
                  - (this.lowestNote - 1)).whichType()
                  != INoteType.NOTE) { // Don't erase Notes with Sustains.
            this.track.get(n.getBeat() + i).set((n.getRawPitchNumber(this.scale) - 1
                            - (this.lowestNote - 1)),
                    new Sustain());
          }
        }
      }
      beat = beat + 1; // Advance beat number
      trackString = trackString + "\n"; // Add a line break
    }

    // Clear all sustains from track
    for (int i = 0; i < this.track.size(); i++) {
      for (int j = 0; j < this.track.get(i).size(); j++) {
        if (this.track.get(i).get(j).whichType() == INoteType.SUSTAIN) {
          this.track.get(i).set(j, new Rest());
        }
      }
    }

    return trackString;
  }

  /**
   * Gets the String of notes that is used for the console output header.
   */
  private void updateNoteHeaderString() {
    //this.setLowNote(notes);
    //this.setHighNote(notes);
    int spread = this.highestNote - this.lowestNote;
    int lowPitch = this.lowestNote % 12;
    if (lowPitch == 0) {
      lowPitch = 12;
    }
    int lowBaseInterval = ((this.lowestNote - lowPitch) / 12) + 1;
    Note note1 = new Note(lowPitch, lowBaseInterval, 1, 1, 0);
    for (int i = 0; i <= spread; i++) {
      noteHeaderString += note1.toString(this.scale);
      if (note1.pitch < 12) {
        note1.pitch = note1.pitch + 1;
      } else {
        note1.pitch = 1;
        note1.baseInterval = note1.baseInterval + 1;
      }
    }
    noteHeaderString += "\n";
  }

  @Override
  public ArrayList<ANote> getAtBeat(int beatNumber) {
    return this.track.get(beatNumber);
    /*
    try {
      return removeRests(this.track.get(beatNumber));
    }
    catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException("Invalid beat number: " + beatNumber);
    }
    */
  }

  @Override
  public int getTempo() {
    return this.tempo;
  }

  @Override
  public void setTempo(int tempo) {
    this.tempo = tempo;
  }

  @Override
  public int length() {
    return this.track.size();
  }

  @Override
  public int getHighestNote() {
    return highestNote;
  }

  @Override
  public int getLowestNote() {
    return lowestNote;
  }

  @Override
  public IMusicEditorModel fromFile(String fileName) throws FileNotFoundException {
    CompositionBuilder cb = new CompositionBuilderImpl();
    String path = new File("").getAbsolutePath() + "/" + "src/cs3500/music/SongFiles/" + fileName;
    System.out.print(path);
    FileReader mhll = new FileReader(path);
    IMusicEditorModel model = (IMusicEditorModel) MusicReader.parseFile(mhll, cb);
    return model;
  }

  @Override
  public void addRepeat(int activationBeat, int goBackToBeat) {
    // Adds repeat to beginning of list
    this.insertGoToBeat(new GoToBeat(activationBeat, goBackToBeat));
  }

  @Override
  public void addAltEnd(int startEnd1, int startEnd2) {
    int idxStartEnd2 = this.insertGoToBeat(new GoToBeat(startEnd2, 0));
    if (idxStartEnd2 == 0) {
      this.listGoToBeats.add(new GoToBeat(startEnd1, startEnd2));
    }
    else {
      this.listGoToBeats.add(idxStartEnd2 + 1, new GoToBeat(startEnd1, startEnd2));
    }
  }

  /**
   * Insert the GoToBeat in the list so that is sorted by location (low to high).
   * @param goToBeat The GoToBeat to be inserted
   * @return Returns the location at which the GoToBeat was placed.
   */
  private int insertGoToBeat(GoToBeat goToBeat) {
    for (int i = 0; i < listGoToBeats.size(); i++) {
      if (goToBeat.getLocation() <= this.listGoToBeats.get(i).getLocation()) {
        this.listGoToBeats.add(i, goToBeat);
        return i; // Stop iterating if beat is placed and return where it was place
      }
    }
    // Add the beat at the end if never placed
    this.listGoToBeats.add(goToBeat);
    return 0;
  }
}