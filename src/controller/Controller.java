package controller;

import java.util.Optional;

import model.ModelStatus;
import model.MutableReversiModel;
import model.Player;
import model.RepresentativeColor;
import model.RowColPair;
import view.Features;
import view.GraphicView;

/**
 * Represents an asynchronous controller for a game of reversi. Controllers are event-driven
 * and respond to whatever event happens at a given time. The job of GUIControllers are to
 * control the flow of Reversi Games by determining when and how to update the model, as well
 * as how and when to update the GUI view that they use.
 */
public final class Controller implements Features {
  private final MutableReversiModel model;
  private final GraphicView graphicView;
  private final Player player;
  private final Manager<Controller> manager;
  private final ModelStatus status;

  /**
   * Construct the controller with given parameters.
   *
   * @param model the current model
   * @param graphicView the current view
   * @param player the player that will interact with this controller
   * @param cm the manager of this controller
   */
  public Controller(MutableReversiModel model, GraphicView graphicView, Player player,
                    Manager<Controller> cm, ModelStatus status) {
    this.model = model;
    this.graphicView = graphicView;
    graphicView.addFeatures(this);
    graphicView.display();
    this.player = player;
    boolean yourTurn = model.getTurn() == player.getColor();
    if (model.getTurn() != null) {
      graphicView.toggleTurn(model.getTurn(), yourTurn);
    }
    manager = cm;
    if (player.isAiPlayer()) {
      graphicView.lockMouseForNonHumanPlayer();
    }
    this.status = status;
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
      graphicView.setGameOverState(model.getWinner(), win);
      return;
    }
    graphicView.setColor(player.getColor());
    boolean yourTurn = model.getTurn() == player.getColor();
    graphicView.toggleTurn(model.getTurn(), yourTurn);
    graphicView.setHasToPassWarning(hasToPass, yourTurn);
  }

  /**
   * If the current player is an Ai player, let it try to find next move and execute it.
   */
  public void tryToPlace() {
    if (status.getStatus() == ModelStatus.Status.END) {
      return;
    }
    if (!player.isAiPlayer()) {
      return;
    }
    if (model.getTurn() == player.getColor()) {
      if (status.getStatus() == ModelStatus.Status.BLOCKED) {
        makePass();
        return;
      }
      try {
        Optional<RowColPair> pair = player.chooseNextMove(model);
        if (pair.isEmpty()) {
          return;
        }
        placeMove(pair.get());
      } catch (IllegalStateException ignored) {
        graphicView.showStates("Some thing wrong with the Ai strategy");
      }
    }
  }

  @Override
  public void placeMove(RowColPair pair) {
    try {
      if (status.getStatus() == ModelStatus.Status.END) {
        return;
      }
      // if the human player does not select anything and press enter to attempt a placing
      // notify the player.
      if (pair == null) {
        graphicView.showStates("Your are not selecting anything");
        return;
      }
      if (player.getColor() != model.getTurn()) {
        return;
      }
      if (status.getStatus() == ModelStatus.Status.BLOCKED) {
        graphicView.showStates("No valid move, can only choose to pass " + model.getTurn());
        return;
      }
      model.placeMove(pair, player.getColor());
      graphicView.resetSelectedPosition();
      graphicView.update(model);
      manager.update(model);
    } catch (IllegalStateException | IllegalArgumentException e) {
      graphicView.showStates("Can not place here " + model.getTurn());
    }
  }

  @Override
  public void makePass() {
    try {
      if (status.getStatus() == ModelStatus.Status.END) {
        return;
      }
      if (player.getColor() != model.getTurn()) {
        return;
      }
      graphicView.resetSelectedPosition();
      model.makePass(player.getColor());
      if (status.getStatus() == ModelStatus.Status.END) {
        graphicView.showStates("Game is over " + "Winner is " + model.getWinner());
      }
      graphicView.update(model);
      manager.update(model);
    } catch (IllegalStateException e) {
      graphicView.showStates("Can not do thing right now " + model.getTurn());
    }
  }

  @Override
  public void showHints() {
    graphicView.showHints(player.getColor());
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

  // model -> updated controller -> view
}
