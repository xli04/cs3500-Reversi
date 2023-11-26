package view;

import model.RowColPair;

/**
 * Represents communication between a Controller and a View. Allows the Controller to
 * be called back whenever a high-level event occurs, and allows the View to translate
 * low-level events into high-level events that can be broadcasted to the Controller.
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
   * Displays hints for the amount of tiles that can be flipped for a given potential moves.
   */
  void showHints();
}