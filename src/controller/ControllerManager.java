package controller;

import java.util.ArrayList;
import java.util.List;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;

/**
 * A ControllerManager raises events to all the controllers. Controllers can register (subscribe)
 * to be managed by a controller manager. The job of a controller manager is to call-back all
 * controllers that are subscribed,  whenever an interesting event occurs (such as a move is
 * made on the model), and to have each controller update the corresponding view that the
 * controller controls.
 */
public class ControllerManager implements Manager<Controller> {
  private final List<Controller> controllers;

  /**
   * Construct the current ControllerManager.
   */
  public ControllerManager() {
    controllers = new ArrayList<>();
  }

  /**
   * Register for current manager. so that all the controllers that registered in can be notified
   * once an interesting event occurs (such as a move is made on the model).
   *
   * @param c the controller that wants to register
   */
  @Override
  public void register(Controller c) {
    controllers.add(c);
  }

  /**
   * At the beginning of the game, assign the color for the players where the first player
   * join the game is placing black cell and the second player is placing white cell.
   * During the game, update the current game state to all the controller that shared the
   * same model.
   *
   * @param model if there exist valid move in the current game board
   * @throws IllegalStateException if there are more than two players in the standard game
   */
  @Override
  public void update(ReadOnlyReversiModel model) {
    if (controllers.size() != 2) {
      throw new IllegalStateException("Current game can only have two players");
    }
    if (controllers.get(0).checkPlayer().getColor() == null) {
      controllers.get(0).checkPlayer().assignColor(RepresentativeColor.BLACK);
      controllers.get(1).checkPlayer().assignColor(RepresentativeColor.WHITE);
    }
    for (Controller c : controllers) {
      boolean over = model.isGameOver();
      if (over) {
        c.update(false, true);
      } else {
        c.update(model.hasToPass(), false);
      }
    }
    for (Controller c : controllers) {
      c.tryToPlace();
    }
  }
}
