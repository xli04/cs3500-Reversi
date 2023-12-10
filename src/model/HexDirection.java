package model;

/**
 * Represents a direction relative to a given cell on the board. The checking order
 * of the directions should be LEfT,RIGHT, RIGHTUP, RIGHTDOWN, LEFTUP, LEFTDOWN.
 */
public enum HexDirection implements ModelDirection {
  LEfT(-1, 0, 1),
  RIGHT(1, 0, -1),
  RIGHTUP(1, -1, 0),
  RIGHTDOWN(0, 1, -1),
  LEFTUP(0, -1, 1),
  LEFTDOWN(-1, 1, 0);

  private final int leftColOffset;
  private final int rowOffset;
  private final int rightColOffset;

  /**
   * initialize the direction with the given changes in row and column to get
   * the cell in such direction.
   *
   * @param leftColOffset the changes in row
   * @param rowOffset the changes in column
   * @param rightColOffset the changes in column
   */
  HexDirection(int leftColOffset, int rowOffset, int rightColOffset) {
    this.leftColOffset = leftColOffset;
    this.rowOffset = rowOffset;
    this.rightColOffset = rightColOffset;
  }

  /**
   * get the changes in LeftCol.
   *
   * @return the difference
   */
  public int getLeftColOffset() {
    return this.leftColOffset;
  }

  /**
   * get the changes in row.
   *
   * @return the difference
   */
  public int getRowOffset() {
    return this.rowOffset;
  }

  /**
   * get the change in the RightCol.
   *
   * @return the difference
   */
  public int getRightColOffset() {
    return this.rightColOffset;
  }


}
