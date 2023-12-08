package model;

import java.util.HashMap;
import java.util.Map;

public final class SquareReversiModel extends AbstractReversiModel{

  private SquareReversiModel(int size, ModelStatus status) {
    super(size, status);
  }

  /**
   * Test-focused constructor used to test game logic in rigged positions for the purpose of
   * examining rigged models, the boards must be at least 2 x 2 and turn must be black ior white.
   * They can violate other logical rules (like not starting from
   * starting position, or starting completely empty, since they are only used for testing.
   *
   * @param board the rigged board from which to construct this instance from.
   * @param size  the size to construct the new board to
   * @param turn  the color whose turn it is
   */
  SquareReversiModel(Map<RowColPair, Hexagon> board, int size, RepresentativeColor turn) {
    super(board, size, turn);
  }

  /**
   * A Builder to the regular reversi model, the target is to the reducing the number of
   * constructor and supply flexibility and Step-by-Step Construction for the model.
   */
  public static class ModelBuilder {
    private ModelStatus status;

    private int size;

    /**
     * A setter that updates the status object.
     *
     * @param status the desired status object for our Reversi Model builder
     * @return this Builder, to allow for chained building.
     */
    public ModelBuilder setStatus(ModelStatus status) {
      this.status = status;
      return this;
    }

    /**
     * Initialize the builder to standard reversi game with size 6 and a default
     * status class.
     */
    public ModelBuilder() {
      status = new ReversiModelStatus();
      size = DEFAULT_SIZE;
    }

    /**
     * A setter that updates the size.
     *
     * @param size the desired size for our Reversi Model builder
     * @return this Builder, to allow for chained building.
     */
    public ModelBuilder setSize(int size) {
      this.size = size;
      return this;
    }

    /**
     * Builds a Regular Reversi Model with the configurations of this builder.
     *
     * @return the regular reversi model.
     */
    public SquareReversiModel build() {
      return new SquareReversiModel(size, status);
    }
  }

  @Override
  protected void setEntireBoardToBlankCells(int size) {
    int difference = size / 2 - 1;
    for(int row = 0; row < size; row++) {
      for (int col = 0; col < size; col ++) {
        RowColPair pair = new RowColPair(row - difference, col - difference);
        Hexagon hexagon = new Hexagon(RepresentativeColor.NONE);
        board.put(pair, hexagon);
      }
    }
  }

  @Override
  protected void setBoardToStartingPosition(int size) {
    board.put(new RowColPair(0, 0), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(0, 1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(1, 0), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(1, 1), new Hexagon(RepresentativeColor.BLACK));
  }

  private void checkCoordinators(RowColPair pair) {
    if (!isInBounds(pair) || pair == null) {
      throw new IllegalArgumentException("invalid coordinators");
    }
    if (board.get(pair).getColor() != RepresentativeColor.NONE) {
      throw new IllegalStateException("Can only place on available cell");
    }
  }


  private void checkIfGameOver() {
    if (isGameOver()) {
      throw new IllegalStateException("game already ended");
    }
  }

  private void checkIfGameStarted() {
    if (!hasGameStarted) {
      throw new IllegalStateException("Unable to query the model before the game "
        + "has started");
    }
  }

  @Override
  protected void tryPlaceMove(RowColPair pair, RepresentativeColor currentPlayer){
    if (currentPlayer != turn) {
      throw new IllegalStateException("It's not your turn");
    }
    checkIfGameStarted();
    checkIfGameOver();
    checkCoordinators(pair);
    int row = pair.getRow();
    int col = pair.getCol();
    RepresentativeColor color = turn;
    if (color == RepresentativeColor.NONE || color == null) {
      throw new IllegalArgumentException("Invalid color");
    }
    boolean canPlace = false;
    Hexagon hexagon = board.get(new RowColPair(row, col));
    Map<ModelDirection, Integer> flipTimes = checkMove(pair, turn);
    for (SquareDirection direction : SquareDirection.values()) {
      RowColPair adjacent = findAdjacentCells(pair, direction);
      int flip = flipTimes.get(direction);
      if (flip > 0) {
        canPlace = true;
        if (passTimes > 0) {
          passTimes = 0;
        }
        hexagon.setColor(color);
      }
      RowColPair currenPair = adjacent;
      int fixedR = currenPair.getRow();
      int fixedQ = currenPair.getCol();
      for (int i = 0; i < flip; i++) {
        Hexagon current = board.get(currenPair);
        current.setColor(current.getColor().getOpposite());
        fixedR += direction.getRowOffset();
        fixedQ += direction.getLeftColOffset();
        currenPair = new RowColPair(fixedR, fixedQ);
      }
    }
    if (!canPlace) {
      throw new IllegalStateException("Invalid move");
    }
    turn = turn.getOpposite();
    status.updateStatus(this);
    for (ModelListener listener : listeners) {
      listener.update();
    }
  }

  @Override
  public Map<RowColPair, Hexagon> getBoard() {
    Map<RowColPair, Hexagon> copy = new HashMap<>();
    for (RowColPair pair : board.keySet()) {
      copy.put(new RowColPair(pair.getRow(), pair.getCol()),
        new Hexagon(board.get(pair).getColor()));
    }
    return copy;
  }

  @Override
  protected boolean checkHasToPass() {
    for(RowColPair pair : board.keySet()) {
      Map<ModelDirection, Integer> map = checkMove(pair, turn);
      for (int i : map.values()) {
        if (i > 0) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  protected MutableReversiModel tryGetDeepCopy(RepresentativeColor color) {
    return new SquareReversiModel(this.getBoard(), size, color);
  }

  @Override
  protected Map<ModelDirection, Integer> tryCheckMove(RowColPair pair, RepresentativeColor color)  {
    checkIfGameOver();
    if (!isInBounds(pair) || pair == null) {
      throw new IllegalArgumentException("invalid coordinators");
    }
    if (board.get(pair).getColor() != RepresentativeColor.NONE) {
      Map<ModelDirection, Integer> value = new HashMap<>();
      for (SquareDirection direction : SquareDirection.values()) {
        value.put(direction, 0);
      }
      return value;
    }
    Map<ModelDirection, Integer> value = new HashMap<>();
    for (SquareDirection direction : SquareDirection.values()) {
      value.put(direction, checkFlip(pair, direction, color));
    }
    return value;
  }

  private boolean isInBounds(RowColPair pair) {
    return board.containsKey(pair);
  }

  private int checkFlip(RowColPair current, SquareDirection direction, RepresentativeColor color) {
    RowColPair adjacent = findAdjacentCells(current, direction);
    if (!isInBounds(adjacent)) {
      return 0;
    }
    RepresentativeColor currentRepresentativeColor = board.get(adjacent).getColor();
    if (currentRepresentativeColor == RepresentativeColor.NONE) {
      return 0;
    }
    if (currentRepresentativeColor.getOpposite() != color) {
      return 0;
    }
    return checkContinues(direction, adjacent, color);
  }

  private int checkContinues(SquareDirection direction, RowColPair currentPosition,
                             RepresentativeColor color) {
    Hexagon hexagon = board.get(currentPosition);
    RepresentativeColor current = hexagon.getColor();
    int replace = 1;
    int r = currentPosition.getRow();
    int c = currentPosition.getCol();
    while (true) {
      r += direction.getRowOffset();
      c += direction.getLeftColOffset();
      RowColPair rowCol = new RowColPair(r, c);
      if (!isInBounds(rowCol)) {
        return 0;
      }
      current = board.get(rowCol).getColor();
      if (current == color) {
        return replace;
      } else if (current == RepresentativeColor.NONE) {
        return 0;
      } else if (current.getOpposite() == color) {
        replace++;
      }
    }
  }

  private RowColPair findAdjacentCells(RowColPair current, SquareDirection direction) {
    return new RowColPair(current.getRow() + direction.getRowOffset(),
      current.getCol() + direction.getLeftColOffset());
  }

}
