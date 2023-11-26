package model;

import strategy.InfallibleStrategy;

/**
 * A ReversiPlayer interface, represents ai player in the reversi game.
 */
public final class ReversiPlayer implements Player {
  private final RepresentativeColor color;
  private final InfallibleStrategy strategy;
  private PlayerType type;

  /**
   * Construct the current player with the color this player represent and the strategy the
   * player will use for choose next move, if the strategy is null. this means this player is
   * a human player.
   *
   * @param color    the color
   * @param strategy the strategy
   */
  public ReversiPlayer(RepresentativeColor color, InfallibleStrategy strategy) {
    this.color = color;
    this.strategy = strategy;
    if (strategy == null) {
      type = PlayerType.Human;
    } else {
      type = PlayerType.Ai;
    }
  }

  @Override
  public RowColPair chooseNextMove(ReadOnlyReversiModel model) {
    if (strategy == null) {
      return null;
    }
    return strategy.choosePosition(model, color);
  }

  @Override
  public RepresentativeColor getColor() {
    return color;
  }

  @Override
  public boolean isAiPlayer() {
    return type == PlayerType.Ai;
  }

  /**
   * Represents a player type relative to a player in the game. A regular reversi should
   * only has two types of player, ai or human.
   */
  protected enum PlayerType {Human, Ai}
}

