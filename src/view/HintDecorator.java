package view;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import model.ModelDirection;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

public class HintDecorator implements PanelDecorator{
  private boolean hint;
  private final ReadOnlyReversiModel model;
  private RepresentativeColor color;
  private Map<RowColPair, RowColPair> location;

  public HintDecorator(ReadOnlyReversiModel model) {
    hint = false;
    this.model = model;
  }

  @Override
  public void setColor(RepresentativeColor color) {
    this.color = color;
  }

  @Override
  public void setFunctionality(RepresentativeColor color) {
    if (this.color == color) {
      hint = !hint;
    }
  }

  /**
   * Paint the number on the specific hexagon represents the number of cells that the current
   * player place here can flipped.
   */
  @Override
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
      }
  }

  @Override
  public void setDrawingPoints(Map<RowColPair, RowColPair> map) {
    location = new HashMap<>(map);
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
