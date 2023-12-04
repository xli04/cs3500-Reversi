package adapter.strategy;

import java.util.Map;
import java.util.Optional;
import reversi.provider.model.CellPosition;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.strategy.FallbackStrategy;
import reversi.provider.strategy.ReversiStrategy;
import model.Direction;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import strategy.FallibleStrategy;

/**
 * ProviderFallBackStrategy represents an adapter pattern with provider's
 * FallbackStrategy with our strategy interface.
 */
public class ProviderFallBackStrategy extends FallbackStrategy implements FallibleStrategy {

  /**
   * Constructs a fallback strategy AI.
   *
   * @param firstStrategy  the first strategy to be played
   * @param secondStrategy to second strategy to be played if the first one fails
   */
  public ProviderFallBackStrategy(ReversiStrategy firstStrategy, ReversiStrategy secondStrategy) {
    super(firstStrategy, secondStrategy);
  }

  @Override
  public Optional<RowColPair> choosePosition(ReadOnlyReversiModel model,
                                             RepresentativeColor player) {
    PlayerTurn turn = player == RepresentativeColor.WHITE ? PlayerTurn.WHITE : PlayerTurn.BLACK;
    CellPosition cell;
    try {
      cell = super.chooseCellPosition((ReversiModel) model, turn);
    } catch (IllegalStateException e) {
      return Optional.empty();
    }
    RowColPair pair = convert(cell);
    Map<Direction, Integer> move = model.checkMove(pair, model.getTurn());
    int value = 0;
    for (int i : move.values()) {
      value += i;
    }
    if (value == 0) {
      ProviderLongestPathStrategy strategy = new ProviderLongestPathStrategy();
      return strategy.choosePosition(model, player);
    }
    return Optional.of(convert(cell));
  }

  private RowColPair convert(CellPosition position) {
    return new RowColPair(-position.getX(), position.getZ());
  }
}
