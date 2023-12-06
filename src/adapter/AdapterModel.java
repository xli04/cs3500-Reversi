package adapter;

import java.util.HashMap;
import java.util.Map;

import model.CubeCoordinateTrio;
import model.Direction;
import model.Hexagon;
import model.MutableReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import reversi.provider.controller.Player;
import reversi.provider.model.CellPosition;
import reversi.provider.model.GamePiece;
import reversi.provider.model.ModelFeatures;
import reversi.provider.model.PlayerTurn;
import reversi.provider.model.ReversiModel;

/**
 * Combine model, represents a adapter pattern on our model and provider's model, the key
 * idea is using the methods in our model to implement the methods in the provider's model
 * interface so that their view and our view can placing in a same combine model.
 */
public class AdapterModel implements ReversiModel {
  private final MutableReversiModel model;

  /**
   * initialize the game with the given size. the 2 should be the smallest size for a board
   * to put in all the pre-positioned cells.
   *
   * @param model our model servers as a delegation
   */
  public AdapterModel(MutableReversiModel model) {
    this.model = model;
  }

  /**
   * initialize the game with the given size. the 2 should be the smallest size for a board
   * to put in all the pre-positioned cells.
   */

  @Override
  public boolean isGameOver() {
    return model.isGameOver();
  }

  @Override
  public GamePiece winner() {
    RepresentativeColor color = model.getWinner();
    if (color == RepresentativeColor.WHITE) {
      return GamePiece.WHITE;
    } else if (color == RepresentativeColor.BLACK) {
      return GamePiece.BLACK;
    }
    return GamePiece.EMPTY;
  }

  @Override
  public Map<CellPosition, GamePiece> getBoard() {
    Map<RowColPair, Hexagon> ourBoard = model.getCurrentBoard();
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
    return model.getSize();
  }

  @Override
  public boolean playerContainsLegalMove() {
    return !model.hasToPass();
  }

  @Override
  public int getPlayerScore(Player player) {
    PlayerTurn piece = player.getPlayer();
    return model.getScore(translateTurnToRepColor(piece));
  }

  @Override
  public boolean coordinateContainsLegalMove(CellPosition cell) {
    Map<Direction, Integer> map = model.checkMove(convert(cell), model.getTurn());
    for (int i : map.values()) {
      if (i > 0) {
        return true;
      }
    }
    return false;
  }

  @Override
  public GamePiece contentOfGivenCell(CellPosition cell) {
    RepresentativeColor color = model.getColorAt(convert(cell));
    if (color == RepresentativeColor.BLACK) {
      return GamePiece.BLACK;
    } else if (color == RepresentativeColor.WHITE) {
      return GamePiece.WHITE;
    }
    return GamePiece.EMPTY;
  }

  @Override
  public int pathLength(CellPosition cell) {
    int value = 0;
    if (model.getColorAt(convert(cell)) != RepresentativeColor.NONE) {
      return 0;
    }
    Map<Direction, Integer> map = model.checkMove(convert(cell), model.getTurn());
    for (int i : map.values()) {
      value += i;
    }
    return value;
  }

  @Override
  public PlayerTurn getCurrentPlayer() {
    try {
      if (model.getTurn() == RepresentativeColor.BLACK) {
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
    model.startGame();
  }

  @Override
  public void passTurn() {
    model.makePass(model.getTurn());
  }

  @Override
  public void playTurn(CellPosition cell) {
    model.placeMove(convert(cell), model.getTurn());
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
   *
   */
  private RepresentativeColor translateTurnToRepColor(PlayerTurn turn) {
    //the only possible values for player turn are black and white
    return turn == PlayerTurn.BLACK ? RepresentativeColor.BLACK : RepresentativeColor.WHITE;
  }
}
