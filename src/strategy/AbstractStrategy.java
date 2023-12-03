package strategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.CubeCoordinateTrio;
import model.Direction;
import model.Hexagon;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

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
    Map<RowColPair, Hexagon> map = model.getCurrentBoard();
    for (RowColPair pair : map.keySet()) {
      try {
        Map<Direction, Integer> directionToFlippedCardCount = model.checkMove(pair, color);
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
    return Arrays.asList(new RowColPair(-(model.getSize() - 1), 0),
      new RowColPair(-(model.getSize() - 1), (model.getSize() - 1)),
      new RowColPair((model.getSize() - 1), -(model.getSize() - 1)),
      new RowColPair((model.getSize() - 1), 0),
      new RowColPair(0, model.getSize() - 1),
      new RowColPair(0, -(model.getSize() - 1)));
  }


  /**
   * Check if the position is near corner at this turn.
   *
   * @param pair the position that want to be placed
   * @param cornerPoints the coordinators of corner position in the board
   * @return the fixed value, since placing at the corner may produce potential disadvantages
   *        by letting opposite color occupied the corner.
   */
  protected boolean isNextToCorner(RowColPair pair, List<RowColPair> cornerPoints) {
    CubeCoordinateTrio cube = pair.convertToCube();
    for (Direction direction : Direction.values()) {
      RowColPair newPosition = new CubeCoordinateTrio(cube.getRow() + direction.getRowOffset(),
          cube.getLeftCol() + direction.getLeftColOffset(),
          cube.getRightCol() + direction.getRightColOffset()).convertToRowCol();
      if (cornerPoints.contains(newPosition)) {
        return true;
      }
    }
    return false;
  }
}