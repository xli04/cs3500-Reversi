package strategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.Direction;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * A strategy that chooses cells in corners, since corner cells cannot be captured. This strategy
 * choose any cell that is not in a corner, if such a cell exists. Otherwise, it does not make a
 * choice and returns an empty optional position.
 **/
public final class CornerStrategy extends AbstractStrategy implements FallibleStrategy {

  @Override
  public Optional<RowColPair> choosePosition(ReadOnlyReversiModel model,
                                             RepresentativeColor player) {
    List<RowColPair> cornerPoints = getCornerPoints(model);
    for (RowColPair position : cornerPoints) {
      if (model.getColorAt(position) != RepresentativeColor.NONE) {
        continue;
      }
      Map<Direction, Integer> value = model.checkMove(position, player);
      int pointsCanGet = 0;
      for (Integer i : value.values()) {
        pointsCanGet += i;
      }
      if (pointsCanGet > 0) {
        return Optional.of(position);
      }
    }
    Map<RowColPair, Integer> pairs = findAvailablePosition(model, player);
    RowColPair pair = null;
    int value = -1;
    for (RowColPair position : pairs.keySet()) {
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
    if (pair != null) {
      return Optional.of(pair);
    }
    return Optional.empty();
  }

}
