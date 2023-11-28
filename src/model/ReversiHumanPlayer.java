package model;

import java.util.Optional;

/**
 * A ReversiPlayer interface, represents Human player in the reversi game.
 */
public class ReversiHumanPlayer implements Player {

  private RepresentativeColor color;

  /**
   * Construct the current human player, since human players can choose their moves by their own
   * no parameters needed.
   **/
  public ReversiHumanPlayer() {
    color = null;
  }

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
