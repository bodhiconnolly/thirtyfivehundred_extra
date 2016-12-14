package cs3500.music.view;

import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.JScrollBar;

import cs3500.music.controller.KeyboardHandler;

/**
 * A skeleton Frame (i.e., a window) in Swing.
 * CHANGE NOTE: Used to implement IMusicView, but now implements GuiView.
 */
public class GuiViewFrame extends JFrame implements GuiView {

  private GridControl.Grid grid;
  private int lowestNote;
  private int height;
  private boolean justScrolled = false;
  private JScrollPane scroller;
  private JFrame window;
  private JTextField input;
  private boolean editable;

  /**
   * Creates new GuiView.
   */
  public GuiViewFrame(int highestNote, int lowestNote, int numBeats, boolean editable) {

    this.lowestNote = lowestNote;
    this.editable = editable;

    //this.displayPanel = new ConcreteGuiViewPanel();
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    height = highestNote - this.lowestNote;
    //this.getContentPane().setLayout(new BorderLayout());
    grid = new GridControl.Grid(numBeats, height, this.lowestNote);
    grid.setSize();
    window = new JFrame();
    window.setSize(getPreferredSize());
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());
    input = new JTextField(15);
    if (editable) {
      panel.add(input);
    }
    panel.add(grid);

    scroller = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    window.add(scroller);
    window.setVisible(true);

  }

  @Override
  public void setRepeats(ArrayList<ArrayList<Integer>> repeatList){
    this.grid.setStarts(repeatList.get(0));
    this.grid.setEnds(repeatList.get(1), repeatList.get(2));
  }


  /**
   * Render note.
   *
   * @param rawpitch   pitch
   * @param volume     volume of note
   * @param duration   Duration of note
   * @param instrument instr
   * @param beatNum    beat
   */
  public void renderNote(int rawpitch, int volume, int duration, int instrument, int beatNum) {
    for (int i = 1; i < duration; ++i) {
      //render sustains
      grid.fillCell(beatNum + i, height - rawpitch, Color.GREEN);
    }
    //render first note
    grid.fillCell(beatNum, height - rawpitch, Color.BLACK);

  }

  @Override
  public void setTempo(int tempo) {
    throw new UnsupportedOperationException("Can't set a tempo for this type of view.");
  }

  @Override
  public int getTempo() {
    throw new UnsupportedOperationException("No tempo for this type of view.");
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(800, 500);
  }

  @Override
  public void setBeat(int num) {
    JScrollBar vertical = this.scroller.getHorizontalScrollBar();
    Rectangle r = window.getBounds();
    int w = r.width;
    grid.setBeat(num, justScrolled);
//    System.out.print("Beat pos: ");
//    System.out.println(num*20);
//    System.out.print("Scroll pos: ");
//    System.out.print(vertical.getValue());
//    System.out.print(" ");
//    System.out.println(vertical.getValue()+w);

    if (num*20>vertical.getValue()+w-100){
      vertical.setValue(num*20-40);
    }
    if (num*20<vertical.getValue()){
      vertical.setValue(num*20-40);
    }

  }

  @Override
  public void keyboardCallback(KeyboardHandler handler) {
    if (editable) {
      input.addKeyListener(handler);
      input.setFocusable(true);
    } else {
      window.addKeyListener(handler);
      window.setFocusable(true);
    }

  }

  @Override
  public void scroll(int toScroll) {
    JScrollBar vertical = this.scroller.getHorizontalScrollBar();
    vertical.setValue(vertical.getValue() + toScroll);
  }

  @Override
  public int getLowestNote() {
    return this.lowestNote;
  }

  @Override
  public String getTextInput() {
    String command = this.input.getText();
    this.input.setText("");
    return command;
  }

  @Override
  public void update(boolean noteChange) {
    if (noteChange) {
      grid.resetNotes();
    }
    grid.repaint();
  }

  @Override
  public void togglePlaying() {
    throw new UnsupportedOperationException("GuiViewFrame does not support this operation.");
  }
  
  @Override
  public void jumpToStart() {
    scroll(-50000);
   }
  
  @Override
  public void jumpToEnd() {
   scroll(50000);
  }


}