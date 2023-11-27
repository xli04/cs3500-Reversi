package model;

/**
 * a ModelStatus interface represents the status of the model that the player passed in, it
 * has many different status to show the current state of the model.
 */
public interface ModelStatus {

  /**
   * Return the status of the given model, when the game is already over, it will return end,
   * when the game in process and there is no other elements bother the game, it will return
   * InProcess, if the game has not started yet, it will return HasNotStarted, if there is
   * no valid mov exist in this board, it will return Blocked.
   *
   * @param model the model that need to check the status
   * @return the status that represents the current state of the game
   */
  status getStatus(ReadOnlyReversiModel model);

  /**
   * Represents the status for a game of Reversi
   */
  enum status {
    /**
     * The game has ended, either by 2 consecutive passes or by the board filling up
     */
    END,
    /**
     * InProgress = the game has started and there are still legal moves on the baord (at least
     * one legal move for black or for white)
     */
    InProgress,

    /**
     * HasNotStarted = startGame() has not been called yet.
     */
    HasNotStarted,

    /**
     * Blocked = no valid moves, but the game is not over yet since the game has no passed
     */
    BLOCKED
  }
}
