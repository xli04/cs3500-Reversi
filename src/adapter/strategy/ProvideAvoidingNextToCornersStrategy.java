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
import reversi.provider.strategy.AvoidingNextToCorners;
import strategy.FallibleStrategy;

/**
 * ProvideAvoidingNextToCornersStrategy represents an adapter pattern with provider's
 * AvoidingNextToCorners with our strategy interface.
 */
public class ProvideAvoidingNextToCornersStrategy implements FallibleStrategy {
  private final AvoidingNextToCorners strategy;
  private final ReversiModel model;

  public ProvideAvoidingNextToCornersStrategy(ReversiModel model) {
    this.strategy = new AvoidingNextToCorners();
    this.model = model;
  }

  @Override
  public Optional<RowColPair> choosePosition(ReadOnlyReversiModel model,
                                             RepresentativeColor player) {
    PlayerTurn turn = player == RepresentativeColor.WHITE ? PlayerTurn.WHITE : PlayerTurn.BLACK;
    Optional<CellPosition> cell = strategy.chooseCellPosition(this.model, turn);
    if (cell.isPresent()) { //if the strategy returned a cell to move to...
      //Get all of the counts of tiles that it would flip in each direction
      int cellsFlipped = ProviderTranslator.countMoves(model,cell.get());
      if (cellsFlipped> 0) { //if the move flips any tiles...
        RowColPair pair = ProviderTranslator.convertToRowColPair(cell.get());
        return Optional.of(pair);
      }
    }
    //if the strategy does not pick a cell to move to, or the cell flips 0 tiles, return an
    //empty optional
    return Optional.empty();
  }
}
