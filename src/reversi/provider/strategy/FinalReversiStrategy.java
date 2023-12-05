package reversi.provider.strategy;

import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.model.CellPosition;

/**
 * Allows an AI to play, is an infallible strategy that is able to play the game.
 */

public interface FinalReversiStrategy {

  /**
   * Chooses a position on the board based on their strategy.
   *
   * @param model  Reversi model to be played on
   * @param player player that is going to be played
   * @return CellPosition of where the player is going to move
   */
  CellPosition chooseCellPosition(ReversiModel model, PlayerTurn player);

}
