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
   * no valid mov exist in this board, it will return NoValidMove.
   *
   * @param model the model that need to check the status
   * @return the status that represents the current state of the game
   */
  status getStatus(ReadOnlyReversiModel model);

  /**
   * Represents the status that the game might be.
   */
  enum status{END,InProcess,HasNotStarted,NoValidMove}
}
