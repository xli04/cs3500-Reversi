package controller;

import java.util.ArrayList;
import java.util.List;

import model.ModelListener;
import model.RepresentativeColor;

/**
 * A ControllerListeners class represents a model listener of the controller, used to register as
 * a listener in the model.
 */
public class ControllerListeners implements ModelListener {
  List<Controller> controllers;

  /**
   * construct the ControllerListeners.
   */
  public ControllerListeners() {
    controllers = new ArrayList<>();
  }

  /**
   * Register the controller to this list of listeners, so that they will be
   * notified once the model was changed.
   *
   * @param controller the controller that need to be registered in.
   */
  public void register(Controller controller) {
    if (controllers.contains(controller)) {
      throw new IllegalArgumentException("Already added");
    }
    controllers.add(controller);
  }

 /**
  * At the beginning of the game, assign the color for the players that the first player
  * join the game is placing black cell and the second player is placing white cell.
  * During the game, update the current game state to all the controller that shared the
  * same model.
  */
  @Override
  public void update() {
    if (controllers.size() != 2) {
      throw new IllegalStateException("Current game can only have two players");
    }
    if (controllers.get(0).checkPlayer().getColor() == null
      && controllers.get(1).checkPlayer().getColor() == null) {
      // first player join the game is placing black cell
      controllers.get(0).checkPlayer().assignColor(RepresentativeColor.BLACK);
      // second player is placing white cell
      controllers.get(1).checkPlayer().assignColor(RepresentativeColor.WHITE);
    }
    for (Controller c : controllers) {
      c.update();
    }
    for (Controller c : controllers) {
      c.tryToPlace();
    }
  }
}
