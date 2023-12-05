package reversi.provider.strategy;

import java.util.Map;
import java.util.Optional;
import reversi.provider.model.GamePiece;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.model.CellPosition;

/**
 * Third Strategy: Checks each of the corners to see if the currents player can play there.
 */
public class Corners implements ReversiStrategy {
  @Override
  public Optional<CellPosition> chooseCellPosition(ReversiModel model, PlayerTurn player) {
    Map<CellPosition, GamePiece> board = model.getBoard();
    CellPosition corner = new CellPosition(-1, -1, -1);
    int index = model.getBoardWidth() - 1;
    for (CellPosition cell : board.keySet()) {
      corner = getCorner(model, cell, index, corner, cell.getX(), cell.getY(), board);
      corner = getCorner(model, cell, index, corner, cell.getX(), cell.getZ(), board);
      corner = getCorner(model, cell, index, corner, cell.getY(), cell.getZ(), board);
    }
    if (corner.equals(new CellPosition(-1, -1, -1))) {
      return Optional.empty();
    } else {
      return Optional.of(corner);
    }
  }

  // checks each corner if there is a valid path to be played.
  private CellPosition getCorner(ReversiModel model, CellPosition cell, int index,
                                 CellPosition corner, int one, int two, Map<CellPosition,
      GamePiece> board) {
    if (Math.abs(one) == index && Math.abs(two) == index) {
      if (model.pathLength(cell) != 0 && board.get(cell) == GamePiece.EMPTY) {
        corner = cell;
      }
    }
    return corner;
  }
}