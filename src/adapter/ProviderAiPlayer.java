package adapter;

import reversi.provider.controller.Player;
import reversi.provider.model.CellPosition;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import model.CubeCoordinateTrio;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.ReversiAiPlayer;
import model.RowColPair;
import strategy.InfallibleStrategy;

/**
 * ProviderAiPlayer represents an adapter pattern on provider's ReversiAiPlayer logic
 * with our player interface.
 */
public class ProviderAiPlayer extends ReversiAiPlayer implements Player {

  /**
   * Construct the current player with the strategy the player will use for choose next move.
   *
   * @param strategy the strategy
   */
  public ProviderAiPlayer(InfallibleStrategy strategy) {
    super(strategy);
  }

  @Override
  public CellPosition takeTurn(ReversiModel model) {
    return convertBack(super.chooseNextMove((ReadOnlyReversiModel) model).get());
  }

  @Override
  public PlayerTurn getPlayer() {
    if (super.getColor() == RepresentativeColor.BLACK) {
      return PlayerTurn.BLACK;
    }
    return PlayerTurn.WHITE;
  }

  @Override
  public boolean isAi() {
    return true;
  }

  private CellPosition convertBack(RowColPair pair) {
    CubeCoordinateTrio trio = pair.convertToCube();
    return new CellPosition(-trio.getRow(), -trio.getRightCol(), trio.getLeftCol());
  }
}
