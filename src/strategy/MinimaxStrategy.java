package strategy;

import java.util.Optional;
import model.MutableReversiModel;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * A player that chooses the highest-scoring move based on the assumption
 * that the opponent will always choose the lowest-scoring move (each opponent will try to
 * minimize the success of the other player).
 */
public final class MinimaxStrategy extends AbstractStrategy implements FallibleStrategy {
  /**
   * The maximum depth of the search tree from the current position.
   */
  private static final int MAX_DEPTH = 5;

  @Override
  public Optional<RowColPair> choosePosition(ReadOnlyReversiModel model,
                                                RepresentativeColor player) {
    Move move = getMyMove(model.getDeepCopy(player), MAX_DEPTH, player);
    if (move == null) {
      return Optional.empty();
    }
    try {
      Thread.sleep(500);
    } catch (InterruptedException ignored) {
      if (move == null) {
        return Optional.empty();
      }
    }
    return Optional.of(move.getPosition());
  }

  /**
   * Simulate best move for the current player's color.
   *
   * @param model the model to make to copy and try different moves on
   * @param depth the number of levels to descend in the game tree before evaluating a position.
   * @param player the player whose turn it is on the current board
   * @return n Optional move. We will return the best move if it exists,
   *         otherwise an empty optional.
   */
  private Move getMyMove(MutableReversiModel model, int depth, RepresentativeColor player) {
    if (depth < 0) {
      throw new IllegalArgumentException();
    }
    if (depth == 0 || model.isGameOver()) {
      return new Move(getValue(model, player));
    }
    Move bestMove = null;
    for (RowColPair position : findAvailablePosition(model, player).keySet()) {
      MutableReversiModel newModel = model.getDeepCopy(player);
      if (newModel.getTurn() != player) {
        newModel.makePass(newModel.getTurn());
      }
      newModel.placeMove(position, player);
      double childValue = getOpponentValue(newModel, depth - 1, player.getOpposite());
      childValue /= 2;
      if (bestMove == null || childValue > bestMove.getValue()) {
        bestMove = new Move(position, childValue);
      }
    }
    return bestMove;
  }

  /**
   * Simulate the possible for current player's opposite color.
   *
   * @param model the model to make to copy and try different moves on
   * @param depth the number of levels to descend in the game tree before evaluating a position.
   * @param player  the player whose turn it is on the current board
   * @return a double represents the potential affect caused by the simulated move
   */
  private double getOpponentValue(MutableReversiModel model, int depth,
                                 RepresentativeColor player) {
    if (depth < 0) {
      throw new IllegalArgumentException();
    }
    if (depth == 0 || model.isGameOver()) {
      return getValue(model, player.getOpposite());
    }
    double minValue = Integer.MAX_VALUE;
    for (RowColPair position : findAvailablePosition(model, player).keySet()) {
      MutableReversiModel childModel = model.getDeepCopy(player);
      if (childModel.getTurn() != player) {
        childModel.makePass(childModel.getTurn());
      }
      childModel.placeMove(position, player);
      Move childMove = getMyMove(childModel, depth - 1, player.getOpposite());
      if (childMove != null && childMove.getValue() < minValue) {
        minValue = childMove.getValue();
      }
    }
    return minValue;
  }
}
