package reversi.provider.strategy;

import java.util.Optional;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.model.CellPosition;

/**
 * Allows the AI to play one strategy first, and if it fails, calls another strategy.
 */

public class FallbackStrategy implements FinalReversiStrategy {

  ReversiStrategy firstStrategy;
  ReversiStrategy secondStrategy;

  /**
   * Constructs a fallback strategy AI.
   *
   * @param firstStrategy the first strategy to be played
   * @param secondStrategy to second strategy to be played if the first one fails
   */

  public FallbackStrategy(ReversiStrategy firstStrategy, ReversiStrategy secondStrategy) {
    this.firstStrategy = firstStrategy;
    this.secondStrategy = secondStrategy;
  }

  @Override
  public CellPosition chooseCellPosition(ReversiModel model, PlayerTurn player) {
    Optional<CellPosition> first = this.firstStrategy.chooseCellPosition(model, player);
    Optional<CellPosition> second = this.secondStrategy.chooseCellPosition(model, player);
    if (first.isPresent()) {
      return first.get();
    } else if (second.isPresent()) {
      return second.get();
    } else {
      throw new IllegalStateException("There are no possible moves chosen by these strategies.");
    }

  }
}
