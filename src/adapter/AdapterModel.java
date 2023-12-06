package adapter;

import java.util.HashMap;
import java.util.Map;

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
    return ProviderTranslator.convertColorToProviderColor(model.getWinner());
  }

  @Override
  public Map<CellPosition, GamePiece> getBoard() {
    Map<RowColPair, Hexagon> ourBoard = model.getCurrentBoard();
    Map<CellPosition, GamePiece> providerBoard = new HashMap<>();
    for (RowColPair pair : ourBoard.keySet()) {
      GamePiece piece = ProviderTranslator.convertColorToProviderColor(
              ourBoard.get(pair).getColor());
      providerBoard.put(ProviderTranslator.convertToCellPosition(pair), piece);
    }
    return providerBoard;
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
    return ProviderTranslator.countMoves(model,cell) > 0;
  }

  @Override
  public GamePiece contentOfGivenCell(CellPosition cell) {
    RepresentativeColor color = model.getColorAt(ProviderTranslator.convertToRowColPair(cell));
    return ProviderTranslator.convertColorToProviderColor(color);
  }

  @Override
  public int pathLength(CellPosition cell) {
    if (model.getColorAt(ProviderTranslator.convertToRowColPair(cell)) != RepresentativeColor.NONE) {
      return 0;
    }
    return ProviderTranslator.countMoves(model, cell);
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
    model.placeMove(ProviderTranslator.convertToRowColPair(cell), model.getTurn());
  }

  /**
   *
   */
  private RepresentativeColor translateTurnToRepColor(PlayerTurn turn) {
    //the only possible values for player turn are black and white
    return turn == PlayerTurn.BLACK ? RepresentativeColor.BLACK : RepresentativeColor.WHITE;
  }
}
