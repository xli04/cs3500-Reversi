package Provider.src.cs3500.reversi.model;

import java.util.Objects;

/**
 * Represents a cell in the ReversiModel, it has an x, y, and z position that indicate where the
 * cell is on the board.
 * Coordinate System:
 * The origin is located in the center of the hexagon, with the coordinates 0, 0, 0.
 * The x-values are the horizontal axis of the hexagon, increasing when going up and decreasing to
 * the down.
 * The y-values are the line of hexagons that has a positive slope, increasing values when
 * going down and to the right, and decreasing when going up and to the left.
 * The z-values are the line of hexagon that has a negative slope, increasing values when going
 * up and to the right, and decreasing values when going down and to the left.
 */
public class CellPosition {
  // represents the x position of the cell <->
  private final int x;
  // represents the y position of the cell /
  private final int y;
  // represents the z position of the cell \
  private final int z;

  /**
   * Constructs a new CellPosition and initializes the x, y, and z fields.
   *
   * @param x represents the x value of the CellPosition
   * @param y represents the y value of the CellPosition
   * @param z represents the z value of the CellPosition
   */
  public CellPosition(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public boolean equals(Object other) {
    if ((other instanceof CellPosition)) {
      CellPosition that = (CellPosition) other;

      return this.x == that.x && this.y == that.y && this.z == that.z;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  // returns the x value of the cellPosition
  public int getX() {
    return this.x;
  }

  // returns the y value of the cellPosition
  public int getY() {
    return this.y;
  }

  // returns the z value of the cellPosition
  public int getZ() {
    return this.z;
  }
}
