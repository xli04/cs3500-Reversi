package adapter.strategy;

import java.util.Map;
import java.util.Optional;

import adapter.ProviderTranslator;
import model.Direction;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import reversi.provider.model.CellPosition;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.strategy.LongestPath;
import strategy.FallibleStrategy;

/**
 * ProviderLongestPathStrategy represents an adapter pattern with provider's
 * LongestPath with our strategy interface.
 */
public class ProviderLongestPathStrategy implements FallibleStrategy {
  private final LongestPath strategy;
  private final ReversiModel model;

  public ProviderLongestPathStrategy(ReversiModel model) {
    this.strategy = new LongestPath();
    this.model = model;
  }

  @Override
  public Optional<RowColPair> choosePosition(ReadOnlyReversiModel model,
                                             RepresentativeColor player) {
    PlayerTurn turn = player == RepresentativeColor.WHITE ? PlayerTurn.WHITE : PlayerTurn.BLACK;
    Optional<CellPosition> cell = strategy.chooseCellPosition(this.model, turn);
    if (cell.isPresent()) { //if the strategy returned a cell to move to...
      int cellsFlipped = ProviderTranslator.countMoves(model,cell.get());
      if (cellsFlipped == 0) {
        return Optional.empty();
      }
    }
    return cell.map(ProviderTranslator::convertToRowColPair);
  }
}
