package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A regular Reversi model, contains the regular rules. Once the cell was placed
 * successfully in this model, the adjacent cells will be flipped if they formed
 * a line with opposite color cells. The user may choose to pass if there is no
 * valid move or they just want to do it.
 */
public final class RegularReversiModel implements MutableReversiModel {
  private static final int DEFAULT_SIZE = 6;
  //INVARIANT: size is greater than 1.
  private final int size;
  private final Map<RowColPair, Hexagon> board;
  //INVARIANT: passTimes can not be larger than 2.
  private int passTimes;
  //INVARIANT: turn can only be white or black.
  private RepresentativeColor turn;
  private final List<ModelListener> listeners;
  private boolean hasGameStarted = false;
  private final ModelStatus status;

  /**
   * initialize the game with the given configurations from the Builder.
   * A private constructor is used to prevent direct instantiation and
   * ensure that this occurs in the builder.
   *
   * @param builder the builder
   */
  private RegularReversiModel(Builder builder) {
    if (builder.board == null || builder.size < 2 || (builder.turn != RepresentativeColor.WHITE
            && builder.turn != RepresentativeColor.BLACK)) {
      throw new IllegalArgumentException(
              "Error occurred when trying to initialize Reversi Model from rigged board"
      );
    }
    board = builder.board;
    passTimes = builder.passTimes;
    this.size = builder.size;
    this.status = builder.status;
    this.turn = builder.turn;
    this.hasGameStarted = builder.hasGameStarted;
    this.listeners = new ArrayList<>();
    setEntireBoardToBlankCells(size);
    setBoardToStartingPosition();
  }

  /**
   * Represents a configurable builder for a Regular Reversi Model
   */
  public static class Builder {
    //Required fields
    private ModelStatus status;

    //Optional fields - initialized to default values
    private int size = DEFAULT_SIZE;
    private Map<RowColPair, Hexagon> board = new HashMap<>();
    private int passTimes = 0;
    private RepresentativeColor turn = RepresentativeColor.BLACK;
    private boolean hasGameStarted = false;

    /**
     * Constructs the builder with the given status. Since status is the only required parameter
     * for a game of Reversi, the Builder need only take in this value.
     *
     * @param status the current status for the game
     */
    public Builder(ModelStatus status) {
      this.status = status;
    }

    /**
     * Test-focused constructor the game with the given size. the 2 should be the smallest size for a board
     * to put in all the pre-positioned cells. Used to test the functionality of model.
     * The values will be the defualt ones. For a standard Reversi Game, the default values are
     * side length 6 and 6 pre-positioned cells with 3 black and 3 white. Since it is only
     * test-focused, there are no required arguments. This constructor is also used for
     * deepCopy()
     */
    Builder() {
    }

    /**
     * A setter that updates the size. Since size is intended to be configurable by the client,
     * it is public
     *
     * @param size the desired size for our Reversi Model builder
     * @return this Builder, to allow for chained building.
     */
    public Builder setSize(int size) {
      this.size = size;
      return this;
    }

    /**
     * The following fields should only be configured for testing purposes so these setters are
     * package-private. They are also used for deepCopy()
     **/
    Builder setBoard(Map<RowColPair, Hexagon> board) {
      this.board = board;
      return this;
    }

    Builder setPassTimes(int passTimes) {
      this.passTimes = passTimes;
      return this;
    }

    Builder setTurn(RepresentativeColor color) {
      this.turn = color;
      return this;
    }

    Builder setGameStarted(boolean hasGameStarted) {
      this.hasGameStarted = hasGameStarted;
      return this;
    }

    /**
     * Builds a Regular Reversi Model with the configurations of this builder.
     *
     * @return the immutable regular reversi model.
     */
    public RegularReversiModel build() {
      return new RegularReversiModel(this);
    }
  }

  /**
   * each cube system has three coordinators r,q and s, as the graph shows below
   * and the middle point is (0,0,0), the graph shows the coordinators with the order(r,q,s), as
   * the graph shows, row means the horizontal line, the q means the leftCol which refers to the
   * number of column we count from left to right and the s means rightCol means the number of
   * column from right to left. the original point is in the middle of the graph, if the cell go
   * left, the r will increase, if the cell go right, the r will decrease. for the q, the leftCol,
   * it will increase when the col go left. and for the s, the rightCol, it will increase
   * when the col go right.
   * (-2,0,2) (-2,1,1) (-2,2,0)
   * (-1,-1,2) (-1,0,1) (-1,1,0) (-1,-2,-1)
   * (0,-2,2) (0,-1,1) (0,0,0) (0,1,-1) (0,2,-2)
   * (1,-2,1) (1,-1,0) (1,0,-1) (1,1,-2)
   * (2,-2,0) (2,-1,-1) (2,0,-2)
   *
   * @param size the size of the board
   */
  private void setEntireBoardToBlankCells(int size) {
    int row = 2 * size - 1;
    int half = row - size;
    int upHalfStarter = 0;
    for (int i = half; i > 0; i--) {
      for (int j = upHalfStarter; j <= half; j++) {
        int x = -i;
        board.put(new RowColPair(x, j), new Hexagon(RepresentativeColor.NONE));
      }
      upHalfStarter--;
    }
    for (int i = -half; i <= half; i++) {
      board.put(new RowColPair(0, i), new Hexagon(RepresentativeColor.NONE));
    }
    int downHalfStarter = half;
    for (int i = 1; i <= half; i++) {
      for (int j = -half; j < downHalfStarter; j++) {
        board.put(new RowColPair(i, j), new Hexagon(RepresentativeColor.NONE));
      }
      downHalfStarter--;
    }
  }

  /**
   * Set the initial state of the standard board, at the beginning, the number of white cells
   * and black cells will be the same. The middle cells of the board should be empty and the
   * pre-positioned cells are surrounding it.
   */
  private void setBoardToStartingPosition() {
    board.put(new RowColPair(0, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(0, -1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-1, 0), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-1, 1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(1, 0), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(1, -1), new Hexagon(RepresentativeColor.BLACK));
  }

  /**
   * check if the given row and column is inside the board.
   *
   * @return true if it is in the board otherwise false.
   */
  private boolean isInBounds(RowColPair pair) {
    return board.containsKey(pair);
  }

  /**
   * check if the game is already over, since some actions are ot allowed after game is over.
   *
   * @throws IllegalStateException when the game is already over
   */
  private void checkIfGameOver() {
    if (isGameOver()) {
      throw new IllegalStateException("game already ended");
    }
  }

  /**
   * check if the attempt move is a valid move. check if the attempt position is
   * next to the opposite color cell with the current color and check if we can
   * flip some cell in a certain direction. once one player successfully placed
   * a cell, reset the pass times. In the end, update all the views by calling the controller
   * to let the view repaint the board and update the controllers to show the current state of the
   * game.
   *
   * @param pair          the row-col pair
   * @param currentPlayer the player that wants to place a move
   * @throws IllegalArgumentException If the coordinators are invalid
   * @throws IllegalStateException    If we can not place the given color cell in given position
   *                                  or the game is already over or has not started
   */
  @Override
  public void placeMove(RowColPair pair, RepresentativeColor currentPlayer) {
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
    Map<Direction, Integer> flipTimes = checkMove(pair, turn);
    for (Direction direction : Direction.values()) {
      CubeCoordinateTrio adjacentCube = findAdjacentCells(pair.convertToCube(), direction);
      RowColPair adjacent = adjacentCube.convertToRowCol();
      int flip = flipTimes.get(direction);
      if (flip > 0) {
        canPlace = true;
        if (passTimes > 0) {
          passTimes = 0;
        }
        hexagon.setColor(color);
      }
      CubeCoordinateTrio cube = adjacent.convertToCube();
      int fixedR = cube.getRow();
      int fixedQ = cube.getLeftCol();
      int fixedS = cube.getRightCol();
      for (int i = 0; i < flip; i++) {
        Hexagon current = board.get(cube.convertToRowCol());
        current.setColor(current.getColor().getOpposite());
        fixedR += direction.getRowOffset();
        fixedQ += direction.getLeftColOffset();
        fixedS += direction.getRightColOffset();
        cube = new CubeCoordinateTrio(fixedR, fixedQ, fixedS);
      }
    }
    if (!canPlace) {
      throw new IllegalStateException("Invalid move");
    }
    turn = turn.getOpposite();
    status.updateStatus(this);
    notifyAllListenersOfModelChange();
  }

  /**
   * check if the provided row and column are valid coordinators.
   *
   * @throws IllegalArgumentException if the coordinators is outOfBounds or it returns null
   * @throws IllegalStateException    if the position was already occupied
   */
  private void checkCoordinators(RowColPair pair) {
    if (!isInBounds(pair) || pair == null) {
      throw new IllegalArgumentException("invalid coordinators");
    }
    if (board.get(pair).getColor() != RepresentativeColor.NONE) {
      throw new IllegalStateException("Can only place on exist cell");
    }
  }

  @Override
  public Map<Direction, Integer> checkMove(RowColPair pair, RepresentativeColor color) {
    checkIfGameOver();
    checkCoordinators(pair);
    Map<Direction, Integer> map = new HashMap<>();
    for (Direction direction : Direction.values()) {
      int flip = checkFlip(pair, direction, color);
      map.put(direction, flip);
    }
    return map;
  }

  /**
   * make a pass action when the user has to pass in this turn or the user wants to pass
   * alter the turn to another player. In the end, update all the views by calling the controller
   * to let the view repaint the board and update the controllers to show the current state of the
   * game.
   *
   * @param currentPlayer the player that want to make pass
   */
  @Override
  public void makePass(RepresentativeColor currentPlayer) {
    checkIfGameStarted();
    checkIfGameOver();
    if (currentPlayer != turn) {
      throw new IllegalStateException("It's not your turn");
    }
    passTimes++;
    turn = turn.getOpposite();
    status.updateStatus(this);
    notifyAllListenersOfModelChange();
  }

  /**
   * check if the game was started, since some actions are allowed before the game was started.
   *
   * @throws IllegalStateException when the game has not started
   */
  private void checkIfGameStarted() {
    if (!hasGameStarted) {
      throw new IllegalStateException("Unable to query the model before the game "
              + "has started");
    }
  }

  /**
   * check the num of cell that can be flipped from the given position in the
   * given direction.
   *
   * @param direction the direction
   * @return the number of flippable cells in that direction
   */
  private int checkFlip(RowColPair current, Direction direction, RepresentativeColor color) {
    CubeCoordinateTrio adjacent = findAdjacentCells(current.convertToCube(), direction);
    RowColPair adjacentRowCol = adjacent.convertToRowCol();
    if (!isInBounds(adjacentRowCol)) {
      return 0;
    }
    RepresentativeColor currentRepresentativeColor = board.get(adjacentRowCol).getColor();
    if (currentRepresentativeColor == RepresentativeColor.NONE) {
      return 0;
    }
    if (currentRepresentativeColor.getOpposite() != color) {
      return 0;
    }
    return checkContinues(direction, adjacentRowCol, color);
  }

  /**
   * check the number of continues cells with opposite color in the certain direction.
   *
   * @param direction       the given direction
   * @param currentPosition the position that need to check its surround area
   * @return the number of continues cells with opposite color in the certain direction
   */
  private int checkContinues(Direction direction, RowColPair currentPosition,
                             RepresentativeColor color) {
    Hexagon hexagon = board.get(currentPosition);
    RepresentativeColor current = hexagon.getColor();
    int replace = 1;
    CubeCoordinateTrio cube = currentPosition.convertToCube();
    int q = cube.getLeftCol();
    int r = cube.getRow();
    int s = cube.getRightCol();
    while (true) {
      q += direction.getLeftColOffset();
      r += direction.getRowOffset();
      s += direction.getRightColOffset();
      CubeCoordinateTrio newCube = new CubeCoordinateTrio(r, q, s);
      RowColPair rowCol = newCube.convertToRowCol();
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

  /**
   * Check through the whole board to see if there exist a valid move for the current turn color.
   *
   * @return false if there exist valid move for given color otherWise true
   */
  @Override
  public boolean hasToPass() {
    checkIfGameOver();
    checkIfGameStarted();
    for (RowColPair pair : board.keySet()) {
      if (board.get(pair).getColor() != RepresentativeColor.NONE) {
        continue;
      }
      for (Direction direction : Direction.values()) {
        int flip = checkFlip(pair, direction, turn);
        if (flip > 0) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public RepresentativeColor getColorAt(RowColPair pair) {
    if (!isInBounds(pair)) {
      throw new IllegalArgumentException("Out of bounds");
    }
    return board.get(pair).getColor();
  }

  @Override
  public RepresentativeColor getWinner() {
    checkIfGameStarted();
    if (!isGameOver()) {
      throw new IllegalStateException("game hasn't ended yet");
    }
    int blackScore = getScore(RepresentativeColor.BLACK);
    int whiteScore = getScore(RepresentativeColor.WHITE);
    if (blackScore > whiteScore) {
      return RepresentativeColor.BLACK;
    } else if (whiteScore > blackScore) {
      return RepresentativeColor.WHITE;
    }
    return null;
  }

  @Override
  public int getScore(RepresentativeColor color) {
    int score = 0;
    for (RowColPair pair : board.keySet()) {
      RepresentativeColor currentColor = board.get(pair).getColor();
      if (currentColor == color) {
        score++;
      }
    }
    return score;
  }

  @Override
  public MutableReversiModel getDeepCopy(RepresentativeColor color) {
    return new RegularReversiModel.Builder(status).setSize(size).setBoard(board).setTurn(turn)
            .setGameStarted(hasGameStarted).setPassTimes(passTimes).build();
  }

  @Override
  public void startGame() {
    if (hasGameStarted) {
      throw new IllegalStateException("Game already started");
    }
    if (isGameOver()) {
      throw new IllegalStateException("Game already over");
    }
    this.hasGameStarted = true;
    turn = RepresentativeColor.BLACK;
    status.updateStatus(this);
    notifyAllListenersOfModelChange();
  }

  private void notifyAllListenersOfModelChange() {
    for (ModelListener listener : listeners) {
      listener.update();
    }
  }

  /**
   * get the coordinators for the current cell's adjacent cell in given direction.
   *
   * @param current   the coordinators for the current cell
   * @param direction the given direction
   * @return the coordinators
   */
  private CubeCoordinateTrio findAdjacentCells(CubeCoordinateTrio current, Direction direction) {
    return new CubeCoordinateTrio(current.getRow() + direction.getRowOffset(),
            current.getLeftCol() + direction.getLeftColOffset(),
            current.getRightCol() + direction.getRightColOffset());
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
  public boolean isGameOver() {
    return passTimes >= 2;
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public RepresentativeColor getTurn() {
    checkIfGameOver();
    return turn;
  }

  @Override
  public void addListener(ModelListener listener) {
    listeners.add(listener);
  }
}
