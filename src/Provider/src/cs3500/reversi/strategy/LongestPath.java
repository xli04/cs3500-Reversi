package Provider.src.cs3500.reversi.strategy;

import java.util.Map;
import java.util.Optional;

import Provider.src.cs3500.reversi.model.GamePiece;
import Provider.src.cs3500.reversi.model.PlayerTurn;
import Provider.src.cs3500.reversi.model.ReversiModel;
import Provider.src.cs3500.reversi.model.CellPosition;

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
    }
    else {
      return Optional.of(longestPath);
    }
  }
}
