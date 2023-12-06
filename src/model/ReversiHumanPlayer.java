package model;

import java.util.Optional;

/**
 * A ReversiPlayer interface, represents Human player in the reversi game.
 */
public final class ReversiHumanPlayer implements Player {

  private RepresentativeColor color;

  /**
   * Construct the current human player, since human players can choose their moves by their own
   * no parameters needed.
   **/
  public ReversiHumanPlayer() {
    color = null;
  }

  /**
   * Our controller will not ask a human player to choose the next move, but we think using the
   * optional is better since our controller do not need to handle the null value.
   *
   * @param model the current model
   * @return the empty value in optional
   */
  @Override
  public Optional<RowColPair> chooseNextMove(ReadOnlyReversiModel model) {
    return Optional.empty();
  }

  @Override
  public RepresentativeColor getColor() {
    return color;
  }

  @Override
  public boolean isAiPlayer() {
    return false;
  }

  /**
   * assign the color to the players that which color they will play for in this
   * standard reversi game.
   *
   * @param color the given color
   * @throws IllegalStateException player's color can not modified during the game
   *         once it was signed
   */
  @Override
  public void assignColor(RepresentativeColor color) {
    if (this.color != null) {
      throw new IllegalStateException("Can not chang the color during the game");
    }
    this.color = color;
  }
}
