package controller;

import model.MutableReversiModel;
import model.Player;
import model.RowColPair;
import view.Features;
import view.ReversiView;

/**
 * A controller represents the features in this game, user will interact with controller.
 */
public class Controller implements Features {
  private final MutableReversiModel model;
  private final ReversiView view;
  private final Player player;
  private final ControllerManager manager;

  /**
   * Construct the controller with given parameters.
   *
   * @param model the current model
   * @param view the current view
   * @param player the player that will interact with this controller
   * @param cm the manager of this controller
   */
  public Controller(MutableReversiModel model, ReversiView view, Player player,
                    ControllerManager cm) {
    this.model = model;
    this.view = view;
    view.setColor(player.getColor());
    view.addFeatures(this);
    view.display();
    this.player = player;
    boolean yourTurn = model.getTurn() == player.getColor();
    if (model.getTurn() != null) {
      view.toggleTurn(model.getTurn(), yourTurn);
    }
    manager = cm;
    if (player.isAiPlayer()) {
      view.lockMouseForNonHumanPlayer();
    }
    cm.register(this);
  }

  /**
   * Update the game state to all the view that shared the same model with current one,
   * if the current model do not has any valid move in the curretn turn, notify the player
   * that will play in next turn.
   *
   * @param hasToPass whether there exist valid move inside of the current model
   */
  public void update(boolean hasToPass, boolean gameOver) {
    if (gameOver) {
      view.setGameOverState(model.getWinner());
      return;
    }
    boolean yourTurn = model.getTurn() == player.getColor();
    view.toggleTurn(model.getTurn(), yourTurn);
    view.setHasToPassWarning(hasToPass, yourTurn);
  }

  /**
   * If the current player is an Ai player, let it try to find next move and execute it.
   */
  public void tryToPlace() {
    if (!player.isAiPlayer()) {
      return;
    }
    if (model.isGameOver()) {
      return;
    }
    if (model.getTurn() == player.getColor()) {
      if (model.hasToPass()) {
        makePass();
        return;
      }
      try {
        RowColPair pair = player.chooseNextMove(model);
        placeMove(pair);
      } catch (IllegalStateException ignored) {
        view.showStates("Some thing wrong with the Ai strategy");
      }
    }
  }

  @Override
  public void placeMove(RowColPair pair) {
    try {
      if (model.isGameOver()) {
        return;
      }
      if (player.getColor() != model.getTurn()) {
        return;
      }
      if (model.hasToPass()) {
        view.showStates("No valid move, can only choose to pass " + model.getTurn());
        return;
      }
      model.placeMove(pair, player.getColor());
      view.resetSelectedPosition();
      view.update(model);
      manager.update(model);
    } catch (IllegalStateException | IllegalArgumentException e) {
      view.showStates("Can not place here " + model.getTurn());
    }
  }

  @Override
  public void makePass() {
    try {
      if (player.getColor() != model.getTurn()) {
        return;
      }
      view.resetSelectedPosition();
      model.makePass(player.getColor());
      if (model.isGameOver()) {
        view.showStates("Game is over " + "Winner is " + model.getWinner());
      }
      view.update(model);
      manager.update(model);
    } catch (IllegalStateException e) {
      view.showStates("Can not do thing right now " + model.getTurn());
    }
  }

  @Override
  public void showHints() {
    view.showHints(player.getColor());
  }
}
