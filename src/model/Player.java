package model;

import java.util.Optional;

/**
 * Represents a player for a game of Reversi. Reversi games controller will assign each player
 * a color. Players select their moves, and then take turns making moves with their color, with
 * the goal of maximizing their probabilities of victory. When the game ends, the player with
 * the higher score is declared as the winner or there is no winner.
 */
public interface Player {
  /**
   * choose next possible move.
   *
   * @param model the current model
   * @return the position to place the cell ot null if there is no valid move.
   */
  Optional<RowColPair> chooseNextMove(ReadOnlyReversiModel model);

  /**
   * get the color of the cell that this player will place.
   *
   * @return the color
   */
  RepresentativeColor getColor();

  /**
   * check if the current player is an Ai player.
   *
   * @return true if it is an Ai player, otherwise false
   */
  boolean isAiPlayer();

  /**
   * Assign the color to the player, which represents the color of cell they will place for.
   *
   * @param color the color of cell that they will place for
   */
  void assignColor(RepresentativeColor color);
}
