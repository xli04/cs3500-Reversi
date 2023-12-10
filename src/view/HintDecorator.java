package view;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.Font;
import java.awt.Color;
import java.util.Map;
import javax.swing.JPanel;

import model.ModelDirection;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * The HintDecorator class represents a decorator for enhancing a graphical panel with hint
 * functionality. Hints can be enabled or disabled for a specific player independently.
 */
public class HintDecorator extends JPanel {
  private final ReadOnlyReversiModel model;
  private boolean hint;
  private RepresentativeColor color;
  private Map<RowColPair, RowColPair> location;
  private final HexBoardPanel panel;

  /**
   * Constructs a HintDecorator for the given Reversi model.
   *
   * @param model The ReadOnlyReversiModel to associate with the decorator.
   */
  public HintDecorator(ReadOnlyReversiModel model, HexBoardPanel panel) {
    hint = false;
    this.model = model;
    this.panel = panel;
    setOpaque(false);
    setPreferredSize(panel.getPreferredSize());
  }

  public void setColor(RepresentativeColor color) {
    this.color = color;
  }

  /**
   * turn on and turn off the hints.
   *
   * @param color if the current color is matching with this color
   */
  public void setFunctionality(RepresentativeColor color) {
    if (this.color == color) {
      hint = !hint;
    }
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.transform(panel.transformLogicalToPhysical());
    location = panel.getDrawingPoints();
    paint(g2d, panel.getSelectedPosition());
  }

  /**
   * Paint the number on the specific hexagon represents the number of cells that the current
   * player placed here can be flipped.
   */
  public void paint(Graphics2D g2d, RowColPair pair) {
    if (pair == null) {
      return;
    }
    if (!model.isGameOver() && color == model.getTurn() && hint) {
      Font font = new Font("Arial", Font.PLAIN, 4);
      g2d.setFont(font);
      g2d.scale(1, -1);
      g2d.setColor(Color.gray);
      Map<ModelDirection, Integer> values = model.checkMove(pair, model.getTurn());
      int value = 0;
      for (int i : values.values()) {
        value += i;
      }
      Point2D p2d = new Point2D.Double(location.get(pair).getRow(),
          location.get(pair).getCol() - 6);
      g2d.drawString(String.valueOf(value),
          (int) inverse().transform(p2d, null).getX(),
          (int) inverse().transform(p2d, null).getY());
    }
  }

  /**
   * Flipping the graphics vertically by applying a scaling transformation with
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
