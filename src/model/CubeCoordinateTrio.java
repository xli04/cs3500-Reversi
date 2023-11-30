package model;

/**
 * A cube pair coordinator system contains three coordinators used to determine the
 * adjacent cells. row means the row in our board the vertically line, the leftCol means
 * we count the number of column from left to right and the rightCol refers to count from
 * right to left.
 */
public final class CubeCoordinateTrio {
  private final int row;
  private final int leftCol;
  private final int rightCol;

  /**
   * construct the cubePair with given parameters.
   *
   * @param row the row
   * @param leftCol the left column
   * @param rightCol the right column
   */
  public CubeCoordinateTrio(int row, int leftCol, int rightCol) {
    this.row = row;
    this.leftCol = leftCol;
    this.rightCol = rightCol;
  }

  /**
   * convert the coordinators in cubeTrio to RowColPair by using the row to represent the row
   * and leftCol to represent the column since the way we counting element in the row is from
   * left to right.
   *
   * @return the RowColPair represent current position.
   */
  public RowColPair convertToRowCol() {
    return new RowColPair(row, leftCol);
  }

  /**
   * get the current row of this CubeTrio.
   *
   * @return the current row
   */
  public int getRow() {
    return row;
  }

  /**
   * get the current LeftCol of this CubeTrio.
   *
   * @return the current LeftCol
   */
  public int getLeftCol() {
    return leftCol;
  }

  /**
   * get the current RightCol of this CubeTrio.
   *
   * @return the current RightCol
   */
  public int getRightCol() {
    return rightCol;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CubeCoordinateTrio)) {
      return false;
    }
    return leftCol == ((CubeCoordinateTrio) obj).getRow()
      && row == ((CubeCoordinateTrio) obj).getRow()
      && rightCol == ((CubeCoordinateTrio) obj).getRightCol();
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}