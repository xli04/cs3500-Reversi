package adapter;

import java.util.HashMap;
import java.util.Map;
import reversi.provider.controller.Player;
import reversi.provider.model.CellPosition;
import reversi.provider.model.GamePiece;
import reversi.provider.model.ModelFeatures;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;
import model.CubeCoordinateTrio;
import model.Direction;
import model.Hexagon;
import model.ModelStatus;
import model.RegularReversiModel;
import model.RepresentativeColor;
import model.ReversiModelStatus;
import model.RowColPair;

/**
 * Combine model, represents a adapter pattern on our model and provider's model, the key
 * idea is using the methods in our model to implement the methods in the provider's model
 * interface so that their view and our view can placing in a same combine model.
 */
public class CombineModel extends RegularReversiModel implements ReversiModel {

  /**
   * initialize the game with the given size. the 2 should be the smallest size for a board
   * to put in all the pre-positioned cells.
   *
   * @param size   size of the board
   * @param status the status that represents the most recent states of game
   */
  protected CombineModel(int size, ModelStatus status) {
    super(size, status);
  }

  /**
   * initialize the game with the given size. the 2 should be the smallest size for a board
   * to put in all the pre-positioned cells.
   */

  @Override
  public boolean isGameOver() {
    return super.isGameOver();
  }

  @Override
  public GamePiece winner() {
    RepresentativeColor color = super.getWinner();
    if (color == RepresentativeColor.WHITE) {
      return GamePiece.WHITE;
    } else if (color == RepresentativeColor.BLACK) {
      return GamePiece.BLACK;
    }
    return GamePiece.EMPTY;
  }

  @Override
  public Map<CellPosition, GamePiece> getBoard() {
    Map<RowColPair, Hexagon> ourBoard = super.getCurrentBoard();
    Map<CellPosition, GamePiece> board = new HashMap<>();
    for (RowColPair pair : ourBoard.keySet()) {
      GamePiece piece;
      if (ourBoard.get(pair).getColor() == RepresentativeColor.BLACK) {
        piece = GamePiece.BLACK;
      } else if (ourBoard.get(pair).getColor() == RepresentativeColor.WHITE) {
        piece = GamePiece.WHITE;
      } else {
        piece = GamePiece.EMPTY;
      }
      board.put(convertBack(pair), piece);
    }
    return board;
  }

  @Override
  public int getBoardWidth() {
    return super.getSize();
  }

  @Override
  public boolean playerContainsLegalMove() {
    return !super.hasToPass();
  }

  @Override
  public int getPlayerScore(Player player) {
    PlayerTurn piece = player.getPlayer();
    if (piece == PlayerTurn.BLACK) {
      return super.getScore(RepresentativeColor.BLACK);
    }
    return super.getScore(RepresentativeColor.WHITE);
  }

  @Override
  public boolean coordinateContainsLegalMove(CellPosition cell) {
    Map<Direction, Integer> map = super.checkMove(convert(cell), super.getTurn());
    for (int i : map.values()) {
      if (i > 0) {
        return true;
      }
    }
    return false;
  }

  @Override
  public GamePiece contentOfGivenCell(CellPosition cell) {
    RepresentativeColor color = super.getColorAt(convert(cell));
    if (color == RepresentativeColor.BLACK) {
      return GamePiece.BLACK;
    } else if (color == RepresentativeColor.WHITE) {
      return GamePiece.WHITE;
    }
    return GamePiece.BLACK;
  }

  @Override
  public int pathLength(CellPosition cell) {
    int value = 0;
    if (super.getColorAt(convert(cell)) != RepresentativeColor.NONE) {
      return 0;
    }
    Map<Direction, Integer> map = super.checkMove(convert(cell), super.getTurn());
    for (int i : map.values()) {
      value += i;
    }
    return value;
  }

  @Override
  public PlayerTurn getCurrentPlayer() {
    try {
      if (super.getTurn() == RepresentativeColor.BLACK) {
        return PlayerTurn.BLACK;
      }
    } catch (IllegalStateException e) {
      return PlayerTurn.WHITE;
    }
    return PlayerTurn.WHITE;
  }

  @Override
  public void addFeatureListener(ModelFeatures features) {
    // we will add the corresponded listener in our method,
    // and our listener is used to update only.
  }

  @Override
  public void startGame(int boardSize) {
    super.startGame();
  }

  @Override
  public void passTurn() {
    super.makePass(super.getTurn());
  }

  @Override
  public void playTurn(CellPosition cell) {
    super.placeMove(convert(cell), super.getTurn());
  }

  /**
   * Convert from provider's coordinators system to our coordinator system by reversing their
   * x as the coordinator to represent our row and their z to represent our col.
   *
   * @param position the position in provider's coordinator system
   * @return the position in our coordinator system
   */
  private RowColPair convert(CellPosition position) {
    return new RowColPair(-position.getX(), position.getZ());
  }

  /**
   * Convert from our coordinators system to provider's coordinator system by reversing the
   * row int cubetrio system as their x, reversing right col as their y, using left col as
   * their z.
   *
   * @param pair the position in our coordinator system
   * @return the position in provider's coordinator system
   */
  private CellPosition convertBack(RowColPair pair) {
    CubeCoordinateTrio trio = pair.convertToCube();
    return new CellPosition(-trio.getRow(), -trio.getRightCol(), trio.getLeftCol());
  }

  /**
   * A Builder to the regular reversi model, the target is to the reducing the number of
   * constructor and supply flexibility and Step-by-Step Construction for the model.
   */
  public static class ModelBuilder {
    private static final int DEFAULT_SIZE = 6;
    private ModelStatus status;
    private int size;

    /**
     * Initialize the builder to standard reversi game with size 6 and a default
     * status class.
     */
    public ModelBuilder() {
      status = new ReversiModelStatus();
      size = DEFAULT_SIZE;
    }

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
    public CombineModel build() {
      return new CombineModel(size, status);
    }
  }
}
