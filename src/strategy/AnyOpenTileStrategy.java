package strategy;

import java.util.Map;
import java.util.Optional;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * Represents an infallible strategy that chooses the first open tile that is on the board.
 * This strategy should always return have a selection for legal, in-progress games.
 */
public final class AnyOpenTileStrategy extends AbstractStrategy implements FallibleStrategy {

  @Override
  public Optional<RowColPair> choosePosition(ReadOnlyReversiModel model,
                                             RepresentativeColor player) throws
          IllegalStateException {
    Map<RowColPair, Integer> positions = findAvailablePosition(model, player);
    for (RowColPair position : positions.keySet()) {
      if (positions.get(position) > 0) {
        return Optional.ofNullable(position);
      }
    }
    return Optional.empty();
  }
}
