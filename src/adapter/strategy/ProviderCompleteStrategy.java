package adapter.strategy;

import java.util.Map;
import reversi.provider.model.CellPosition;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.strategy.FinalStrategy;
import reversi.provider.strategy.ReversiStrategy;
import model.Direction;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import strategy.InfallibleStrategy;

/**
 * ProviderCompleteStrategy represents an adapter pattern with provider's FinalStrategy
 * with our strategy interface.
 */
public class ProviderCompleteStrategy extends FinalStrategy implements InfallibleStrategy {

  /**
   * Constructs a FinalStrategy that will work as an AI.
   *
   * @param strategyToTry a strategy to be used.
   */
  public ProviderCompleteStrategy(ReversiStrategy strategyToTry) {
    super(strategyToTry);
  }

  @Override
  public RowColPair choosePosition(ReadOnlyReversiModel model, RepresentativeColor player) {
    PlayerTurn turn = player == RepresentativeColor.WHITE ? PlayerTurn.WHITE : PlayerTurn.BLACK;
    CellPosition cell = super.chooseCellPosition((ReversiModel) model, turn);
    RowColPair pair = convert(cell);
    Map<Direction, Integer> move = model.checkMove(pair, model.getTurn());
    int value = 0;
    for (int i : move.values()) {
      value += i;
    }
    if (value == 0) {
      throw new IllegalStateException("There are no possible moves chosen by this strategy");
    }
    return pair;
  }

  private RowColPair convert(CellPosition position) {
    return new RowColPair(-position.getX(), position.getZ());
  }

}
