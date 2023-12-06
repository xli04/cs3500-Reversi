package model;

/**
 * A ReversiModelStatus class represents the current status for the game model, once the player
 * placed a move or made a pass or the start the game, the status of the game will be updated
 * in order to reflect the most recent status.
 */
public final class ReversiModelStatus implements ModelStatus {
  private Status status;

  /**
   * Construct the ReversiModelStatus and set the initial state to HasNotStarted.
   */
  public ReversiModelStatus() {
    status = Status.HasNotStarted;
  }

  @Override
  public void updateStatus(ReadOnlyReversiModel model) {
    if (model.isGameOver()) {
      status = Status.END;
      return;
    }
    if (model.hasToPass()) {
      status = Status.BLOCKED;
      return;
    }
    status = Status.InProgress;
  }

  @Override
  public Status getStatus() {
    return status;
  }
}
