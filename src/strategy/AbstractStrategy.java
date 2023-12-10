package strategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.CubeCoordinateTrio;
import model.HexDirection;
import model.CellPiece;
import model.ModelDirection;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import model.SquareDirection;
import model.SquareReversiModel;

/**
 * Represents a strategy for a game of reversi. Abstract strategies are exclusively fallible
 * strategies (strategies that may or may not return a move for a given model), or  infallible
 * strategies (strategies that are guaranteed to return a move for a given model). Strategies
 * serve to help players to decide where to place their next move during a game of Reversi.
 */
public abstract class AbstractStrategy {
  /**
   * Evaluates a given board state for the given tile color. Positive values indicate
   * an advantageous position for the given color, negative values indicate a disadantageous
   * position for the given color, and values close to 0 indicate even (neither color has
   * an advantage) positions.
   *
   * @param model     the model whose board state we will evaluate
   * @param tileColor the tile color we will evaluate for.
   * @return the evaluation for the given color on the given board, expressed as a double.
   */
  protected double getValue(ReadOnlyReversiModel model, RepresentativeColor tileColor) {
    int difference = model.getScore(tileColor) - model.getScore(tileColor.getOpposite());
    if (model.isGameOver()) {
      return Integer.signum(difference) * model.getSize() * model.getSize();
    }
    return difference;
  }

  /**
   * Finds all the available positions (potential moves) for the given color.
   *
   * @param model the model whose board we will search for positions.
   * @param color the current color to search positions for.
   * @return a map where the key is position and value is the number of cells that can be flipped
   *        for a move of the given color for the given position.
   */
  protected Map<RowColPair, Integer> findAvailablePosition(ReadOnlyReversiModel model,
                                                           RepresentativeColor color) {
    Map<RowColPair, Integer> positionToFlippedCardCount = new HashMap<>();
    Map<RowColPair, CellPiece> map = model.getBoard();
    for (RowColPair pair : map.keySet()) {
      if (model.getColorAt(pair) != RepresentativeColor.NONE) {
        continue;
      }
      try {
        Map<ModelDirection, Integer> directionToFlippedCardCount = model.checkMove(pair, color);
        int numCardsThatCanBeFlipped = 0;
        for (int i : directionToFlippedCardCount.values()) {
          numCardsThatCanBeFlipped += i;
        }
        if (numCardsThatCanBeFlipped > 0) {
          positionToFlippedCardCount.put(pair, numCardsThatCanBeFlipped);
        }
      } catch (IllegalStateException | IllegalArgumentException exception) {
        continue;
      }
    }
    return positionToFlippedCardCount;
  }

  /**
   * get the position's coordinators by using current model's size.
   *
   * @param model the current model
   * @return a List contains the corner coordinators
   */
  protected List<RowColPair> getCornerPoints(ReadOnlyReversiModel model) {
    if (model.checkType() == ReadOnlyReversiModel.ModelType.SQUARE) {
      int convert = model.getSize() / 2 - 1;
      return Arrays.asList(new RowColPair(-convert, -convert),
        new RowColPair(- convert, - convert + model.getSize() - 1),
        new RowColPair(-convert + model.getSize() - 1, - convert),
        new RowColPair(-convert + model.getSize() - 1, -convert + model.getSize() - 1));
    } else if (model.checkType() == ReadOnlyReversiModel.ModelType.HEX) {
      return Arrays.asList(new RowColPair(-(model.getSize() - 1), 0),
        new RowColPair(-(model.getSize() - 1), (model.getSize() - 1)),
        new RowColPair((model.getSize() - 1), -(model.getSize() - 1)),
        new RowColPair((model.getSize() - 1), 0),
        new RowColPair(0, model.getSize() - 1),
        new RowColPair(0, -(model.getSize() - 1)));
    }
    throw new IllegalArgumentException("strategy can not applied on this model");
  }


  /**
   * Check if the position is near corner at this turn.
   *
   * @param pair the position that want to be placed
   * @param cornerPoints the coordinators of corner position in the board
   * @return the fixed value, since placing at the corner may produce potential disadvantages
   *        by letting opposite color occupied the corner.
   */
  protected boolean isNextToCorner(ReadOnlyReversiModel model,
                                   RowColPair pair, List<RowColPair> cornerPoints) {
    CubeCoordinateTrio cube = pair.convertToCube();
    ModelDirection[] directions = new ModelDirection[]{};
    if (model instanceof SquareReversiModel) {
      directions = SquareDirection.values();
    } else {
      directions = HexDirection.values();
    }
    for (ModelDirection hexDirection : directions) {
      RowColPair newPosition = new CubeCoordinateTrio(cube.getRow() + hexDirection.getRowOffset(),
          cube.getLeftCol() + hexDirection.getLeftColOffset(),
          cube.getRightCol() + hexDirection.getRightColOffset()).convertToRowCol();
      if (cornerPoints.contains(newPosition)) {
        return true;
      }
    }
    return false;
  }
}