package Provider.src.cs3500.reversi.model;

/**
 * Represents all the possible pieces for a cell to have in a ReversiModel, and empty marks a cell
 * with no pieces.
 */
public enum GamePiece {
  BLACK,
  WHITE,
  EMPTY;

  /**
   * Returns a string equivalent to the game piece.
   *
   * @return a string equivalent to the game piece
   */
  public String toString() {
    switch (this) {
      case BLACK:
        return "black";
      case WHITE:
        return "white";
      default:
        return "empty";
    }
  }

}