import controller.Controller;
import controller.ControllerListeners;
import model.ModelStatus;
import model.Player;
import model.RegularReversiModel;
import model.ReversiAiPlayer;
import model.ReversiHumanPlayer;
import model.ReversiModelStatus;
import strategy.CompleteStrategy;
import strategy.MinimaxStrategy;
import view.ReversiGraphicView;

/**
 * Main launching point for the reversi Game.
 */
public class ReversiGame {
  /**
   * entry point for this game.
   *
   * @param args the default constructor
   */
  public static void main(String[] args) {
    ModelStatus status = new ReversiModelStatus();
    RegularReversiModel model = new RegularReversiModel(status);
    Player aiPlayer1 = new ReversiAiPlayer(new CompleteStrategy(new MinimaxStrategy()));
    Player humanPlayer1 = new ReversiHumanPlayer();
    ReversiGraphicView view = new ReversiGraphicView(model);
    Player aiPlayer2 = new ReversiAiPlayer(new CompleteStrategy(new MinimaxStrategy()));
    Player humanPlayer2 = new ReversiHumanPlayer();
    ReversiGraphicView view2 = new ReversiGraphicView(model);
    Controller controller = new Controller(model, view, humanPlayer1, status);
    Controller controller2 = new Controller(model, view2, humanPlayer2, status);
    ControllerListeners listeners = new ControllerListeners();
    listeners.register(controller);
    listeners.register(controller2);
    model.addListener(listeners);
    model.startGame();
  }
}
