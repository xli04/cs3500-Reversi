package reversi.provider.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import reversi.provider.model.GamePiece;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.model.CellPosition;

/**
 * Strategy 2: Avoiding the places next to corners.
 */
public class AvoidingNextToCorners implements ReversiStrategy {

  @Override
  public Optional<CellPosition> chooseCellPosition(ReversiModel model, PlayerTurn player) {
    Map<CellPosition, GamePiece> board = model.getBoard();
    Map<CellPosition, GamePiece> messyBoard = model.getBoard();
    List<CellPosition> longestPaths = new ArrayList<CellPosition>();
    CellPosition corner = new CellPosition(-1, -1, -1);
    int index = model.getBoardWidth() - 2;

    for (int i = 0; i < board.size(); i++) {
      int longestLength = 0;
      CellPosition longestPath = new CellPosition(-1, -1, -1);
      for (CellPosition cell : messyBoard.keySet()) {
        if (board.get(cell) == GamePiece.EMPTY && model.pathLength(cell) > longestLength) {
          longestLength = model.pathLength(cell);
          longestPath = cell;
        }
      }
      if (!longestPath.equals(new CellPosition(-1, -1, -1))) {
        longestPaths.add(longestPath);
      }
      messyBoard.remove(longestPath);
    }


    for (CellPosition cell : longestPaths) {
      corner = new CellPosition(-1, -1, -1);
      corner = getCorner(model, cell, index, corner, cell.getX(), cell.getY(), board);
      corner = getCorner(model, cell, index, corner, cell.getX(), cell.getZ(), board);
      corner = getCorner(model, cell, index, corner, cell.getY(), cell.getZ(), board);
      if (!corner.equals(new CellPosition(0, 0, 0))) {
        return Optional.of(corner);
      }
    }

    if (corner.equals(new CellPosition(-1, -1, -1))) {
      return Optional.empty();
    } else {
      return Optional.of(corner);
    }
  }

  private CellPosition getCorner(ReversiModel model, CellPosition cell, int index,
                                 CellPosition corner, int one, int two, Map<CellPosition,
      GamePiece> board) {
    if ((Math.abs(one) == index && Math.abs(two) == index)
        || (Math.abs(one) == (index + 1) && Math.abs(two) == index)
        || (Math.abs(one) == index && Math.abs(two) == (index + 1))) {
      return new CellPosition(0, 0, 0);
    } else if (corner.equals(new CellPosition(0, 0, 0))) {
      return corner;
    }
    return cell;
  }
}
