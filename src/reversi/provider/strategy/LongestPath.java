package reversi.provider.strategy;

import java.util.Map;
import java.util.Optional;

import reversi.provider.model.GamePiece;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.model.CellPosition;

/**
 * First Strategy: Find the spot where the most pieces will be captured, if there are multiple
 * then it will go in the top-leftmost spot.
 */
public class LongestPath implements ReversiStrategy {
  @Override
  public Optional<CellPosition> chooseCellPosition(ReversiModel model, PlayerTurn player) {
    Map<CellPosition, GamePiece> board = model.getBoard();
    int longestLength = 0;
    CellPosition longestPath = new CellPosition(-1, -1, -1);
    for (CellPosition cell : board.keySet()) {
      if (board.get(cell) == GamePiece.EMPTY && model.pathLength(cell) > longestLength) {
        longestLength = model.pathLength(cell);
        longestPath = cell;
      }
    }
    if (longestPath.equals(new CellPosition(-1, -1, -1))) {
      return Optional.empty();
    } else {
      return Optional.of(longestPath);
    }
  }
}
