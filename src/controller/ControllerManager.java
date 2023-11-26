package controller;

import java.util.ArrayList;
import java.util.List;

import model.ReadOnlyReversiModel;

/**
 * A ControllerManager raises events to all the controllers. Controllers can register (subscribe)
 * to be managed by a controller manager. The job of a controller manager is to call-back all
 * controllers that are subscribed,  whenever an intereseting event occurs (such as a move is
 * made on the model), and to have each controller update the corresponding view that the
 * controller controls.
 */
public final class ControllerManager implements Manager<GUIController> {
  private final List<GUIController> controllers;

  /**
   * Construct the current ControllerManager.
   */
  public ControllerManager() {
    this.controllers = new ArrayList<>();
  }

  /**
   * Register for current manager.
   *
   * @param c the controller that wants to register
   */
  @Override
  public void register(GUIController c) {
    controllers.add(c);
  }

  /**
   * update the current game state to all the controller that shared the same model.
   *
   * @param model if there exist valid move in the current game board
   */
  @Override
  public void update(ReadOnlyReversiModel model) {
    for (GUIController c : controllers) {
      boolean over = model.isGameOver();
      if (over) {
        c.update(false, true);
      } else {
        c.update(model.hasToPass(), false);
      }

    }
    for (GUIController c : controllers) {
      c.tryToPlace();
    }
  }
}
