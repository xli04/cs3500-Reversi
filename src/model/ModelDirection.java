package model;

import java.util.List;

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

  ModelDirection[] getDirection();
}
