import adapter.CombineModel;
import adapter.ProviderAiPlayer;
import adapter.ProviderFeatures;
import adapter.ProviderGraphicView;
import adapter.ProviderHumanPlayer;
import adapter.strategy.ProviderCornersStrategy;
import adapter.strategy.ProviderLongestPathStrategy;
import adapter.strategy.ProviderFallBackStrategy;
import adapter.strategy.ProviderCompleteStrategy;
import adapter.strategy.ProvideAvoidingNextToCornersStrategy;
import controller.Controller;
import controller.ControllerListeners;
import model.ModelStatus;
import model.Player;
import model.ReversiAiPlayer;
import model.ReversiHumanPlayer;
import model.ReversiModelStatus;
import strategy.AvoidCellsNextToCornersStrategy;
import strategy.CaptureMaxPieces;
import strategy.CompleteStrategy;
import strategy.CompositeStrategy;
import strategy.CornerStrategy;
import strategy.InfallibleStrategy;
import strategy.MinimaxStrategy;
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
 * which means it will simulate the action and then take the best action.
 */
public class ReversiGame {
  /**
   * entry point for this game.
   *
   * @param args the default constructor
   */
  public static void main(String[] args) {
    Player player1 = new ReversiHumanPlayer();
    Player player2 = new ProviderHumanPlayer();
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
          player = new ProviderHumanPlayer();
        }
      } else {
        try {
          InfallibleStrategy strategy = Difficulty.valueOf(type).getStrategy();
          if (i == 0) {
            player = new ReversiAiPlayer(strategy);
          } else {
            player = new ProviderAiPlayer(strategy);
          }
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
    ModelStatus status = new ReversiModelStatus();
    CombineModel model = new CombineModel.ModelBuilder().setSize(6).setStatus(status).build();
    IView view = new ReversiGraphicView(model);
    ProviderGraphicView view2 = new ProviderGraphicView(model, new ProviderFeatures(model, player2));
    Controller controller = new Controller(model, view, player1, status);
    Controller controller2 = new Controller(model, view2, player2, status);
    ControllerListeners listeners = new ControllerListeners();
    listeners.register(controller);
    listeners.register(controller2);
    model.addListener(listeners);
    model.startGame();
  }

  /**
   * Represent a difficult for the strategies.
   */
  public enum Difficulty {

    /**
     * our most simple strategy, it will only looking for the position that can capture most
     * * piece.
     */
    EASY(new CompleteStrategy(new CaptureMaxPieces())),

    /**
     * medium is based on the easy strategy and it will also prefer to take the corner
     * positions first.
     */
    MEDIUM(new CompleteStrategy(new CompositeStrategy(new CornerStrategy(),
        new CaptureMaxPieces()))),

    /**
     * For the medium plus, it not only has the behavior for the medium strategy,
     * it also AvoidCellsNextToCorners.
     */
    MEDIUMPLUS(new CompleteStrategy(new CompositeStrategy(new CompositeStrategy(
        new CornerStrategy(), new AvoidCellsNextToCornersStrategy()), new CaptureMaxPieces()))),

    /**
     * for the hard level, this strategy is minimax ,
     * which means it will simulate the action and then take the best action.
     */
    HARD(new CompleteStrategy(new MinimaxStrategy())),

    /**
     * for the provider easy, this strategy is easiest strategy from the provider's
     * strategies.
     */
    PROVIDEREASY(new ProviderCompleteStrategy(new ProviderLongestPathStrategy())),

    /**
     * for the provider medium, this strategy is combination of corner strategy and
     * the strategy that find the position that can get the highest points.
     */
    PROVIDERMEDIUM(new CompleteStrategy(new ProviderFallBackStrategy(new ProviderCornersStrategy(),
            new ProviderLongestPathStrategy()))),

    /**
     * for the provider hard, this strategy is the combination of CornersStrategy and
     * the strategy that AvoidingNextToCorners.
     */
    PROVIDERHARD(new CompleteStrategy(new ProviderFallBackStrategy(new ProviderCornersStrategy(),
            new ProvideAvoidingNextToCornersStrategy())));

    private final InfallibleStrategy strategy;

    /**
     * Every level of difficulty will has a corresponding strategy.
     *
     * @param strategy the corresponding strategy
     */
    Difficulty(InfallibleStrategy strategy) {
      this.strategy = strategy;
    }

    /**
     * get the corresponding strategy fo this difficulty.
     *
     * @return the corresponding strategy
     */
    public InfallibleStrategy getStrategy() {
      return strategy;
    }
  }

}
