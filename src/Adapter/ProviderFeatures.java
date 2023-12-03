package Adapter;

import Provider.src.cs3500.reversi.model.CellPosition;
import Provider.src.cs3500.reversi.model.PlayerTurn;
import Provider.src.cs3500.reversi.model.ReversiModel;
import Provider.src.cs3500.reversi.view.ViewFeatures;
import model.Player;
import model.RepresentativeColor;
import model.RowColPair;
import view.Features;

public class ProviderFeatures implements ViewFeatures {
  private final ReversiModel model;
  private final Player player;

  public ProviderFeatures(ReversiModel model, Player player) {
    this.model = model;
    this.player = player;
  }

  @Override
  public void passTurn() {
    model.passTurn();
  }

  @Override
  public void playTurn(CellPosition cell) {
    model.playTurn(cell);
  }

  @Override
  public PlayerTurn controllerPlayer() {
    if (player.getColor() == RepresentativeColor.WHITE) {
      return PlayerTurn.WHITE;
    } else if (player.getColor() == RepresentativeColor.BLACK) {
      return PlayerTurn.BLACK;
    }
    return null;
  }
}
