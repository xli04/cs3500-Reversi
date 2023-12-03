package Provider.src.cs3500.reversi.model;

import java.util.Map;

import Provider.src.cs3500.reversi.controller.Player;
import Provider.src.cs3500.reversi.model.CellPosition;

/**
 * Represents a model of Reversi that can only be read, there is no way to mutate this model. This
 * interface is used for things like the view where information about the model is needed but should
 * not be changed.
 */
public interface ReadOnlyReversiModel {

  /**
   * Signal if the game is over or not. A game is over if passCounter is 2, meaning both players of
   * the game have pass in a row. The game is over if the board is completely full, and the game is
   * over if there is only one color present on the board.
   *
   * @return true if game is over, false otherwise
   * @throws IllegalStateException if the game hasn't been started yet
   */
  boolean isGameOver();

  /**
   * Checks the board to see who won the game after the game is over.
   *
   * @return GamePiece.BLACK if black wins, GamePiece.WHITE if white wins, GamePiece.EMPTY if tie
   * @throws IllegalStateException if the game has already been started,
   *                               if the game is not over
   */
  GamePiece winner();

  /**
   * Creates a copy of the current board.
   *
   * @return new hashmap that is a copy of the board
   * @throws IllegalStateException if the game has not been started
   */
  Map<CellPosition, GamePiece> getBoard();

  /**
   * Finds the length of the sides of the board.
   *
   * @return a number that is the length of one side of the board
   * @throws IllegalStateException if the game has not been started
   */
  int getBoardWidth();

  /**
   * Determines if the current player contains a legal move.
   *
   * @return true if the current player contains a legal move.
   * @throws IllegalStateException if the game has not been started
   */
  boolean playerContainsLegalMove();

  /**
   * Returns the score for the given player.
   *
   * @return the integer for the score of the given player
   * @throws IllegalStateException if the game has not been started
   */
  int getPlayerScore(Player player);

  /**
   * Returns whether the given coordinate of the cell contains a legal move for the current player.
   *
   * @return true if the given coordinates of the cell has a legal move for the current player
   * @throws IllegalStateException if the game has not been started
   */
  boolean coordinateContainsLegalMove(CellPosition cell);

  /**
   * Finds the contents of the cell of the given coordinate.
   *
   * @return the GamePiece at the given coordinate
   * @throws IllegalStateException    if the game has not been started
   * @throws IllegalArgumentException if the given coordinate is invalid
   */
  GamePiece contentOfGivenCell(CellPosition cell);

  /**
   * Finds the length of the of valid cells to be flipped, if the move is valid.
   *
   * @return and integer of the amount of cells that can be flipped
   * @throws IllegalStateException if the game has not been started
   */
  int pathLength(CellPosition cell);

  /**
   * Finds which player's turn it currently is.
   *
   * @return the current player turn
   */
  PlayerTurn getCurrentPlayer();

  /**
   * Adds the given feature as a listener of the model, so when things are updated and notifications
   * of those updates are sent then the feature will be on the list to receive the notification.
   *
   * @param features the feature that needs to be added to the list, in this case the controller
   */
  void addFeatureListener(ModelFeatures features);

}
