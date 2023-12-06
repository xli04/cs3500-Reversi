package adapter;

import java.util.Map;

import model.CubeCoordinateTrio;
import model.Direction;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import reversi.provider.model.CellPosition;
import reversi.provider.model.GamePiece;

/**
 * Represents a utility class to translate between our Reversi codebase and our providers' Reversi
 * codebase
 */
public final class ProviderTranslator {
  // Coordinate converters

  /**
   * Convert from provider's coordinators system to our coordinator system by reversing their
   * x as the coordinator to represent our row and their z to represent our col.
   *
   * @param position the position in provider's coordinator system
   * @return the position in our coordinator system
   */
  public static RowColPair convertToRowColPair(CellPosition position) {
    return new RowColPair(-position.getX(), position.getZ());
  }

  /**
   * Convert from our coordinators system to provider's coordinator system by reversing the
   * row int cubetrio system as their x, reversing right col as their y, using left col as
   * their z.
   *
   * @param pair the position in our coordinator system
   * @return the position in provider's coordinator system
   */
  public static CellPosition convertToCellPosition(RowColPair pair) {
    CubeCoordinateTrio trio = pair.convertToCube();
    return new CellPosition(-trio.getRow(), -trio.getRightCol(), trio.getLeftCol());
  }

  //Color converters

  /**
   * Convert from our color system (Representative color) to our provider's color system
   *
   * @param color the representative color to be converted
   * @return the converted color, as a GamePiece color.
   */
  public static GamePiece convertColorToProviderColor(RepresentativeColor color) {
    switch (color) {
      case BLACK:
        return GamePiece.BLACK;
      case WHITE:
        return GamePiece.WHITE;
      case NONE:
        return GamePiece.EMPTY;
      default:
        throw new IllegalStateException("Unable to convert from cyan to provider color, no " +
                "such equivalent exists");
    }
  }

  /**
   * Counts the number of cells that a give move flips in all directions on the given model.
   *
   * @param model the model to check the moves on
   * @param cell  the desintation cell for the potential move
   * @return the amount of cells that would be flipped if the move were made on the model .
   */
  public static int countMoves(ReadOnlyReversiModel model, CellPosition cell) {
    Map<Direction, Integer> moveMap = model.checkMove(
            ProviderTranslator.convertToRowColPair(cell), model.getTurn());
    int cellsFlipped = 0;
    for (int i : moveMap.values()) {
      cellsFlipped += i;
    }
    return cellsFlipped;
  }

}