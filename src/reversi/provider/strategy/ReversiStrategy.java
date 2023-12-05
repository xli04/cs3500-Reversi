package reversi.provider.strategy;

import java.util.Optional;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.model.CellPosition;

/**
 * A Strategy interface for choosing where to play next for the given player.
 */

public interface ReversiStrategy {

  /**
   * Chooses a position on the board based on their strategy.
   *
   * @param model copy of the Reversi model to be played on
   * @param player player that is going to be played
   * @return an optional CellPosition of whether the strategy has a move or not
   */
  Optional<CellPosition> chooseCellPosition(ReversiModel model, PlayerTurn player);

}
