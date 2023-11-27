package model;

import java.awt.Polygon;

/**
 * A Single hexagon tile in the game board.
 */
public final class Hexagon {
  private Polygon poly;

  private RepresentativeColor representativeColor;

  /**
   * initialize the hexagon with the given color.
   *
   * @param representativeColor the given color
   */

  public Hexagon(RepresentativeColor representativeColor) {
    this.representativeColor = representativeColor;
  }

  /**
   * get the color.
   *
   * @return the color fot current hexagon
   */
  public RepresentativeColor getColor() {
    return representativeColor;
  }

  /**
   * set the color to given color.
   *
   * @param clr the given color
   */
  public void setColor(RepresentativeColor clr) {
    representativeColor = clr;
  }

  /**
   * Get the polygon represent this hexagon.
   *
   * @return the hexagon
   */
  public Polygon getPolygon() {
    return poly;
  }

  /**
   * Set the polygon represent this hexagon.
   *
   * @param polygon the polygon to be setted
   */
  public void setPoly(Polygon polygon) {
    poly = polygon;
  }
}
