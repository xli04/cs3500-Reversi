package adapter;

import reversi.provider.controller.Player;
import reversi.provider.model.CellPosition;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import model.CubeCoordinateTrio;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.ReversiHumanPlayer;
import model.RowColPair;

/**
 * ProviderHumanPlayer represents an adapter pattern on provider's ReversiHumanPlayer logic
 * with our player interface.
 */
public class ProviderHumanPlayer extends ReversiHumanPlayer implements Player {

  @Override
  public CellPosition takeTurn(ReversiModel model) {
    return null;
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
    return false;
  }
}
