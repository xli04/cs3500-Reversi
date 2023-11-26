package model;

import java.util.Objects;

/**
 * Represents row-column pair. Rows and cols are both zero-indexed. (0,0) means the middle cell
 * in our board.
 */
public final class RowColPair implements Comparable<RowColPair> {
  private final int row;
  private final int col;

  /**
   * construct the RowColPai with the given row and column.
   *
   * @param row the given row
   * @param col the given column
   */
  public RowColPair(int row, int col) {
    this.row = row;
    this.col = col;
  }

  /**
   * get the row in this pair.
   *
   * @return the row
   */
  public int getRow() {
    return row;
  }

  /**
   * get the colum in this pair.
   *
   * @return the column
   */
  public int getCol() {
    return col;
  }

  /**
   * Converting the current position in RowColPair system to cubeTrio system by using the
   * row to represent r and col to represent leftCol since we are reading the row and colum
   * from left to right, using row and col to represents rightCol.
   *
   * @return the CubeCoordinateTrio represents current position
   */
  public CubeCoordinateTrio convertToCube() {
    int s = -row - col;
    return new CubeCoordinateTrio(row, col, s);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof RowColPair)) {
      return false;
    }
    return row == ((RowColPair) obj).getRow() && col == ((RowColPair) obj).getCol();
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }

  @Override
  public int compareTo(RowColPair o) {
    if (this.row < o.getRow()) {
      return 1;
    } else if (this.row > o.getRow()) {
      return -1;
    } else {
      if (this.col < o.getCol()) {
        return 1;
      } else if (this.col > o.getCol()) {
        return -1;
      }
    }
    return 0;
  }
}
