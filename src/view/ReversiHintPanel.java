package view;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Objects;

import javax.swing.*;

import model.ModelDirection;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

public class ReversiHintPanel extends JPanel {
  private final ReadOnlyReversiModel model;
  private final Dimension dimension;
  private RowColPair pair;
  private final RepresentativeColor color;
  private boolean showHints;
  private final HexGrid hexGrid;
  private static int preferWidth = 100;
  private static int preferHeight = 100;
  public ReversiHintPanel(ReadOnlyReversiModel model, Dimension dimension, RepresentativeColor color) {
    this.model = Objects.requireNonNull(model);
    int boardSize = model.getSize();
    while (boardSize > 6) {
      preferWidth += 100;
      preferHeight += 110;
      boardSize -= 6;
    }
    hexGrid = new HexGrid(model, preferWidth, preferHeight, model.getSize());
    this.dimension = dimension;
    pair = null;
    this.color = color;
    showHints = false;
  }

  public void setSelectedPosition(RowColPair selected) {
    pair = selected;
  }

  public void setShowHints() {
    showHints = !showHints;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setLayout(new FlowLayout());
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.transform(transformLogicalToPhysical());
    paintNumbers(g2d, pair);
  }

  protected void paintNumbers(Graphics2D g2d, RowColPair pair) {
    if (pair == null) {
      return;
    }
    if (!model.isGameOver() && color == model.getTurn() && showHints) {
      Font font = new Font("Arial", Font.PLAIN, 4);
      g2d.setFont(font);
      g2d.scale(1, -1);
      g2d.setColor(Color.gray);
      Map<RowColPair, RowColPair> location = hexGrid.getThePositionForDrawingNumber();
      Map<ModelDirection, Integer> values = model.checkMove(pair, model.getTurn());
      int value = 0;
      for (int i : values.values()) {
        value += i;
      }
      Point2D p2d = new Point2D() {
        @Override
        public double getX() {
          return location.get(pair).getRow();
        }

        @Override
        public double getY() {
          return location.get(pair).getCol() - 6;
        }

        @Override
        public void setLocation(double x, double y) {
          // no action for setLocation.
        }
      };
      g2d.drawString(String.valueOf(value),
        (int) inverse().transform(p2d, null).getX(),
        (int) inverse().transform(p2d, null).getY());
      repaint();
    }
  }

  private AffineTransform transformLogicalToPhysical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = dimension;
    ret.translate(getWidth() / 2., getHeight() / 2.);
    ret.scale(getWidth() / preferred.getWidth(), getHeight() / preferred.getHeight());
    ret.scale(1, -1);
    return ret;
  }

  /**
   * flipping the graphics vertically by applying a scaling transformation with
   * a scaling factor of 1 along the x-axis (no change) and a scaling factor of -1
   * along the y-axis.
   *
   * @return the transformation after flipping
   */
  private AffineTransform inverse() {
    AffineTransform ret = new AffineTransform();
    ret.scale(1, -1);
    return ret;
  }
}
