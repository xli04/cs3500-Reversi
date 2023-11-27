import java.util.Arrays;
import controller.Controller;
import controller.ControllerManager;
import model.MutableReversiModel;
import model.Player;
import model.RegularReversiModel;
import model.ReversiPlayer;
import strategy.CompleteStrategy;
import strategy.MinimaxStrategy;
import view.RegularReversiView;
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
    MutableReversiModel model = new RegularReversiModel(Arrays.asList(cm, manager));
    Player aiPlayer1 = new ReversiPlayer(new CompleteStrategy(new MinimaxStrategy()));
    Player humanPlayer1 = new ReversiPlayer(null);
    RegularReversiView view = new RegularReversiView(model, manager);
    Player aiPlayer2 = new ReversiPlayer(new CompleteStrategy(new MinimaxStrategy()));
    Player humanPlayer2 = new ReversiPlayer(null);
    RegularReversiView view2 = new RegularReversiView(model, manager);
    Controller controller = new Controller(model, view, humanPlayer1, cm);
    Controller controller2 = new Controller(model, view2, aiPlayer2, cm);
    model.startGame();
  }
}
