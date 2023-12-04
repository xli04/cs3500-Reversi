package reversi.provider.view;

import reversi.provider.model.PlayerTurn;
import reversi.provider.model.CellPosition;

/**
 * Represents actions taken by the controller in the view that either update the model or get
 * information.
 */
public interface ViewFeatures {
  /**
   * Calls pass turn in the model.
   */
  void passTurn();

  /**
   * Calls playTurn in the model.
   *
   * @param cell represents the cell passed in to playTurn when called on the model.
   */
  void playTurn(CellPosition cell);

  /**
   * Returns the player that is attached to the controller, either BLACK or WHITE.
   *
   * @return the player turn attached to the controller
   */
  PlayerTurn controllerPlayer();

}
