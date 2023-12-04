package reversi.provider.controller;

import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.model.CellPosition;

/**
 * Represents a player that can play in the Reversi Game.
 */
public interface Player {

  /**
   * Allows the current player to take their turn.
   *
   * @param model the reversi game to be played on
   * @return the CellPosition of where the player is going to play, or null if not
   */
  CellPosition takeTurn(ReversiModel model);

  /**
   * Returns the current player.
   *
   * @return a player
   */
  PlayerTurn getPlayer();

  boolean isAi();
}

