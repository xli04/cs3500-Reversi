package model;

/**
 * The SquareDirection enum represents directions on a square grid for modeling movements.
 * Each direction has associated row and column offsets.
 */
public enum SquareDirection implements ModelDirection {
  SQUARELEFT(0, -1),
  SQUARERIGHT(0, 1),
  SQUAREUP(-1, 0),
  SQUAREDOWN(1, 0),
  SQUARELEFTUP(-1, -1),
  SQUARERIGHTUP(-1, 1),
  SQUARELEFTDOWN(1, -1),
  SQUARERIGHTDOWN(1, 1);

  private final int rowOffset;
  private final int colOffset;

  /**
   * Constructs a SquareDirection with the specified row and column offsets.
   *
   * @param rowOffset The offset in the row direction.
   * @param colOffset The offset in the column direction.
   */
  SquareDirection(int rowOffset, int colOffset) {
    this.rowOffset = rowOffset;
    this.colOffset = colOffset;
  }

  @Override
  public int getLeftColOffset() {
    return colOffset;
  }

  public int getRowOffset() {
    return rowOffset;
  }

  @Override
  public int getRightColOffset() {
    return 0;
  }
}
