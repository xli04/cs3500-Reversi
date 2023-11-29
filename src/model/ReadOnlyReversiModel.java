package model;

import java.util.Map;

/**
 * Represents a Reversi Model that can only be queried. Read-only reversi models can be queried
 * for game-logic and game state-related information for reversi games.
 */
public interface ReadOnlyReversiModel {
  /**
   * Returns a deep copy of the current board.
   *
   * @return the current board
   */
  Map<RowColPair, Hexagon> getBoard();

  /**
   * check if the game is over, if the both players pass in a row or the board is full.
   *
   * @return the curretn game states
   */
  boolean isGameOver();

  /**
   * check if there is valid move exits for the current player.
   *
   * @return true if there is valid moves, false otherwise
   * @throws IllegalStateException if the game is already ended
   * @throws IllegalArgumentException if the color are invalid
   */
  boolean hasToPass();

  /**
   * get the cell color at the given position.
   *
   * @param pair the position
   * @return the color in the given position
   * @throws IllegalArgumentException if the coordinators are invalid
   */
  RepresentativeColor getColorAt(RowColPair pair);

  /**
   * If the game is over, check if there is a winner color in the game.
   *
   * @return white means white color wins, black means black color wins,
   *         null means this is a tie game
   * @throws IllegalStateException if the game is still in process
   */
  RepresentativeColor getWinner();

  /**
   * Returns a map of each direction adjacent to a given cell in the current turn
   * , where the key is a specific direction, and the value is the number of tiles
   * that can be flipped with a move in that given direction. Used to calculate move.
   * If a value is 0, that means there are no cells that can be flipped in that direction.
   * If all directions have values of 0, this move is invalid, otherwise it is valid.
   *
   * @param pair the current coordinators
   * @param color the current player that to be checked
   * @return a map that contains the number of cells that can be flipped in the 6 directions
   * @throws IllegalStateException if the game is already ended
   * @throws IllegalArgumentException if the coordinators are invalid
   */
  Map<Direction, Integer> checkMove(RowColPair pair, RepresentativeColor color);

  /**
   * get the size of board, which refers to the side length of board.
   *
   * @return the side length of current board
   */
  int getSize();

  /**
   * Get the current turn for the game.
   *
   * @return the representative color for current turn
   * @throws IllegalStateException if the game is already ended
   */
  RepresentativeColor getTurn();


  /**
   * count the number of cells appear in the board in given color.
   *
   * @param color the given color
   * @return the score of that color
   */
  int getScore(RepresentativeColor color);

  /**
   * Get a copy of the game state in the current game.
   *
   * @param color which color of the player in the copy of the game will start with
   * @return a game that has the same board with current model
   */
  MutableReversiModel getDeepCopy(RepresentativeColor color);

  void addListener(ModelListener listener);
}
