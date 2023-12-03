package Provider.src.cs3500.reversi.strategy;

import java.util.Optional;

import Provider.src.cs3500.reversi.model.PlayerTurn;
import Provider.src.cs3500.reversi.model.ReversiModel;
import Provider.src.cs3500.reversi.model.CellPosition;

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
