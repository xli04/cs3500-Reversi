package model;

import strategy.InfallibleStrategy;

/**
 * A ReversiPlayer interface, represents ai player in the reversi game.
 */
public final class ReversiPlayer implements Player {
  private RepresentativeColor color;
  private final InfallibleStrategy strategy;
  private final PlayerType type;

  /**
   * Construct the current player with the color this player represent and the strategy the
   * player will use for choose next move, if the strategy is null. this means this player is
   * a human player.
   *
   * @param strategy the strategy
   */
  public ReversiPlayer(InfallibleStrategy strategy) {
    this.strategy = strategy;
    if (strategy == null) {
      type = PlayerType.Human;
    } else {
      type = PlayerType.Ai;
    }
  }

  /**
   * assign the color to the players that which color they will play for in this
   * standard reversi game.
   *
   * @param color the given color
   * @throws IllegalStateException player's color can not modified during the game
   *         once it was signed
   */
  public void assignColor(RepresentativeColor color) {
    if (this.color != null) {
      throw new IllegalStateException("Can not chang the color during the game");
    }
    this.color = color;
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
  protected enum PlayerType { Human, Ai }
}

