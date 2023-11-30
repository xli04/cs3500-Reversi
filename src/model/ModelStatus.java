package model;

/**
 * a ModelStatus interface represents the status of the model that the player passed in, it
 * has many different status to show the current state of the model.
 */
public interface ModelStatus {

  /**
   * update the status of the given model, when the game is already over, it update to end,
   * when the game in process and there is no other elements bother the game, it will update to
   * InProcess, if the game has not started yet, it will update to HasNotStarted, if there is
   * no valid mov exist in this board, it will update to Blocked.
   *
   * @param model the model that need to check the status
   */
  void updateStatus(ReadOnlyReversiModel model);

  /**
   * Return the status of the given model, when the game is already over, it will return end,
   * when the game in process and there is no other elements bother the game, it will return
   * InProcess, if the game has not started yet, it will return HasNotStarted, if there is
   * no valid mov exist in this board, it will return Blocked.
   *
   * @return the status that represents the current state of the game
   */
  Status getStatus();

  /**
   * Represents the status for a game of Reversi.
   */
  enum Status {

    /**
     * The game has ended by 2 consecutive passes.
     */
    END,

    /**
     * InProgress = the game has started and there are still legal moves on the board (at least
     * one legal move for black or for white).
     */
    InProgress,

    /**
     * HasNotStarted = startGame() has not been called yet.
     */
    HasNotStarted,

    /**
     * Blocked = no valid moves, but the game is not over yet since the game has no passed.
     */
    BLOCKED
  }
}
