package adapter;

import reversi.provider.model.CellPosition;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import reversi.provider.view.ViewFeatures;
import model.Player;
import model.RepresentativeColor;

/**
 * ProviderController represents an adapter pattern with provider's ViewFeatures with our
 * controller so that we don't need to modify our controller.
 */
public class ProviderFeatures implements ViewFeatures {
  private final ReversiModel model;
  private final Player player;
  /**
   * Construct the controller with given parameters.
   *
   * @param model  the current model
   * @param player the player that will interact with this controller
   */
  public ProviderFeatures(ReversiModel model, Player player) {
    this.model = model;
    this.player = player;
  }

  @Override
  public void passTurn() {
    if (!model.isGameOver()) {
      model.passTurn();
    }
  }

  @Override
  public void playTurn(CellPosition cell) {
    if (!model.isGameOver()) {
      model.playTurn(cell);
    }
  }

  @Override
  public PlayerTurn controllerPlayer() {
    if (player.getColor() == RepresentativeColor.BLACK) {
      return PlayerTurn.BLACK;
    }
    return PlayerTurn.WHITE;
  }
}
