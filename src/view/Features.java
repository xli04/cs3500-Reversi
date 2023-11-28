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

  /**
   * Displays the hints for a current player, that is the number of tiles of the color of the
   * player that would be flipped by each potential move.
   */
  void showHints();
}