package strategy;

import java.util.Optional;
import model.MutableReversiModel;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * Represents a Reversi Strategy that applies alpha-beta pruning to increase the efficiency
 * of the minimax approach. Alpha-beta strategies are fallible - they may or may not determine
 * that a best move exists for the given board.
 */
public final class AlphaBetaStrategy extends AbstractStrategy implements FallibleStrategy {
  private final int defaultDepth = 5; //constant to represent the maximum depth

  //we will be diving before evaluating a board and deciding the best move.
  @Override
  public Optional<RowColPair> choosePosition(ReadOnlyReversiModel model,
                                             RepresentativeColor player) {
    Optional<Move> move = getMyMove(model.getDeepCopy(player), defaultDepth, -Double.MAX_VALUE,
            Double.MAX_VALUE, player);
    return move.isEmpty() ? Optional.empty() : Optional.of(move.get().getPosition());
  }

  /**
   * Simulate best move for the current player's color.
   *
   * @param model the model to make to copy and try different moves on
   * @param depth the number of levels to descend in the game tree before evaluating a position.
   * @param alpha the best value for max in our game tree,starts at a very large negative number
   * @param beta  the  best value for min in our game tree, starts at a very large positive number
   * @param turn  the player whose turn it is on the current board
   * @return an Optional move. We will return the best move if it exists,
   *          otherwise an empty optional.
   */
  private Optional<Move> getMyMove(MutableReversiModel model, int depth, double alpha,
                                   double beta, RepresentativeColor turn) {
    if (depth < 0) {
      throw new IllegalArgumentException();
    }
    if (depth == 0 || model.isGameOver()) {
      //if we have reached our max depth or the game is over...
      //Return a move containing the evaluation of the current baord.
      return Optional.of(new Move(getValue(model, turn)));
    }

    Move bestMove = null;
    double value = Integer.MIN_VALUE; //represents how good a move is for us
    for (RowColPair position : findAvailablePosition(model, turn).keySet()) {
      if (alpha > beta) {
        //if the best max value (alpha) ever exceeds the best min value (beta), we can prune
        break;
      }
      MutableReversiModel newModel = model.getDeepCopy(turn);
      newModel.placeMove(position, turn);
      double childValue = getOpponentValue(newModel, depth - 1, alpha, beta,
              turn.getOpposite());
      //if we find a better move than our current best, or don't have a best move yet...
      if (bestMove == null || childValue > value) {
        value = childValue;
        bestMove = new Move(position, childValue);
        alpha = Math.max(alpha, value);
      }
      //update our alpha our alpha and beta
      if (childValue > alpha) {
        alpha = childValue;
      }
      if (childValue < beta) {
        beta = childValue;
      }
    }
    return bestMove == null ? Optional.empty() : Optional.of(bestMove);
  }

  /**
   * Simulate the possible for current player's opposite color.
   *
   * @param model the model to make to copy and try different moves on
   * @param depth the number of levels to descend in the game tree before evaluating a position.
   * @param alpha the best value for max in our game tree,starts at a very large negative number
   * @param beta  the  best value for min in our game tree, starts at a very large positive number
   * @param color  the player whose turn it is on the current board
   * @return a double represents the potential affect caused by the simulated move
   */

  //Estimates the value of the given position for the given color.
  private double getOpponentValue(MutableReversiModel model, int depth, double alpha,
                                  double beta, RepresentativeColor color) {
    if (depth < 0) {
      throw new IllegalArgumentException();
    }
    if (depth == 0 || model.isGameOver()) {
      return getValue(model, color.getOpposite());
    }
    double value = Double.MAX_VALUE;
    for (RowColPair position : findAvailablePosition(model, color).keySet()) {
      MutableReversiModel newModel = model.getDeepCopy(color);
      if (newModel.getTurn() != color) {
        newModel.makePass(newModel.getTurn());
      }
      newModel.placeMove(position, color);
      Optional<Move> childMove = getMyMove(newModel, depth - 1, alpha, beta,
              color.getOpposite());
      if (childMove.isEmpty()) {
        throw new IllegalStateException("Illegal board encountered when trying to find child move");
      }
      double childValue = childMove.get().getValue();
      value = Math.min(value, childValue);
      beta = Math.min(beta, value);
      alpha = Math.max(alpha, value);
    }
    //if there are no available positions, value will equal double.MAX_VALUE. If this is the case,
    //the value should really be the evaluation of the baord
    return value == Double.MAX_VALUE ? getValue(model, color) : value;
  }

}
