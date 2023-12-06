package adapter.strategy;

import adapter.ProviderTranslator;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import reversi.provider.model.CellPosition;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.strategy.FinalStrategy;
import strategy.InfallibleStrategy;

/**
 * ProviderCompleteStrategy represents an adapter pattern with provider's FinalStrategy
 * with our strategy interface.
 */
public class ProviderCompleteStrategy implements InfallibleStrategy {

  private final FinalStrategy strategy;
  private final ReversiModel model;

  /**
   * Constructs a FinalStrategy that will work as an AI.
   *
   * @param strategy a strategy to be used.
   */
  public ProviderCompleteStrategy(FinalStrategy strategy, ReversiModel model) {
    this.strategy = strategy;
    this.model = model;
  }

  @Override
  public RowColPair choosePosition(ReadOnlyReversiModel model, RepresentativeColor player) {
    PlayerTurn turn = player == RepresentativeColor.WHITE ? PlayerTurn.WHITE : PlayerTurn.BLACK;
    CellPosition cell = strategy.chooseCellPosition(this.model, turn);
    int cellsFlipped = ProviderTranslator.countMoves(model, cell);
    //complete strategies must return a move that flips at least one tile. If not, throw an ISE
    if (cellsFlipped == 0) {
      throw new IllegalStateException("There are no possible moves chosen by this strategy");
    }
    return ProviderTranslator.convertToRowColPair(cell);
  }

}
