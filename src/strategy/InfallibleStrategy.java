package strategy;

import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * Reperesents a complete strategy that cannot fail on an in-progress legal model in a
 * legal state. Returns a valid row col pair for the game they are called on, or throws
 * an exception if the game they are called on has no valid moves.
 */
public interface InfallibleStrategy {

  /**
   * get the position based on the current strategy rules that can maximize the benefit, which can
   * never be failed by return null or empty.
   *
   * @param model the current model
   * @param player the current player that need to choose next move
   * @return the position
   * @throws IllegalStateException if there is no valid move
   */
  RowColPair choosePosition(ReadOnlyReversiModel model, RepresentativeColor player);
}
