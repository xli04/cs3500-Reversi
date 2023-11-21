package model;

import java.awt.Color;

/**
 * A representation of the colors of board positions and players.
 */
public enum RepresentativeColor {
  /**
   * An NONE color, represent the empty position.
   */
  NONE("None", Color.LIGHT_GRAY),

  /**
   * A white cell or a position with a white hexagon.
   */
  WHITE("White", Color.WHITE),

  /**
   * A black cell or a position with a black hexagon.
   */
  BLACK("Black", Color.BLACK),

  /**
   * A Cyan color represents a mark to notify the user that they are selecting this hexagon.
   */
  CYAN("Cyan", Color.CYAN);


  private final String name;
  private final Color color;

  /**
   * each color has its name in upper letter and a color used to display it in JFrame.
   *
   * @param name its name
   * @param color its display color
   */
  RepresentativeColor(String name, Color color) {
    this.name = name;
    this.color = color;
  }

  /**
   * Gets the capitalized name of this tile color.
   *
   * @return the capitalized name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the display color of this tile color.
   *
   * @return the display color
   */
  public Color getActualColor() {
    return color;
  }

  /**
   * Returns the opposite of this color, where BLACK and
   * WHITE are opposites.
   *
   * @return the opposite of this color
   * @throws IllegalArgumentException if the color is not black or white
   */
  public RepresentativeColor getOpposite() {
    switch (this) {
      case WHITE:
        return BLACK;
      case BLACK:
        return WHITE;
      default:
        throw new IllegalArgumentException("Invalid color");
    }
  }
}
