package adapter.strategy;

import java.util.Optional;

import adapter.ProviderTranslator;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import reversi.provider.model.CellPosition;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.strategy.Corners;
import strategy.FallibleStrategy;

/**
 * ProviderCornersStrategy represents an adapter pattern with provider's
 * Corners with our strategy interface.
 */
public class ProviderCornersStrategy implements FallibleStrategy {
  private final Corners strategy;
  private final ReversiModel model;

  public ProviderCornersStrategy(ReversiModel model) {
    this.strategy = new Corners();
    this.model = model;
  }


  @Override
  public Optional<RowColPair> choosePosition(ReadOnlyReversiModel model,
                                             RepresentativeColor player) {
    PlayerTurn turn = player == RepresentativeColor.WHITE ? PlayerTurn.WHITE : PlayerTurn.BLACK;
    Optional<CellPosition> cell = strategy.chooseCellPosition(this.model, turn);
    if (cell.isPresent()) { //if the strategy returned a cell to move to...
      //Get all of the counts of tiles that it would flip in each direction
      int cellsFlipped = ProviderTranslator.countMoves(model, cell.get());
      if (cellsFlipped == 0) {
        return Optional.empty();
      }
    }
    //if the strategy did not return a cell to move to, get the position from the longest path
    // strategy
    if (cell.isEmpty()) {
      ProviderLongestPathStrategy strategy = new ProviderLongestPathStrategy(this.model);
      return strategy.choosePosition(model, player);
    }
    return cell.map(ProviderTranslator::convertToRowColPair);
  }

}
