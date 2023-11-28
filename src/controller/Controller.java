package controller;

import java.util.Optional;
import model.ModelStatus;
import model.MutableReversiModel;
import model.Player;
import model.ReadOnlyReversiModel;
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
  private final ModelStatus status;

  /**
   * Construct the controller with given parameters.
   *
   * @param model the current model
   * @param graphicView the current view
   * @param player the player that will interact with this controller
   * @param status the status that represents the most recent states of game
   */
  public Controller(MutableReversiModel model, GraphicView graphicView, Player player,
                    ModelStatus status) {
    this.model = model;
    this.graphicView = graphicView;
    graphicView.addFeatures(this);
    graphicView.display();
    this.player = player;
    boolean yourTurn = model.getTurn() == player.getColor();
    if (model.getTurn() != null) {
      graphicView.toggleTurn(model.getTurn(), yourTurn);
    }
    if (player.isAiPlayer()) {
      graphicView.lockMouseForNonHumanPlayer();
    }
    this.status = status;
  }

  /**
   * Update the game state to all the view that shared the same model with current one,
   * if the current model do not has any valid move in the current turn, notify the player
   * that will play in next turn. If the game is over, update the view to show the game
   * over message and the winner in theis game.
   */
  public void update() {
    graphicView.resetPanel(model);
    if (status.getStatus() == ModelStatus.Status.END) {
      RepresentativeColor winner = model.getWinner();
      boolean win = winner == player.getColor();
      graphicView.setGameOverState(model.getWinner(), win);
      graphicView.showMessage("Game is over Winner is " + winner);
      return;
    }
    graphicView.resetSelectedPosition();
    graphicView.setColor(player.getColor());
    boolean yourTurn = model.getTurn() == player.getColor();
    graphicView.toggleTurn(model.getTurn(), yourTurn);
    graphicView.setHasToPassWarning(status.getStatus() == ModelStatus.Status.BLOCKED, yourTurn);
  }

  /**
   * If the current player is an Ai player, let it try to find next move and execute it or
   * make pass for it if there is no valid move exist in the game. Under this circumstance,
   * if we checked there exist valid move in this board but the strategy can not find it,
   * it can only be some thing wrong with the strategy.
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
        if (pair.isPresent()) {
          placeMove(pair.get());
        } else {
          graphicView.showMessage("Some thing wrong with the Ai strategy");
        }
      } catch (IllegalStateException e) {
        graphicView.showMessage("Some thing wrong with the Ai strategy");
      }
    }
  }

  /**
   * Place a cell in the given position, if the game over, the user can not interact with the
   * board anymore, or if the user can not take actions in this turn, it also should not take
   * an action if there are no valid move for the current player, notify the user and reject all
   * the commands except choose to pass. If the move is invalid by either can not place in the
   * given position or the human player does not select any position, generate a message to
   * notify the player. In the end, let the view manger and controller manager to update all
   * the views and controllers that care about the model.
   *
   * @param pair the given position
   */
  @Override
  public void placeMove(RowColPair pair) {
    try {
      if (status.getStatus() == ModelStatus.Status.END) {
        return;
      }
      if (player.getColor() != model.getTurn()) {
        return;
      }
      if (status.getStatus() == ModelStatus.Status.BLOCKED) {
        graphicView.showMessage("No valid move, can only choose to pass " + "Player: "
            + player.getColor());
        return;
      }
      model.placeMove(pair, player.getColor());
      graphicView.resetSelectedPosition();
    } catch (IllegalStateException | IllegalArgumentException e) {
      String position = "";
      // if the human player does not select anything and press enter to attempt a placing
      // notify the player.
      if (pair == null) {
        position = "Your are not selecting anything";
      } else {
        position = "(" + pair.getRow() + "," + pair.getCol() + ")";
      }
      graphicView.showMessage("Can not place here " + position + " " + " Player: "
          + model.getTurn());
    }
  }

  /**
   * Make pass in this turn, if the game over, the user can not interact with the
   * board anymore, or if the user can not take actions in this turn, After make a
   * pass, if the game is ended, generate a message to notify the user game is over
   * and the winner of the game. In the end, let the view manger and controller manager to
   * update all the views and controllers that care about the model.
   */
  @Override
  public void makePass() {
    try {
      if (status.getStatus() == ModelStatus.Status.END) {
        return;
      }
      if (player.getColor() != model.getTurn()) {
        return;
      }
      model.makePass(player.getColor());
      graphicView.resetSelectedPosition();
    } catch (IllegalStateException e) {
      graphicView.showMessage("Can not take action right now " + "Player: " + player.getColor());
    }
  }

  /**
   * Keep the human player away from bothering the ai player by clicking the hint button.
   */
  @Override
  public void showHints() {
    if (player.isAiPlayer()) {
      return;
    }
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
}
