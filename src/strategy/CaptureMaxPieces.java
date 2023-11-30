package strategy;

import java.util.Map;
import java.util.Optional;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * This strategy represents to put the cell in the position that can get the highest score.
 * If there are multiple moves that capture the same number of pieces, one of these moves
 * is chosen.
 */
public final class CaptureMaxPieces extends AbstractStrategy implements FallibleStrategy {
  @Override
  public Optional<RowColPair> choosePosition(ReadOnlyReversiModel model,
                                             RepresentativeColor player) {
    Map<RowColPair, Integer> map =
        findAvailablePosition(model, player);
    RowColPair leftTopMost = null; //we will prefer the upper-left-most pair to break ties
    int value = -1;
    for (RowColPair pair : map.keySet()) {
      int currentValue = map.get(pair);
      if (leftTopMost == null) {
        leftTopMost = pair;
        value = currentValue;
        continue;
      }
      if (currentValue > value) {
        leftTopMost = pair;
        value = currentValue;
      } else if (currentValue == value) {
        if (pair.getRow() <= leftTopMost.getRow()) {
          if (pair.getRow() < leftTopMost.getRow()) {
            leftTopMost = pair;
          } else {
            if (pair.getCol() < leftTopMost.getCol()) {
              leftTopMost = pair;
            }
          }
        }
      }
    }
    if (leftTopMost == null) {
      return Optional.empty();
    }
    return Optional.of(leftTopMost);
  }
}
