package reversi.provider.model;

/**
 * Represents actions taken by the controller in the model that either update the view or get
 * information.
 */
public interface ModelFeatures {
  /**
   * Calls get currentPlayer on the model.
   *
   * @return the PlayerTurn of the current turn in the model, either a BLACK or WHITE turn
   */
  PlayerTurn getPlayerTurn();

  /**
   * If the controller has an ai player then it takes the next turn, if it is a human player it
   * calls notifyPlayer in the view, which tells the player it is their turn.
   */
  void turnSwitched();

  /**
   * Calls the showGameIsOverMessage on the view that tells the players the game is over and if they
   * won or lost.
   */
  void gameIsOver();

  /**
   * Calls the update method on the view that will update the view to have the current state of the
   * model.
   */
  void updateBoard();
}
