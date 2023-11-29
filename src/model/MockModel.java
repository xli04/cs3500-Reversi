package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a Mock model, represents a mock of the regular model, used to test if the strategy did the
 * things we expected. it will give feedback which pair does the strategy visited and also
 * to test the strategy itself works properly.
 */
final class MockModel implements MutableReversiModel {
  private final MutableReversiModel model;
  private final StringBuilder builder;
  private final List<RowColPair> positionsLieToStrategy;
  
  @Override
  public Map<Direction, Integer> checkMove(RowColPair pair, RepresentativeColor color) {
    builder.append("Checking").append("(").append(pair.getRow()).append(",")
      .append(pair.getCol()).append(")").append("\n");
    if (positionsLieToStrategy.contains(pair)) {
      builder.append("lie on you").append("\n");
      Map<Direction, Integer> lieMap = new HashMap<>();
      for (Direction direction : Direction.values()) {
        lieMap.put(direction, 0);
      }
      return lieMap;
    }
    return model.checkMove(pair, color);
  }

  /**
   * Construct the mockModel with the regular model as delegation and a string builder
   * to record the action and lying list that used to lie to the strategy.
   *
   * @param model the given regular model
   * @param builder the given builder
   * @param lie a list of positions that we choose to lie to the strategy
   * @throws IllegalArgumentException if any of the parameters is invalid
   */
  MockModel(MutableReversiModel model, StringBuilder builder, List<RowColPair> lie) {
    if (model == null || builder == null || lie == null) {
      throw new IllegalArgumentException("Can not create a mock model");
    }
    this.model = model;
    this.builder = builder;
    this.positionsLieToStrategy = lie;
  }

  @Override
  public void placeMove(RowColPair pair, RepresentativeColor currentPlayer) {
    model.placeMove(pair, currentPlayer);
  }

  @Override
  public void makePass(RepresentativeColor currentPlayer) {
    model.makePass(currentPlayer);
  }

  @Override
  public Map<RowColPair, Hexagon> getBoard() {
    return model.getBoard();
  }

  @Override
  public boolean isGameOver() {
    return model.isGameOver();
  }

  @Override
  public boolean hasToPass() {
    return model.hasToPass();
  }

  @Override
  public RepresentativeColor getColorAt(RowColPair pair) {
    return model.getColorAt(pair);
  }

  @Override
  public RepresentativeColor getWinner() {
    return model.getWinner();
  }

  @Override
  public int getSize() {
    return model.getSize();
  }

  @Override
  public RepresentativeColor getTurn() {
    return model.getTurn();
  }

  @Override
  public int getScore(RepresentativeColor color) {
    return model.getScore(color);
  }

  @Override
  public MutableReversiModel getDeepCopy(RepresentativeColor color) {
    return model.getDeepCopy(color);
  }

  @Override
  public void addListener(ModelListener listener) {
    builder.append("add listener");
  }

  @Override
  public void startGame() {
    builder.append("game start");
    model.startGame();
  }
}
