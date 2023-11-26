package controller;

import java.util.ArrayList;
import java.util.List;

import model.ReadOnlyReversiModel;

/**
 * A ControllerManager raises events to all the controllers. The job of a controller manager is
 * to notify all the controllers whenever an interesting event happens on the model, for example
 * whenever the model is updated.
 */
public final class ControllerManager implements Manager<Controller> {
  private final List<Controller> controllers;

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
  public void register(Controller c) {
    controllers.add(c);
  }

  /**
   * update the current game state to all the controller that shared the same model.
   *
   * @param model if there exist valid move in the current game board
   */
  @Override
  public void update(ReadOnlyReversiModel model) {
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
