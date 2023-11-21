package strategy;

import java.util.Optional;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * Represents a comprehensive strategy that is composed of two different components: a fallible
 * strategy and an fallible strategy. Composite strategies default to trying their fallible
 * strategies as their first option. If the fallible strategies finds a move to make, this
 * strategy selects that move. Otherwise, the strategy will use its second fallible strategy as a
 * backup. Due to this, Composite strategies can be composed of fallible strategies with other
 * composite strategies, allowing for flexible and advanced strategies to be created.
 */
public final class CompositeStrategy implements FallibleStrategy {
  private final FallibleStrategy fallibleStrategy;
  private final FallibleStrategy backUpStrategy;

  /**
   * Constructs a new Composite strategy, consisting of the given fallible and the given fallible
   * strategies.
   *
   * @param fallibleStrategy   the first-option strategy to default to selecting a move from.
   * @param backUpStrategy   the fall-back, backup strategy to select a move from if the fallible
   *                           strategy does not decide on a move.
   */
  public CompositeStrategy(FallibleStrategy fallibleStrategy,
                           FallibleStrategy backUpStrategy) {
    this.fallibleStrategy = fallibleStrategy;
    this.backUpStrategy = backUpStrategy;
  }

  @Override
  public Optional<RowColPair> choosePosition(ReadOnlyReversiModel model,
                                             RepresentativeColor player) {
    Optional<RowColPair> firstChoice =
        fallibleStrategy.choosePosition(model, player);
    if (firstChoice.isPresent()) {
      return firstChoice;
    }
    return backUpStrategy.choosePosition(model, player);
  }
}
