import java.util.Arrays;
import controller.Controller;
import controller.ControllerManager;
import model.ModelStatus;
import model.MutableReversiModel;
import model.Player;
import model.RegularReversiModel;
import model.ReversiAiPlayer;
import model.ReversiHumanPlayer;
import model.ReversiModelStatus;
import strategy.CompleteStrategy;
import strategy.MinimaxStrategy;
import view.ReversiGraphicView;
import view.ViewManager;

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
    ViewManager manager = new ViewManager();
    ControllerManager cm = new ControllerManager();
    ModelStatus status = new ReversiModelStatus();
    MutableReversiModel model = new RegularReversiModel(Arrays.asList(cm, manager), status);
    Player aiPlayer1 = new ReversiAiPlayer(new CompleteStrategy(new MinimaxStrategy()));
    Player humanPlayer1 = new ReversiHumanPlayer();
    ReversiGraphicView view = new ReversiGraphicView(model, manager);
    Player aiPlayer2 = new ReversiAiPlayer(new CompleteStrategy(new MinimaxStrategy()));
    Player humanPlayer2 = new ReversiHumanPlayer();
    ReversiGraphicView view2 = new ReversiGraphicView(model, manager);
    Controller controller = new Controller(model, view, humanPlayer1, cm, status);
    Controller controller2 = new Controller(model, view2, humanPlayer2, cm, status);
    model.startGame();
  }
}
