package view;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Hexagon;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * a HexGrid represents the assemble of hexagons.
 */
public final class SquareGrid {
  private final int size;
  private Map<RowColPair, Hexagon> hexagons;
  private final Map<RowColPair, Ellipse2D> center;
  private Map<RowColPair, RowColPair> number;
  private final int width;
  private final int height;
  public final double theta = (Math.PI * 2) / 4.0;
  public final int hexagonLength = 14;
  private final ReadOnlyReversiModel model;

  /**
   * construct the hexgrid with the given parameters.
   *
   * @param preferWidth  the width of window
   * @param preferHeight the height of window
   * @param size         the size of the game board
   */
  public SquareGrid(ReadOnlyReversiModel model, int preferWidth, int preferHeight, int size) {
    this.model = model;
    width = preferWidth;
    height = preferHeight;
    hexagons = model.getBoard();
    center = new HashMap<>();
    this.size = size;
    number = new HashMap<>();
  }

  /**
   * update the current model board state to the view.
   *
   * @param board the current model
   */
  public void update(Map<RowColPair, Hexagon> board) {
    hexagons = new HashMap<>(board);
    number = new HashMap<>();
  }


  /**
   * fill the hexagon, if it is unoccupied, fill it with whole color, other wise draw a
   * circle inside it based on its topping point.
   *
   * @param g2d     the graph
   * @param polygon the current polygon
   */
  private void fillHexagon(Graphics2D g2d, Polygon polygon, RepresentativeColor color) {
    g2d.setColor(Color.BLACK);
    float strokeWidth = 0.1f;
    int currentSize = size;
    while (currentSize > 6) {
      strokeWidth += 0.2;
      currentSize--;
    }
    g2d.setStroke(new BasicStroke(strokeWidth));
    if (polygon != null) {
      g2d.draw(polygon);
      g2d.setColor(color.getActualColor());
      if (color == RepresentativeColor.BLACK || color == RepresentativeColor.WHITE) {
        g2d.setColor(RepresentativeColor.NONE.getActualColor());
      }
      g2d.fillPolygon(polygon);
    }
  }

  private Polygon createSquare(int x, int y, int size) {
    int[] xPoints = {x, x + hexagonLength, x + hexagonLength, x};
    int[] yPoints = {y, y, y + hexagonLength, y + hexagonLength};
    return new Polygon(xPoints, yPoints, size);
  }

  /**
   * first, get the coordinators for the middle point(0,0), use its coordinators to get the point
   * surround it, then if the hexagon was unoccupied(the color is none or cyan), we will fill the
   * hexagon, otherwise draw a circle in that hexagon based on the coordinators for its topping
   * point.
   */
  private void makeHexagons() {
    List<List<Integer>> originalPoint = new ArrayList<>();
    Polygon polygon = new Polygon();
    RowColPair startPair = new RowColPair(0, 0);
    polygon = createSquare(-2 * size, -2 * size, 4);
    RepresentativeColor currentColor = hexagons.get(startPair).getColor();
    hexagons.get(startPair).setPoly(polygon);
    int centerX = -2 * size;
    int centerY = -2 * size;
    if (currentColor == RepresentativeColor.BLACK || currentColor == RepresentativeColor.WHITE) {
      double circleRadius = hexagonLength / Math.sqrt(2);
      center.put(new RowColPair(0, 0),
          new Ellipse2D.Double(centerX - circleRadius, centerY - 8,
            circleRadius * 2, circleRadius * 2));
    } else if (currentColor == RepresentativeColor.NONE && !model.isGameOver()) {
      number.put(new RowColPair(0,0), new RowColPair(centerX, centerY));
    }
    for (RowColPair pair : hexagons.keySet()) {
      drawHexagon(originalPoint, pair);
    }
  }

  /**
   * use the coordinators for the middle point to find the coordinators for other
   * point.
   *
   * @param originalPoint the coordinators for the points of middle hexagon
   * @param pair          the current point
   */
  private void drawHexagon(List<List<Integer>> originalPoint, RowColPair pair) {
    Polygon polygon = new Polygon();
//    if (pair.getCol() == 0 && pair.getRow() == 0) {
//      return;
//    }
    int fixY = pair.getRow() * hexagonLength;
    int fixX = pair.getCol() * hexagonLength;
    polygon = createSquare(pair.getRow() + fixY - 2 *  size, pair.getCol() + fixX - size, 4);
    hexagons.get(pair).setPoly(polygon);
    RepresentativeColor currentColor = hexagons.get(pair).getColor();
    if (currentColor == RepresentativeColor.BLACK || currentColor == RepresentativeColor.WHITE) {
      double circleRadius = hexagonLength / Math.sqrt(3);
      center.put(pair,
          new Ellipse2D.Double(pair.getRow() + fixY - size - circleRadius + (double) size /2, pair.getCol() + fixX + size - circleRadius - (double) size /2,
            circleRadius, circleRadius));
    } else if (currentColor == RepresentativeColor.NONE && !model.isGameOver()
        && model.getColorAt(pair) == RepresentativeColor.NONE) {
        number.put(pair, new RowColPair(pair.getRow() + fixY - 2 *  size, pair.getCol() + fixX - size));
    }
  }

  /**
   * Paint the hexagons and all the circles represent the color inside it.
   *
   * @param g the <code>Graphics</code> object to protect
   */
  public void paintComponent(Graphics g) {
    makeHexagons();
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setColor(Color.DARK_GRAY);
    g2d.fillRect(-width / 2, -height / 2, width, height);
    List<RowColPair> list = new ArrayList<>(hexagons.keySet());
    Collections.sort(list);
    for (RowColPair pair : list) {
      Polygon polygon = hexagons.get(pair).getPolygon();
      fillHexagon(g2d, polygon, hexagons.get(pair).getColor());
    }
    for (RowColPair pair : center.keySet()) {
      g2d.setColor(hexagons.get(pair).getColor().getActualColor());
      g2d.fill(center.get(pair));
    }
  }

  /**
   * Check if the given point is inside any of the hexagon.
   *
   * @param p the given point
   * @return the coordinators in rowcol system that represent the position or null means
   *        the given position doesn't in any hexagons
   */
  public RowColPair getPoint(Point2D p) {
    for (RowColPair pair : hexagons.keySet()) {
      Hexagon hexagon = hexagons.get(pair);
      Polygon part = hexagon.getPolygon();
      if (part.contains(p)) {
        return pair;
      }
    }
    return null;
  }

  /**
   * set the hexagon in give position to given color.
   *
   * @param pair  the given position
   * @param color the given color
   */
  public void setColor(RowColPair pair, RepresentativeColor color) {
    hexagons.get(pair).setColor(color);
  }

  /**
   * get the color in the hexagon in given position.
   *
   * @param pair the given position
   * @return the representative in that hexagon
   */
  public RepresentativeColor getColor(RowColPair pair) {
    return hexagons.get(pair).getColor();
  }

  /**
   * Get the position of the valid move cell and the number of cells they can flip.
   *
   * @return a copy of the map for the the position of the valid move cell and the number
   *         of cells they can flip
   */
  public Map<RowColPair, RowColPair> getThePositionForDrawingNumber() {
    return new HashMap<>(number);
  }
}
