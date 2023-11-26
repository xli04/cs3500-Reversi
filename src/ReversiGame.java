import java.util.Arrays;
import controller.GUIController;
import controller.ControllerManager;
import model.MutableReversiModel;
import model.Player;
import model.RegularReversiModel;
import model.RepresentativeColor;
import model.ReversiPlayer;
import strategy.CompleteStrategy;
import strategy.MinimaxStrategy;
import view.ReversiGUIView;
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
    Player player = new ReversiPlayer(RepresentativeColor.BLACK,
        new CompleteStrategy(new MinimaxStrategy()));
    Player player3 = new ReversiPlayer(RepresentativeColor.BLACK, null);
    ReversiGUIView view = new ReversiGUIView(model, manager);
    Player player2 = new ReversiPlayer(RepresentativeColor.WHITE,
        new CompleteStrategy(new MinimaxStrategy()));
    Player player4 = new ReversiPlayer(RepresentativeColor.WHITE, null);
    ReversiGUIView view2 = new ReversiGUIView(model, manager);
    GUIController GUIController = new GUIController(model, view, player3, cm);
    GUIController controller2 = new GUIController(model, view2, player4, cm);
    model.startGame();
    // who go first,
    // controller should not know the strategy
    // once turn changes, notify all the observers, startGame() generate the first notification.
  }
}
