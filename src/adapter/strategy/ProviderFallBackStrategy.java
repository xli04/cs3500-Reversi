package adapter.strategy;

import java.util.Optional;

import adapter.ProviderTranslator;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import reversi.provider.model.CellPosition;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.strategy.FallbackStrategy;
import strategy.FallibleStrategy;

/**
 * ProviderFallBackStrategy represents an adapter pattern with provider's
 * FallbackStrategy with our strategy interface.
 */
public class ProviderFallBackStrategy implements FallibleStrategy {

  private final FallbackStrategy strategy;
  private final ReversiModel model;


  /**
   * Constructs a fallback strategy AI.
   *
   * @param strategy the first strategy to be played
   */
  public ProviderFallBackStrategy(FallbackStrategy strategy, ReversiModel model) {
    this.strategy = strategy;
    this.model = model;
  }


  @Override
  public Optional<RowColPair> choosePosition(ReadOnlyReversiModel model,
                                             RepresentativeColor player) {
    PlayerTurn turn = player == RepresentativeColor.WHITE ? PlayerTurn.WHITE : PlayerTurn.BLACK;
    CellPosition cell;
    try { //if the fallback strategy fails, return an empty optional
      cell = strategy.chooseCellPosition(this.model, turn);
    } catch (IllegalStateException e) {
      return Optional.empty();
    }
    int cellsFlipped = ProviderTranslator.countMoves(model, cell);
    if (cellsFlipped == 0) { //if this pair would flip no cells, default to the longest path strategy
      ProviderLongestPathStrategy strategy =
              new ProviderLongestPathStrategy(this.model);
      return strategy.choosePosition(model, player);
    }
    return Optional.of(ProviderTranslator.convertToRowColPair(cell));
  }
}
