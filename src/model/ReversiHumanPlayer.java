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

  @Override
  public void assignColor(RepresentativeColor color) {
    this.color = color;
  }
}
