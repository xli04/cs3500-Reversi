package adapter.strategy;

import java.util.Map;
import java.util.Optional;
import reversi.provider.model.CellPosition;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.strategy.AvoidingNextToCorners;
import model.Direction;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
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
    if (cell.isPresent()) {
      Map<Direction, Integer> move = model.checkMove(convert(cell.get()), model.getTurn());
      int value = 0;
      for (int i : move.values()) {
        value += i;
      }
      if (value == 0) {
        return Optional.empty();
      } else {
        RowColPair pair = convert(cell.get());
        return Optional.of(pair);
      }
    }
    return Optional.empty();
  }

  private RowColPair convert(CellPosition position) {
    return new RowColPair(-position.getX(), position.getZ());
  }
}
