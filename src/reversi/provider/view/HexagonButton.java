package reversi.provider.view;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Objects;
import reversi.provider.model.GamePiece;

/**
 * Represents a space on the reversi board, it contains its location on the board, and it contains
 * information about if the cell has a piece or not, and if there is a piece what piece. It also
 * indicates what color it is supposed to be at the given moment (ex. is it supposed to be
 * highlighted cyan or not).
 */
public class HexagonButton {
  // The current piece in the space
  private GamePiece piece;
  // The starting x coordinate of the path representing the hexagon shape
  private final double startX;
  // The starting y coordinate of the path representing the hexagon shape
  private final double startY;
  // The length of the side of one hexagon
  private final double sideLength;
  // The value that takes into account the angle of the line and says how much to increase or
  // decrease the x coordinate of the points in the path by
  private final double rotateThirty;
  // The path of the hexagon, it is used to draw and fill the hexagon
  private Path2D path;
  // The current color of the hexagon, typically gray, when the hexagon is clicked it is cyan
  private Color fillColor;

  /**
   * Constructs a hexagon based on the given game piece, side length, and x and y values of the
   * starting point on the hexagon. Sets the initial fill color to gray, does the math to calculate
   * the rotate thirty value based on the given side length.
   *
   * @param piece      represents the game piece in the hexagon
   * @param sideLength represents the length of one of the sides of the hexagons
   * @param startX     represents the x value of the starting point on the hexagon path
   * @param startY     represents the y value of the starting point on the hexagon path
   */
  public HexagonButton(GamePiece piece, double sideLength, double startX, double startY) {
    this.piece = Objects.requireNonNull(piece);
    if (sideLength < 1) {
      throw new IllegalArgumentException("The side length of a hexagon cannot be less than 1.");
    }
    this.sideLength = sideLength;
    if (startX < 0 || startY < 0) {
      throw new IllegalArgumentException("The starting X and Y values must be at least 0.");
    }
    this.startX = startX;
    this.startY = startY;
    this.rotateThirty = this.sideLength * Math.cos(Math.PI / 6);
    this.fillColor = Color.GRAY;
  }

  /**
   * Adds lines in the path to create the hexagon shape. Uses the side length and starting x and y
   * values passed into the constructor.
   */
  public void createHexagon() {
    this.path = new Path2D.Double();
    this.path.moveTo(startX, startY);
    // adds side 1
    this.path.lineTo(startX + rotateThirty, startY - sideLength / 2);
    // adds side 2
    this.path.lineTo(startX + 2 * rotateThirty, startY);
    // adds side 3
    this.path.lineTo(startX + 2 * rotateThirty, startY + sideLength);
    // adds side 4
    this.path.lineTo(startX + rotateThirty, startY + sideLength + sideLength / 2);
    // adds side 5
    this.path.lineTo(startX, startY + sideLength);
    // adds side 6
    this.path.lineTo(startX, startY);
  }

  /**
   * Creates the game piece on the space based on the current game piece, it is placed in the center
   * of the hexagon.
   *
   * @return an Ellipse2D.Double based on the starting x and y values and the size is based on the
   *        side length of the hexagon.
   */
  public Shape createGamePiece() {
    return new Ellipse2D.Double(startX + (rotateThirty - (sideLength / 2)), startY, sideLength,
        sideLength);
  }

  /**
   * Gets the color of the game piece in the space, if the cell is empty the color returns as gray
   * to indicate there is nothing in the space.
   *
   * @return a Color based on the current game piece value, for a black piece black is returned, for
   *        a white piece white is returned, for an empty piece gray is returned.
   */
  public Color getColor() {
    switch (this.piece) {
      case BLACK:
        return Color.BLACK;
      case WHITE:
        return Color.WHITE;
      default:
        return Color.GRAY;
    }
  }

  /**
   * Gets the path of the hexagon created in the createHexagon method.
   *
   * @return a Path2D representing the hexagon, it is based on the given starting x and y point
   */
  public Path2D getPath() {
    return this.path;
  }

  /**
   * Gets the color of the space so that it can be drawn on the panel in that color.
   *
   * @return returns the fill color.
   */
  public Color getFillColor() {
    return this.fillColor;
  }

  /**
   * Changes the fill color to the given color, used primarily to make the space cyan when clicked,
   * and to turn it back to gray when clicked again.
   *
   * @param color takes in the color that the cell should be
   */
  public void changeFillColor(Color color) {
    this.fillColor = color;
  }

  /**
   * Changes the game piece in the hexagon to the given game piece.
   *
   * @param gamePiece takes in the game piece that should be in the cell
   */
  public void updateGamePiece(GamePiece gamePiece) {
    this.piece = gamePiece;
  }
}
