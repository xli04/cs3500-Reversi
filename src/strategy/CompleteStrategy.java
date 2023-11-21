package strategy;

import java.util.Optional;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * A complete strategy represents a kinds of strategy that will never fail by never
 * return null or empty when choosing positions.
 */
public class CompleteStrategy implements InfallibleStrategy {
  private final FallibleStrategy strategy;

  /**
   * construct the completeStrategy with the provided FallibleStrategy strategy.
   *
   * @param strategy given strategy
   */
  public CompleteStrategy(FallibleStrategy strategy) {
    this.strategy = strategy;
  }

  @Override
  public RowColPair choosePosition(ReadOnlyReversiModel model, RepresentativeColor player) {
    Optional<RowColPair> position = strategy.choosePosition(model, player);
    if (position.isPresent()) {
      return position.get();
    }
    throw new IllegalStateException("No valid move exist");
  }
}
