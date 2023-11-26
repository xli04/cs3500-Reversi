package view;

import model.RowColPair;

/**
 * A feature interface represent the action that controller can make.
 */
public interface Features {
  /**
   * Place a cell in the given position.
   *
   * @param pair the given position
   */
  void placeMove(RowColPair pair);

  /**
   * Make pass in current turn.
   */
  void makePass();

  void showHints();
}