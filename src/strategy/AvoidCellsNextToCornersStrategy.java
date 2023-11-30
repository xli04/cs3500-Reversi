package strategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * A strategy that avoids the cells next to corners. This strategy choose any cell that is not
 * next to a corner, if such a cell exists. Otherwise, it does not make a choice and returns
 * an empty optional position. If there are multiple non-corner cells that work,
 * break ties by choosing the first move not next to a corner that we find.
 */
public final class AvoidCellsNextToCornersStrategy extends AbstractStrategy implements
        FallibleStrategy {

  @Override
  public Optional<RowColPair> choosePosition(ReadOnlyReversiModel model,
                                             RepresentativeColor player) {
    List<RowColPair> cornerPoints = getCornerPoints(model);
    Map<RowColPair, Integer> pairs = findAvailablePosition(model, player);
    RowColPair pair = null;
    int value = -1;
    for (RowColPair position : pairs.keySet()) {
      if (!(isNextToCorner(position, cornerPoints))) {
        if (pair == null) {
          pair = position;
          value = pairs.get(position);
        } else {
          if (value < pairs.get(position)) {
            pair = position;
            value = pairs.get(position);
          }
        }
      }
    }
    if (pair == null) {
      return Optional.empty();
    }
    return Optional.of(pair);
  }
}
