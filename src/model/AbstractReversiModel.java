package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractReversiModel implements MutableReversiModel{
  protected static final int DEFAULT_SIZE = 6;
  //INVARIANT: size is greater than 1.
  protected final int size;
  protected final Map<RowColPair, Hexagon> board;
  //INVARIANT: passTimes can not be larger than 2.
  protected int passTimes;
  //INVARIANT: turn can only be white or black.
  protected RepresentativeColor turn = null;
  protected List<ModelListener> listeners;
  protected boolean hasGameStarted = false;
  protected final ModelStatus status;

  /**
   * initialize the game with the given size. the 2 should be the smallest size for a board
   * to put in all the pre-positioned cells.
   *
   * @param size size of the board
   * @param status the status that represents the most recent states of game
   */
  protected AbstractReversiModel(int size, ModelStatus status) {
    if (size < 2) {
      throw new IllegalArgumentException("Invalid board size");
    }
    board = new HashMap<>();
    passTimes = 0;
    this.size = size;
    this.listeners = new ArrayList<>();
    this.status = status;
    setEntireBoardToBlankCells(size);
    setBoardToStartingPosition(size);
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
  AbstractReversiModel(Map<RowColPair, Hexagon> board, int size, RepresentativeColor turn) {
    if (board == null || size < 2 || (turn != RepresentativeColor.WHITE
      && turn != RepresentativeColor.BLACK)) {
      throw new IllegalArgumentException(
        "Error occurred when trying to initialize Reversi Model from rigged board"
      );
    }
    this.hasGameStarted = true;
    this.board = board;
    passTimes = 0;
    this.turn = turn;
    this.size = size;
    status = new ReversiModelStatus();
    listeners = new ArrayList<>();
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
  protected abstract void setEntireBoardToBlankCells(int size);

  /**
   * Set the initial state of the standard board, at the beginning, the number of white cells
   * and black cells will be the same. The middle cells of the board should be empty and the
   * pre-positioned cells are surrounding it.
   */
  protected abstract void setBoardToStartingPosition(int size);

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


  protected abstract void tryPlaceMove(RowColPair pair, RepresentativeColor color);

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
  public void placeMove(RowColPair pair, RepresentativeColor currentPlayer) {
    tryPlaceMove(pair, currentPlayer);
  }

  protected abstract Map<ModelDirection, Integer> tryCheckMove(RowColPair pair, RepresentativeColor color);
  @Override
  public Map<ModelDirection, Integer> checkMove(RowColPair pair, RepresentativeColor color) {
    return tryCheckMove(pair, color);
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
    for (ModelListener listener : listeners) {
      listener.update();
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

  protected abstract boolean checkHasToPass();

  /**
   * Check through the whole board to see if there exist a valid move for the current turn color.
   *
   * @return false if there exist valid move for given color otherWise true
   */
  @Override
  public boolean hasToPass() {
    return checkHasToPass();
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

  protected abstract MutableReversiModel tryGetDeepCopy(RepresentativeColor color);
  @Override
  public MutableReversiModel getDeepCopy(RepresentativeColor color) {
    return tryGetDeepCopy(color);
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
