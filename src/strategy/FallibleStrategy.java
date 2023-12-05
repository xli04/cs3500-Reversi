package strategy;

import java.util.Optional;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * Represents strategies that are not guaranteed to always have a move for in-progress games.
 * Fallible strategies are incomplete, they may successfully choose a position and also may not.
 */
public interface FallibleStrategy {

  /**
   * get the position based on the current strategy rules that can maximize the benefit. which
   * may be failed by can not find a valid position to place.
   *
   * @param model the current model
   * @param player the current player that need to choose next move
   * @return the position
   */
  Optional<RowColPair> choosePosition(ReadOnlyReversiModel model, RepresentativeColor player);
}
