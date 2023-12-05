import adapter.strategy.ProviderCompleteStrategy;
import adapter.strategy.ProviderFallBackStrategy;
import reversi.provider.model.ReversiModel;
import reversi.provider.strategy.AvoidingNextToCorners;
import reversi.provider.strategy.Corners;
import reversi.provider.strategy.FallbackStrategy;
import reversi.provider.strategy.FinalStrategy;
import reversi.provider.strategy.LongestPath;
import strategy.AvoidCellsNextToCornersStrategy;
import strategy.CaptureMaxPieces;
import strategy.CompleteStrategy;
import strategy.CompositeStrategy;
import strategy.CornerStrategy;
import strategy.InfallibleStrategy;
import strategy.MinimaxStrategy;

/**
 * A difficulty creator class, represents the different difficulties correspond with
 * the different strategy that will be used to choose the position.
 */
public class DifficultyCreator {
  private static ReversiModel model = null;

  /**
   * Constructor the DifficultyCreator class.
   *
   * @param model the model that will be used in the provider's strategy
   */
  public DifficultyCreator(ReversiModel model) {
    DifficultyCreator.model = model;
  }

  /**
   * get the Strategy Corresponding to the Difficulty.
   *
   * @param s the difficulty in the string form
   * @return the strategy that the player wants
   */
  public InfallibleStrategy getDifficultyCorrespondingStrategy(String s) {
    if (s.contains("PROVIDER") && model == null) {
      throw new IllegalArgumentException("Has not set the model yet");
    }
    return Difficulty.valueOf(s).getStrategy();
  }

  /**
   * Represent a difficult for the strategies.
   */
  private enum Difficulty {

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
    PROVIDEREASY(new ProviderCompleteStrategy(new FinalStrategy(new LongestPath()), model)),

    /**
     * for the provider medium, this strategy is combination of corner strategy and
     * the strategy that find the position that can get the highest points.
     */
    PROVIDERMEDIUM(new CompleteStrategy(new ProviderFallBackStrategy(new FallbackStrategy(new Corners(),
      new LongestPath()), model))),

    /**
     * for the provider hard, this strategy is the combination of CornersStrategy and
     * the strategy that AvoidingNextToCorners.
     */
    PROVIDERHARD(new CompleteStrategy(new ProviderFallBackStrategy(new FallbackStrategy(new Corners(),
      new AvoidingNextToCorners()),model)));

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
