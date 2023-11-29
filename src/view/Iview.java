package view;

import model.ReadOnlyReversiModel;
import model.RepresentativeColor;

/**
 * A marker interface for all views, to be used in the Reversi game.
 */
public interface Iview {

  /**
   * display the view.
   */
  void display();

  /**
   * Add the features to the view.
   *
   * @param features the feature gonna to add.
   */
  void addFeatures(Features features);

  /**
   * display the message to notify the users include invalid move and
   * game is over with the winner.
   *
   * @param s the message that to be showed
   */
  void showMessage(String s);

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
   * set the given color to the panel in order to only show the hints to the plyaer
   * who required to do so.
   *
   * @param color the given color
   */
  void setColor(RepresentativeColor color);

  /**
   * Update the view to the most recent state, so that it can reflect the game status properly,
   * it will update include: the board, notify the user it's your turn, notify the user is
   * game over or no valid move exist and the player can only choose to pass.
   *
   * @param model the current model up to dated
   * @param player the color of the player
   */
  void update(ReadOnlyReversiModel model, RepresentativeColor player);
}
