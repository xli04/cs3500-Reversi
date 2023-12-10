package model;

import java.util.HashMap;
import java.util.Map;

/**
 * A regular Reversi model, contains the regular rules. Once the cell was placed
 * successfully in this model, the adjacent cells will be flipped if they formed
 * a line with opposite color cells. The user may choose to pass if there is no
 * valid move or they just want to do it.
 */
public final class HexReversiModel extends AbstractReversiModel {

  /**
   * initialize the game with the given size. the 2 should be the smallest size for a board
   * to put in all the pre-positioned cells.
   *
   * @param size size of the board
   * @param status the status that represents the most recent states of game
   */
  private HexReversiModel(int size, ModelStatus status) {
    super(size, status);
  }

  /**
   * A Builder to the regular reversi model, the target is to the reducing the number of
   * constructor and supply flexibility and Step-by-Step Construction for the model.
   */
  public static class ModelBuilder {
    private ModelStatus status;
    private int size;

    protected final int DEFAULT_SIZE = 6;

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
    public HexReversiModel build() {
      return new HexReversiModel(size, status);
    }
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
  HexReversiModel(Map<RowColPair, CellPiece> board, int size, RepresentativeColor turn) {
    super(board, size, turn);
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
   *        (-2,0,2) (-2,1,1) (-2,2,0)
   *    (-1,-1,2) (-1,0,1) (-1,1,0) (-1,-2,-1)
   * (0,-2,2) (0,-1,1) (0,0,0) (0,1,-1) (0,2,-2)
   *    (1,-2,1) (1,-1,0) (1,0,-1) (1,1,-2)
   *       (2,-2,0) (2,-1,-1) (2,0,-2)
   *
   * @param size the size of the board
   */
  @Override
  protected void setEntireBoardToBlankCells(int size) {
    int row = 2 * size - 1;
    int half = row - size;
    int upHalfStarter = 0;
    for (int i = half; i > 0; i--) {
      for (int j = upHalfStarter; j <= half; j++) {
        int x = -i;
        board.put(new RowColPair(x, j), new CellPiece(RepresentativeColor.NONE));
      }
      upHalfStarter--;
    }
    for (int i = -half; i <= half; i++) {
      board.put(new RowColPair(0, i), new CellPiece(RepresentativeColor.NONE));
    }
    int downHalfStarter = half;
    for (int i = 1; i <= half; i++) {
      for (int j = -half; j < downHalfStarter; j++) {
        board.put(new RowColPair(i, j), new CellPiece(RepresentativeColor.NONE));
      }
      downHalfStarter--;
    }
  }


  /**
   * Set the initial state of the standard board, at the beginning, the number of white cells
   * and black cells will be the same. The middle cells of the board should be empty and the
   * pre-positioned cells are surrounding it.
   */
  @Override
  protected void setBoardToStartingPosition(int size) {
    board.put(new RowColPair(0, 1), new CellPiece(RepresentativeColor.BLACK));
    board.put(new RowColPair(0, -1), new CellPiece(RepresentativeColor.WHITE));
    board.put(new RowColPair(-1, 0), new CellPiece(RepresentativeColor.BLACK));
    board.put(new RowColPair(-1, 1), new CellPiece(RepresentativeColor.WHITE));
    board.put(new RowColPair(1, 0), new CellPiece(RepresentativeColor.WHITE));
    board.put(new RowColPair(1, -1), new CellPiece(RepresentativeColor.BLACK));
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
   * @param pair the row-col pair
   * @param currentPlayer the player that wants to place a move
   * @throws IllegalArgumentException If the coordinators are invalid
   * @throws IllegalStateException    If we can not place the given color cell in given position
   *                                  or the game is already over or has not started
   */
  @Override
  protected void tryPlaceMove(RowColPair pair, RepresentativeColor currentPlayer) {
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
    CellPiece cellPiece = board.get(new RowColPair(row, col));
    Map<ModelDirection, Integer> flipTimes = checkMove(pair, turn);
    for (HexDirection hexDirection : HexDirection.values()) {
      CubeCoordinateTrio adjacentCube = findAdjacentCells(pair.convertToCube(), hexDirection);
      RowColPair adjacent = adjacentCube.convertToRowCol();
      int flip = flipTimes.get(hexDirection);
      if (flip > 0) {
        canPlace = true;
        if (passTimes > 0) {
          passTimes = 0;
        }
        cellPiece.setColor(color);
      }
      CubeCoordinateTrio cube = adjacent.convertToCube();
      int fixedR = cube.getRow();
      int fixedQ = cube.getLeftCol();
      int fixedS = cube.getRightCol();
      for (int i = 0; i < flip; i++) {
        CellPiece current = board.get(cube.convertToRowCol());
        current.setColor(current.getColor().getOpposite());
        fixedR += hexDirection.getRowOffset();
        fixedQ += hexDirection.getLeftColOffset();
        fixedS += hexDirection.getRightColOffset();
        cube = new CubeCoordinateTrio(fixedR, fixedQ, fixedS);
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
  protected Map<ModelDirection, Integer> tryCheckMove(RowColPair pair, RepresentativeColor color) {
    checkIfGameOver();
    checkCoordinators(pair);
    Map<ModelDirection, Integer> map = new HashMap<>();
    for (HexDirection hexDirection : HexDirection.values()) {
      int flip = checkFlip(pair, hexDirection, color);
      map.put(hexDirection, flip);
    }
    return map;
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
      throw new IllegalStateException("Can only place on available cell");
    }
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

  @Override
  protected boolean checkHasToPass() {
    checkIfGameOver();
    checkIfGameStarted();
    for (RowColPair pair : board.keySet()) {
      if (board.get(pair).getColor() != RepresentativeColor.NONE) {
        continue;
      }
      for (HexDirection hexDirection : HexDirection.values()) {
        int flip = checkFlip(pair, hexDirection, turn);
        if (flip > 0) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * check the num of cell that can be flipped from the given position in the
   * given direction.
   *
   * @param direction the direction
   * @return the number of flippable cells in that direction
   */
  private int checkFlip(RowColPair current, ModelDirection direction, RepresentativeColor color) {
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
  private int checkContinues(ModelDirection direction, RowColPair currentPosition,
                             RepresentativeColor color) {
    CellPiece cellPiece = board.get(currentPosition);
    RepresentativeColor current = cellPiece.getColor();
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

  @Override
  public ModelType checkType() {
    return ModelType.HEX;
  }

  @Override
  protected MutableReversiModel tryGetDeepCopy(RepresentativeColor color) {
    return new HexReversiModel(this.getBoard(), size, color);
  }

  /**
   * get the coordinators for the current cell's adjacent cell in given direction.
   *
   * @param current   the coordinators for the current cell
   * @param direction the given direction
   * @return the coordinators
   */
  private CubeCoordinateTrio findAdjacentCells(CubeCoordinateTrio current,
                                               ModelDirection direction) {
    return new CubeCoordinateTrio(current.getRow() + direction.getRowOffset(),
      current.getLeftCol() + direction.getLeftColOffset(),
      current.getRightCol() + direction.getRightColOffset());
  }
}
