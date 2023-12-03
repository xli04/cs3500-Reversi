package Provider.src.cs3500.reversi.controller;

import Provider.src.cs3500.reversi.model.PlayerTurn;
import Provider.src.cs3500.reversi.model.ReversiModel;
import Provider.src.cs3500.reversi.model.CellPosition;

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

