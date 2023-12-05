package adapter.strategy;

import java.util.Map;
import java.util.Optional;
import reversi.provider.model.CellPosition;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.strategy.Corners;
import model.Direction;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
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
    if (cell.isPresent()) {
      Map<Direction, Integer> move = model.checkMove(convert(cell.get()), model.getTurn());
      int value = 0;
      for (int i : move.values()) {
        value += i;
      }
      if (value == 0) {
        return Optional.empty();
      }
    }
    if (cell.isEmpty()) {
      ProviderLongestPathStrategy strategy =
          new ProviderLongestPathStrategy(this.model);
      return strategy.choosePosition(model, player);
    }
    return cell.map(this::convert);
  }

  private RowColPair convert(CellPosition position) {
    return new RowColPair(-position.getX(), position.getZ());
  }

}
