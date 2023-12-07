package model;

public enum SquareDirection implements ModelDirection{
  SQUARELEFT(-1, 0),
  SQUARERIGHT(1, 0),
  SQUAREUP(0, -1),
  SQUAREDOWN(0, 1),
  SQUARELEFTUP(-1, -1),
  SQUARERIGHTUP(-1, 1),
  SQUARELEFTDOWN(1, -1),
  SQUARERIGHTDOWN(1, 1);

  private final int rowOffset;
  private final int colOffset;

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

  @Override
  public ModelDirection[] getDirection() {
    return values();
  }
}
