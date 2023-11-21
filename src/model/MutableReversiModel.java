package model;

/**
 * Represents a Reversi Model that can be manipulated. Mutable reversi models contain
 * the domain logic, about the current game state, for a game of reversi,
 * and this game logic can be updated. Mutatable reversi models can be queried
 * for game-logic related information or mutated to cause some change in game state.
 */
public interface MutableReversiModel extends ReadOnlyReversiModel {
  /**
   * check if the attempt move is a valid move. check if the attempt position is
   * next to the opposite color cell with the current turn color and check if we can
   * flip some cell in a certain direction. If the player place the cell successfully
   * the turn will alter to another player.
   *
   * @param pair the row-col pair
   * @param currentPlayer the player that want to place
   * @throws IllegalStateException if the game is already ended
   * @throws IllegalArgumentException If the coordinators are invalid
   * @throws IllegalStateException If we can not place the given color cell in given position or
   *         player place a cell when it's not his or her turn
   */
  void placeMove(RowColPair pair, RepresentativeColor currentPlayer);

  /**
   * make a pass action when the user has to pass in this turn or the user wants to pass
   * alter the turn to another player.
   *
   * @param currentPlayer the player that want to place
   * @throws IllegalStateException if the game is already ended or
             player place a cell when it's not his or her turn
   */
  void makePass(RepresentativeColor currentPlayer);

  /**
   * notify all the elements such as controller and view that care about the state of this model
   * that the game is start, and set the turn to black.
   */
  void startGame();
}
