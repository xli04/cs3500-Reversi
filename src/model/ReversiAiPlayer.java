package model;

import java.util.Objects;
import java.util.Optional;
import strategy.InfallibleStrategy;

/**
 * A ReversiPlayer interface, represents ai player in the reversi game.
 */
public final class ReversiAiPlayer implements Player {
  private RepresentativeColor color;
  private final InfallibleStrategy strategy;

  /**
   * Construct the current player with the strategy the player will use for choose next move.
   *
   * @param strategy the strategy
   */
  public ReversiAiPlayer(InfallibleStrategy strategy) {
    Objects.requireNonNull(strategy);
    this.strategy = strategy;
    color = null;
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

  @Override
  public Optional<RowColPair> chooseNextMove(ReadOnlyReversiModel model) {
    return Optional.ofNullable(strategy.choosePosition(model, color));
  }

  @Override
  public RepresentativeColor getColor() {
    return color;
  }

  @Override
  public boolean isAiPlayer() {
    return true;
  }
}

