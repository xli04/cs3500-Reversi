import adapter.AdapterModel;
import adapter.ProviderViewFeatures;
import adapter.AdapterGraphicView;
import controller.Controller;
import controller.ControllerListeners;
import model.ModelStatus;
import model.MutableReversiModel;
import model.Player;
import model.RegularReversiModel;
import model.ReversiAiPlayer;
import model.ReversiHumanPlayer;
import model.ReversiModelStatus;
import reversi.provider.view.BasicReversiView;
import strategy.InfallibleStrategy;
import view.IView;
import view.ReversiGraphicView;

/**
 * Main launching point for the reversi Game. User can choose the type of players in the
 * reversi game by themselves by entering two additional strings after type the
 * java -jar NameOfJARFile.jar. ex. java -jar NameOfJARFile.jar human human is means
 * two human players against each other Human is for the human player, and for the ai players,
 * we offer four different difficulty of ai strategy to let the users choose, easy is
 * our most simple strategy, it will only looking for the position that can capture most
 * piece, medium is based on the easy strategy and it will also prefer to take the corner
 * positions first. For the medium plus, it not only has the behavior for the medium strategy,
 * it also AvoidCellsNextToCorners Finally, for the hard level, this strategy is minimax ,
 * which means it will simulate the action and then take the best action. We also support the
 * strategy from our provider's code, for the providereasy, this strategy is easiest strategy
 * from the provider's strategies. for the provider medium, this strategy is combination of
 * corner strategy and the strategy that find the position that can get the highest points.
 * for the provider hard, this strategy is the combination of CornersStrategy and the strategy
 * that AvoidingNextToCorners.
 */
public class ReversiGame {
  /**
   * entry point for this game.
   *
   * @param args the default constructor
   */
  public static void main(String[] args) {
    ModelStatus status = new ReversiModelStatus();
    MutableReversiModel model = new RegularReversiModel.ModelBuilder().setStatus(status).build();
    AdapterModel adapt = new AdapterModel(model);
    Player player1 = new ReversiHumanPlayer();
    Player player2 = new ReversiHumanPlayer();
    DifficultyCreator creator = new DifficultyCreator(adapt);
    if (args.length != 2) {
      throw new IllegalArgumentException("Reversi game must has two players");
    }
    for (int i = 0; i < args.length; i++) {
      Player player = new ReversiHumanPlayer();
      String type = args[i].toUpperCase();
      if (type.equals("HUMAN")) {
        if (i == 0) {
          player = new ReversiHumanPlayer();
        } else {
          player = new ReversiHumanPlayer();
        }
      } else {
        try {
          InfallibleStrategy strategy = creator.getDifficultyCorrespondingStrategy(type);
          player = new ReversiAiPlayer(strategy);
        } catch (IllegalArgumentException e) {
          throw new IllegalArgumentException("No such type of game player supported");
        }
      }
      // since the args should always contains two string, the first one is for player1,
      // the second is for player2.
      if (i == 0) {
        player1 = player;
      } else {
        player2 = player;
      }
    }
    IView view = new ReversiGraphicView(model);
    IView view2 = new AdapterGraphicView(new BasicReversiView(adapt),
        new ProviderViewFeatures(adapt, player2));
    Controller controller = new Controller(model, view, player1, status);
    Controller controller2 = new Controller(model, view2, player2, status);
    ControllerListeners listeners = new ControllerListeners();
    listeners.register(controller);
    listeners.register(controller2);
    model.addListener(listeners);
    model.startGame();
  }


}
