package cs3500.music.view;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import cs3500.music.model.DiatonicScale;

/**
 * A class for creating and customizing a grid.
 */
class GridControl {

  /**
   * Grid that is controller.
   */
  static class Grid extends JPanel {

    private List<Point> fillCellsBlack;
    private List<Point> fillCellsGreen;

    private List<Integer> startRepeats;
    private List<Integer> endRepeats;
    private List<Integer> repeatNums;


    private int xSize;
    private int ySize;
    private int lowestNote;

    private int sideLength = 20;

    private int beatPosition = -1;
    private int beatScrolled = 0;

    Grid(int xSize, int ySize, int lowestNote) {
      this.xSize = xSize;
      this.ySize = ySize;
      this.lowestNote = lowestNote;
      fillCellsBlack = new CopyOnWriteArrayList<Point>();
      fillCellsGreen = new CopyOnWriteArrayList<Point>();
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;


      for (Point fillCell : fillCellsGreen) {
        int cellX = sideLength * 2 + (fillCell.x * sideLength);
        int cellY = sideLength + (fillCell.y * sideLength);
        g.setColor(Color.GREEN);
        g.fillRect(cellX, cellY, sideLength, sideLength);
      }
      for (Point fillCell : fillCellsBlack) {
        int cellX = sideLength * 2 + (fillCell.x * sideLength);
        int cellY = sideLength + (fillCell.y * sideLength);
        g.setColor(Color.BLACK);
        g.fillRect(cellX, cellY, sideLength, sideLength);
      }
      g.setColor(Color.BLACK);
      g2.setStroke(new BasicStroke(2));
      g.drawRect(sideLength * 2, sideLength, (xSize) * sideLength, (ySize + 1) * sideLength);
      g2.setStroke(new BasicStroke(1));
      //vertical
      for (int i = sideLength * 2; i <= (xSize + 2) * sideLength; i += sideLength) {
        if ((i / sideLength - 2) % 4 == 0) {
          g2.setStroke(new BasicStroke(2));
          g.drawLine(i, sideLength, i, (ySize + 2) * sideLength);
        } else {
          g2.setStroke(new BasicStroke(2));
        }
      }
      //horizontal
      for (int i = sideLength; i <= (ySize + 2) * sideLength; i += sideLength) {
        if ((i / sideLength) % 4 == 0) {
          g.drawLine(sideLength * 2, i, (xSize + 2) * sideLength, i);
        } else {
          g.drawLine(sideLength * 2, i, (xSize + 2) * sideLength, i);
        }
      }

      //beat line
      if (beatPosition != -1) {
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.RED);
        g.drawLine((beatPosition + 1) * sideLength, sideLength, (beatPosition + 1)
                * sideLength, (ySize + 2) * sideLength);
        g2.setColor(Color.BLACK);
      }

      if (startRepeats!=null){
        //draw start repeats
        int lineDelta = 4;
        for (int i = 0; i < startRepeats.size(); ++i) {
          int beatStart = startRepeats.get(i)+1;
          g2.setStroke(new BasicStroke(2));
          g2.setColor(Color.BLUE);
          g.drawLine((beatStart + 1) * sideLength, sideLength, (beatStart + 1)
                  * sideLength, (ySize + 2) * sideLength);
          g.drawLine((beatStart + 1) * sideLength+lineDelta, sideLength, (beatStart + 1)
                  * sideLength+lineDelta, (ySize + 2) * sideLength);
          g2.setColor(Color.BLACK);
        }

        //draw end repeats
        for (int i = 0; i < endRepeats.size(); ++i) {
          int beatStart = endRepeats.get(i)+1;
          g2.setStroke(new BasicStroke(2));
          g2.setColor(Color.CYAN);
          g.drawLine((beatStart + 1) * sideLength, sideLength, (beatStart + 1)
                  * sideLength, (ySize + 2) * sideLength);
          g.drawLine((beatStart + 1) * sideLength+lineDelta, sideLength, (beatStart + 1)
                  * sideLength+lineDelta, (ySize + 2) * sideLength);
          g2.setColor(Color.BLACK);
          if (repeatNums.get(i)!=0){
            drawEndings(g, beatStart-1, repeatNums.get(i));
          }
        }
      }

      drawLabels(g);
    }

    private void drawLabels(Graphics g) {
      for (int i = 0; i < xSize; ++i) {
        if (i % 16 == 0) {
          g.drawString(Integer.toString(i),
                  (i + 2) * sideLength, sideLength * 3 / 4);
        }
      }
      for (int i = 0; i < ySize + 1; ++i) {
        int j = ySize - i;
        g.drawString((new DiatonicScale().toString(
                (lowestNote + j) % 12 + 1)
                        + Integer.toString((lowestNote + j) / 12 - 1)),
                0, (int) (sideLength * (i + 1.7)));
      }
    }

    private void drawEndings(Graphics g, Integer beatNum, Integer label) {
      g.setColor(Color.cyan);
         g.drawString(Integer.toString(label),
                  (beatNum + 2) * sideLength, sideLength * 3 / 4);
      g.setColor(Color.black);
    }

    void fillCell(int x, int y, Color c) {
      if (c == Color.BLACK) {
        fillCellsBlack.add(new Point(x, y));
      } else {
        fillCellsGreen.add(new Point(x, y));
      }
      repaint();
    }

    void setSize() {
      super.setPreferredSize(
              new Dimension(sideLength * (xSize + 3),
                      sideLength * (ySize + 3)));
    }

    int setBeat(int num, boolean scrolled) {
      this.beatPosition = num + 1;
      repaint();
      if (scrolled) {
        beatScrolled = 1;
      } else {
        beatScrolled += 1;
      }
      return beatScrolled * sideLength;
    }

    void resetNotes() {
      fillCellsBlack.clear();
      fillCellsGreen.clear();
    }


    void setStarts(List<Integer> ins){
      startRepeats = new CopyOnWriteArrayList<Integer>();
      for (int element : ins){
        startRepeats.add(element);
      }
    }

    void setEnds(List<Integer> ins, List<Integer> nums){
      endRepeats = new CopyOnWriteArrayList<Integer>();
      repeatNums = new CopyOnWriteArrayList<Integer>();
      for (int element : ins){
        endRepeats.add(element);
      }
      for (int element : nums){
        repeatNums.add(element);

      }
    }
  }
}