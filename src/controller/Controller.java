package controller;

import model.MutableReversiModel;
import model.Player;
import model.RepresentativeColor;
import model.RowColPair;
import view.Features;
import view.ReversiView;

/**
 * Represents an asynchronous controller for a game of reversi. Controllers are event-driven
 * and respond to whatever event happens at a given time. The job of GUIControllers are to
 * control the flow of Reversi Games by determining when and how to update the model, as well
 * as how and when to update the GUI view that they use.
 */
public final class Controller implements Features {
  private final MutableReversiModel model;
  private final ReversiView reversiView;
  private final Player player;
  private final Manager<Controller> manager;

  /**
   * Construct the controller with given parameters.
   *
   * @param model the current model
   * @param reversiView the current view
   * @param player the player that will interact with this controller
   * @param cm the manager of this controller
   */
  public Controller(MutableReversiModel model, ReversiView reversiView, Player player,
                    Manager<Controller> cm) {
    this.model = model;
    this.reversiView = reversiView;
    reversiView.addFeatures(this);
    reversiView.display();
    this.player = player;
    boolean yourTurn = model.getTurn() == player.getColor();
    if (model.getTurn() != null) {
      reversiView.toggleTurn(model.getTurn(), yourTurn);
    }
    manager = cm;
    if (player.isAiPlayer()) {
      reversiView.lockMouseForNonHumanPlayer();
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
      boolean win = winner == player.getColor();
      reversiView.setGameOverState(model.getWinner(), win);
      return;
    }
    reversiView.setColor(player.getColor());
    boolean yourTurn = model.getTurn() == player.getColor();
    reversiView.toggleTurn(model.getTurn(), yourTurn);
    reversiView.setHasToPassWarning(hasToPass, yourTurn);
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
        reversiView.showStates("Some thing wrong with the Ai strategy");
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
        reversiView.showStates("No valid move, can only choose to pass " + model.getTurn());
        return;
      }
      model.placeMove(pair, player.getColor());
      reversiView.resetSelectedPosition();
      reversiView.update(model);
      manager.update(model);
    } catch (IllegalStateException | IllegalArgumentException e) {
      reversiView.showStates("Can not place here " + model.getTurn());
    }
  }

  @Override
  public void makePass() {
    try {
      if (player.getColor() != model.getTurn()) {
        return;
      }
      reversiView.resetSelectedPosition();
      model.makePass(player.getColor());
      if (model.isGameOver()) {
        reversiView.showStates("Game is over " + "Winner is " + model.getWinner());
      }
      reversiView.update(model);
      manager.update(model);
    } catch (IllegalStateException e) {
      reversiView.showStates("Can not do thing right now " + model.getTurn());
    }
  }

  @Override
  public void showHints() {
    reversiView.showHints(player.getColor());
  }

  /**
   * Get the player in this controller, used to let the manager assign the color
   * to different player.
   *
   * @return the player in this controller
   */
  public Player checkPlayer() {
    return player;
  }
}
