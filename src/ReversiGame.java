import controller.Controller;
import controller.ControllerListeners;
import model.ModelStatus;
import model.MutableReversiModel;
import model.Player;
import model.ReadOnlyReversiModel;
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
   * The main method for starting a Reversi game based on command-line arguments.
   * It initializes players, board type, and size based on the provided arguments,
   * creates a Reversi model, graphic views, and controllers, and starts the game.
   *
   * @param args Command-line arguments containing board type, players, and optionally size.
   *             The expected format is: [BoardType] [Player1Type] [Player2Type] [Size (optional)]
   *             Example: SQUARE HUMAN COMPUTER 8
   * @throws IllegalArgumentException If the command line arguments are invalid or incomplete.
   */
  public static void main(String[] args) {
    Player player1 = new ReversiHumanPlayer();
    Player player2 = new ReversiHumanPlayer();
    if (args.length != 3 && args.length != 4) {
      throw new IllegalArgumentException("Reversi game must has two players "
        + "and board type with size");
    }
    ReadOnlyReversiModel.ModelType modelType = null;
    int size = -1;
    for (int i = 0; i < args.length; i++) {
      String type = args[i].toUpperCase();
      switch (i) {
        case 0: {
          modelType = ReadOnlyReversiModel.ModelType.valueOf(type);
          continue;
        }
        case 1: {
          player1 = generatePlayer(type);
          continue;
        }
        case 2: {
          player2 = generatePlayer(type);
          continue;
        }
        case 3: {
          size = Integer.parseInt(type);
          continue;
        }
        default: {
          throw new IllegalArgumentException("max size of command line should be 4");
        }
      }
    }
    MutableReversiModel model;
    ModelStatus status = new ReversiModelStatus();
    if (args.length == 3) {
      model = ModelCreator.create(modelType, status);
    } else {
      model = ModelCreator.create(modelType, size, status);
    }
    ReversiGraphicView view = new ReversiGraphicView(model);
    ReversiGraphicView view2 = new ReversiGraphicView(model);
    Controller controller = new Controller(model, view, player1, status);
    Controller controller2 = new Controller(model, view2, player2, status);
    ControllerListeners listeners = new ControllerListeners();
    listeners.register(controller);
    listeners.register(controller2);
    model.addListener(listeners);
    model.startGame();
  }

  private static Player generatePlayer(String type) {
    Player player = new ReversiHumanPlayer();
    if (type.equals("HUMAN")) {
      player = new ReversiHumanPlayer();
    } else {
      try {
        InfallibleStrategy strategy = Difficulty.valueOf(type).getStrategy();
        player = new ReversiAiPlayer(strategy);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("No such type of game player supported");
      }
    }
    return player;
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
    HARD(new CompleteStrategy(new MinimaxStrategy()));

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
