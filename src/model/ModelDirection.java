package model;

/**
 * The ModelDirection interface represents directions used in a game model.
 * Implementing classes/enums provide specific directions with associated
 * row and column offsets.
 */
public interface ModelDirection {

  /**
   * get the changes in LeftCol.
   *
   * @return the difference
   */
  int getLeftColOffset();

  /**
   * get the changes in row.
   *
   * @return the difference
   */
  int getRowOffset();

  /**
   * get the change in the RightCol.
   *
   * @return the difference
   */
  int getRightColOffset();
}
