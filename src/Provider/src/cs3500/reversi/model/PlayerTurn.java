package Provider.src.cs3500.reversi.model;

/**
 * Represents the possible player turns, the only ones are either Black or White.
 */
public enum PlayerTurn {
  BLACK,
  WHITE;

  /**
   * Returns a string equivalent to the player turn.
   *
   * @return a string equivalent to the player turn
   */
  public String toString() {
    if (this.equals(PlayerTurn.BLACK)) {
      return "black";
    } else {
      return "white";
    }
  }
}
