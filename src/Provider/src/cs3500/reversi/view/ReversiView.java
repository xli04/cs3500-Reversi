package Provider.src.cs3500.reversi.view;

import Provider.src.cs3500.reversi.model.GamePiece;
import Provider.src.cs3500.reversi.model.PlayerTurn;

/**
 * Represents the view of a Reversi game, it should display the game and game board at the current
 * state of the game. It should also be able to handle both mouse and key events.
 */
public interface ReversiView {
  /**
   * Sets the window to visible based on the boolean value passed in.
   *
   * @param show the boolean value representing if the window should be shown or not.
   */
  void display(boolean show);

  /**
   * Adds the given feature as a listener of the view, so when things are updated and notifications
   * of those updates are sent then the feature will be on the list to receive the notification.
   *
   * @param features the feature that needs to be added to the list, in this case the controller
   */
  void addFeatureListener(ViewFeatures features);

  /**
   * Sets the title of the JFrame to the given string.
   *
   * @param s the string that represents the title of the JFrame
   */
  void setTitle(String s);

  /**
   * Creates a popup notification on the JFrame if it is that player's turn.
   */
  void notifyPlayer();

  /**
   * Creates a popup notification when the game is over, the message is based off if the current
   * JFrame has the player who won or not.
   *
   * @param player the player that is attached to the JFrame
   * @param winner the winner of the game, the piece is empty in case of a tie
   */
  void showGameOverMessage(PlayerTurn player, GamePiece winner);

  /**
   * Updates the panel to have the current state of the model.
   */
  void update();
}
