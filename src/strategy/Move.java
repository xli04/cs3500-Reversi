package strategy;

import model.RowColPair;

/**
 * Represents an evulation of a board state for a player for a game of Reversi. A move can be
 * expressed as a coordinate on the board onto which a player could place their tile, WITH
 * an evaluation of how beneficial the move is to the player that makes it, or simply as an
 * evaluation of how beneficial a given board state is.
 */
public final class Move {
  private final RowColPair pair;
  private final double value;

  /**
   * Constructs a new move with the given coordinate and the given evaulation of that move.
   *
   * @param pair  the 0-indexed row-col pair of the board for which the player to make their move.
   * @param value the numerical evaluation of how good the board state would be after the move for
   *              the player that makes the move.
   */
  public Move(RowColPair pair, double value) {
    if (pair == null) {
      throw new IllegalArgumentException("Unable to construct to a null coordinate");
    }
    this.pair = pair;
    this.value = value;
  }

  /**
   * Constructs a new move with the given evaluation of the board state.
   *
   * @param value the numerical evaluation of how good the resulting board state is if the player
   *              whose turn it is were to make the move.
   */
  public Move(double value) {
    this.pair = null;
    this.value = value;
  }

  /**
   * Retrieves the coordinate location of the current move if it exits. Otherwise, throws
   * an IllegalArgumentException.
   *
   * @return a copy of the coordinate location of the current move.
   * @throws IllegalArgumentException since the position would be unavilable
   */
  public RowColPair getPosition() {
    if (pair == null) {
      throw new IllegalArgumentException("Unable to retreive position for a move "
        + "without a coordinate. ");
    }
    return new RowColPair(pair.getRow(), pair.getCol());
  }

  /**
   * Returns the evaluation of the baord state of this move.
   *
   * @return the evaluation of the board state, expressed as a double.
   */
  public double getValue() {
    return this.value;
  }

}
