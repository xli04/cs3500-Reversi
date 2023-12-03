package Provider.src.cs3500.reversi.model;

/**
 * Represents the primary model interface for playing a game of Reversi.
 */
public interface ReversiModel extends ReadOnlyReversiModel {

  /**
   * Starts a game of Reversi with the given boardSize. It first verifies that the game has not
   * already been started, and it verifies that the board size is not less than 3 because a game
   * cannot be played with a board size that is less than 3 as there are no valid moves that can be
   * made.
   * The board is then initialized to have the correct number of cells with the given board size,
   * for example a board of size 3 will be initialized to have 19 cells, and a board of size 5 will
   * have 91 cells. The board size given is the length of one side of the board. The board is also
   * initialized to have the six starting pieces on the board, the alternating pieces around the
   * center that.
   *
   * @param boardSize represents the size of the reversi board, the size given is the length of one
   *                  side of the board
   * @throws IllegalStateException    if the game has already started
   * @throws IllegalArgumentException if the boardSize is below 3
   */
  void startGame(int boardSize);

  /**
   * Passes the current player's turn, and switches it to the next player's turn.
   *
   * @throws IllegalStateException if the game has already been started,
   *                               if the game is over
   */
  void passTurn();

  /**
   * Adds the player's piece in the given cell, and flips all other cells that are in between the
   * given cell and another cell in any direction that ends with that player's piece. The cells that
   * are flipped must be the other player's, and they must all be in a row, there can be no empty
   * cells in between any cells being flipped or between the cells being flipped and the current
   * player's pieces.
   *
   * @param cell represents the position the player wants to place their piece
   * @throws IllegalStateException    if the game has already been started,
   *                                  if the game is over,
   *                                  if the given cell is not a valid move
   * @throws IllegalArgumentException if the given cell is not a part of the board,
   *                                  if the given cell is null,
   *                                  if the given cell already has a piece in it
   */
  void playTurn(CellPosition cell);
}
