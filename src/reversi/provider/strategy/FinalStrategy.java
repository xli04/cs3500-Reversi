package reversi.provider.strategy;

import java.util.Optional;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.model.CellPosition;

/**
 * Creates the AI as an infallible method that returns a CellPosition or throws an exception.
 */
public class FinalStrategy implements FinalReversiStrategy {

  ReversiStrategy strategyToTry;

  /**
   * Constructs a FinalStrategy that will work as an AI.
   *
   * @param strategyToTry a strategy to be used.
   */
  public FinalStrategy(ReversiStrategy strategyToTry) {
    this.strategyToTry = strategyToTry;
  }

  @Override
  public CellPosition chooseCellPosition(ReversiModel model, PlayerTurn player) {
    Optional<CellPosition> maybeAns = this.strategyToTry.chooseCellPosition(model, player);
    if (maybeAns.isPresent()) {
      return maybeAns.get();
    }
    throw new IllegalStateException("There are no possible moves chosen by this strategy");
  }
}
