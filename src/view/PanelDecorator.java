package view;

import java.awt.Graphics2D;
import java.util.Map;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * The {@code PanelDecorator} interface defines the contract for classes that act as decorators
 * for graphical panels. Decorators enhance the appearance or behavior of a panel, allowing dynamic
 * modifications to the way panels are rendered.
 */
public interface PanelDecorator {

  /**
   * Sets the primary color associated with the decorator. This color is used to modify the
   * appearance of the decorated panel.
   *
   * @param color The representative color to set for the decorator.
   */
  void setColor(RepresentativeColor color);

  /**
   * Sets additional functionality based on the specified representative color. The exact behavior
   * and functionality added depend on the implementation of the decorator.
   *
   * @param color The representative color to determine the added functionality.
   */
  void setFunctionality(RepresentativeColor color);

  /**
   * Paints or renders the decorated panel using the provided {@code Graphics2D} context and the
   * specified coordinates.
   *
   * @param g2d   The graphics context to use for rendering.
   * @param pair  The coordinates (row-col pair) specifying the position where the decoration
   *              should be applied.
   */
  void paint(Graphics2D g2d, RowColPair pair);

  /**
   * Sets the drawing points associated with the decorator. Drawing points indicate specific
   * locations on the panel that the decorator will affect or modify.
   *
   * @param map The mapping of drawing points, where each entry represents the original point
   *            and its corresponding modified point.
   */
  void setDrawingPoints(Map<RowColPair, RowColPair> map);
}
