package controller;

import model.MutableReversiModel;
import model.Player;
import model.RepresentativeColor;
import model.RowColPair;
import view.Features;
import view.RegularReversiReversiView;

/**
 * A controller represents the features in this game, user will interact with controller.
 */
public final class Controller implements Features {
  private final MutableReversiModel model;
  private final RegularReversiReversiView view;
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
  public Controller(MutableReversiModel model, RegularReversiReversiView view, Player player,
                    ControllerManager cm) {
    this.model = model;
    this.view = view;
    this.player = player;
    this.manager = cm;

    view.setColor(player.getColor());
    view.addFeatures(this); //add this controller as an observer to the view
    view.display(); //render the initial game state

    boolean playerTurn = (model.getTurn() == player.getColor());
    if (model.getTurn() != null) {
      view.toggleTurn(model.getTurn(), playerTurn);
    }
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
      RepresentativeColor winner = model.getWinner();
      boolean win = (winner == player.getColor());
      view.setGameOverState(model.getWinner(), win);
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
