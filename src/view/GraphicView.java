package view;

import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * A marker interface for all GUI views, to be used in the Reversi game.
 */
public interface GraphicView {

  /**
   * display the view.
   */
  void display();

  /**
   * Get the user's current selected position in the board.
   *
   * @return the coordinators that chosen by the user
   */
  RowColPair getSelectedPosition();

  /**
   * after the user successfully place a cell in the selected position,
   * reset the select position to null.
   */
  void resetSelectedPosition();

  /**
   * update the current game state to the drawing board.
   *
   * @param model the current model
   */
  void resetPanel(ReadOnlyReversiModel model);

  /**
   * Add the features to the view.
   *
   * @param features the feature gonna to add.
   */
  void addFeatures(Features features);

  /**
   * update the model to all the views that care about this model.
   *
   * @param model the update model.
   */
  void update(ReadOnlyReversiModel model);

  /**
   * display the error message to notify the users include invalid move and
   * game is over with the winner.
   *
   * @param s the message that to be showed
   */
  void showStates(String s);

  /**
   * lock the mouse event for the non human players.
   */
  void lockMouseForNonHumanPlayer();

  /**
   * show hints to the player show required to do so.
   *
   * @param color the color the player placing cell for
   */
  void showHints(RepresentativeColor color);

  /**
   * Set the game over message for the users and notify them who is the winner.
   *
   * @param winner the winner
   * @param winOrNot whether this player win the game
   */
  void setGameOverState(RepresentativeColor winner, boolean winOrNot);

  /**
   * If the there is no valid move in current turn, and it this player's turn to make move,
   * notify the player.
   *
   * @param hasToPass whether there is valid move in current turn
   * @param isYourTurn the current turn in model
   */
  void setHasToPassWarning(boolean hasToPass, boolean isYourTurn);

  /**
   * set the given color to the panel in order to only show the hints to the plyaer
   * who required to do so.
   *
   * @param color the given color
   */
  void setColor(RepresentativeColor color);

  /**
   * notify the who's turn it is, if it this player's turn, notify the player.
   *
   * @param color the current turn in model
   * @param isYourTurn whether is this player's turn
   */
  void toggleTurn(RepresentativeColor color, boolean isYourTurn);
}
