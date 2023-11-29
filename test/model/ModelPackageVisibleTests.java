package model;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a test class for the Regular Reversi Model that package-visible functionality that
 * isn’t part of our model interface.
 */
public class ModelPackageVisibleTests {
  /**
   * make the board with the given size as the side length.
   *
   * @param size the given side length
   * @return the 2d array represent the board
   */
  private Map<RowColPair, Hexagon> makeBoard(int size) {
    Map<RowColPair, Hexagon> board = new HashMap<>();
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
    return board;
  }

  /**
   * the board do not have any cells, thus there is no valid move for black cells ot white cells.
   */
  @Test
  public void testCheckPassTrueWhenThereIsNoValidMove() {
    Map<RowColPair, Hexagon> riggedBoard = makeBoard(5);
    MutableReversiModel nothingBoard = new RegularReversiModel.Builder(new ReversiModelStatus())
            .setBoard(riggedBoard).setSize(5).setTurn(RepresentativeColor.BLACK).build();
    nothingBoard.startGame();
    Assert.assertTrue(nothingBoard.hasToPass());
    nothingBoard.makePass(RepresentativeColor.BLACK);
    Assert.assertTrue(nothingBoard.hasToPass());
  }

  /**
   * the row 0 will looks like
   * _ _ _ _ O _ O X _ _ _ and after placed a black cell in (5,5),
   * it will becomes _ _ _ _ O X X X _ _ _. rather than  _ _ _ _ O O O X _ _ _
   */
  @Test
  public void testCellPlaceBetweenTwoOppositeColorWillNotBeFlipped() {
    Map<RowColPair, Hexagon> board = makeBoard(6);
    board.put(new RowColPair(0, -1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(0, 1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(0, 2), new Hexagon(RepresentativeColor.BLACK));
    MutableReversiModel model = new RegularReversiModel.Builder(new ReversiModelStatus())
            .setBoard(board).setSize(6).setTurn(RepresentativeColor.BLACK).build();
    model.startGame();
    Assert.assertEquals(RepresentativeColor.NONE, model.getColorAt(new RowColPair(0, 0)));
    model.placeMove(new RowColPair(0, 0), RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(0, 0)));
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(0, 1)));
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(0, 2)));
  }

  /**
   * the row -1,0,1 will looks like
   * _ _ _ X O O _ _ _ _
   * _ _ _ _ _ _ O _ _ _ _
   * _ _ _ _ X X _ _ _ _
   * after we placed a black cell in (-1,2) it looks like
   * _ _ _ X X X X _ _ _
   * _ _ _ _ _ _ X _ _ _ _
   * _ _ _ _ X X _ _ _ _
   * flip three white cells in two directions.
   */
  @Test
  public void testPlaceMoveFlipMultipleCardsInDifferentDirections() {
    Map<RowColPair, Hexagon> board = makeBoard(6);
    board.put(new RowColPair(-1, -1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-1, 0), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-1, 1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(0, 1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(1, 0), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(1, -1), new Hexagon(RepresentativeColor.BLACK));
    MutableReversiModel model = new RegularReversiModel.Builder(new ReversiModelStatus())
            .setBoard(board).setSize(6).setTurn(RepresentativeColor.BLACK).build();
    model.startGame();
    Assert.assertEquals(RepresentativeColor.NONE, model.getColorAt(new RowColPair(-1, 2)));
    model.placeMove(new RowColPair(-1, 2), RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(-1, 1)));
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(-1, 0)));
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(0, 1)));
  }

  @Test
  public void testGetWinnerWhiteWinWhenGameOver() {
    Map<RowColPair, Hexagon> board = makeBoard(6);
    board.put(new RowColPair(0, 0), new Hexagon(RepresentativeColor.WHITE));
    MutableReversiModel model = new RegularReversiModel.Builder(new ReversiModelStatus())
            .setBoard(board).setSize(6).setTurn(RepresentativeColor.BLACK).build();
    model.startGame();
    Assert.assertTrue(model.hasToPass());
    model.makePass(RepresentativeColor.BLACK);
    Assert.assertTrue(model.hasToPass());
    model.makePass(RepresentativeColor.WHITE);
    Assert.assertTrue(model.isGameOver());
    Assert.assertEquals(RepresentativeColor.WHITE, model.getWinner());
  }

  /**
   * even though the board is empty and no valid move exist, our game will not end automaticAlly.
   */
  @Test
  public void testGameWillNotEndAutomatically() {
    Map<RowColPair, Hexagon> board = makeBoard(6);
    MutableReversiModel model = new RegularReversiModel.Builder(new ReversiModelStatus())
            .setBoard(board).setSize(6).setTurn(RepresentativeColor.BLACK).build();
    model.startGame();
    Assert.assertTrue(model.hasToPass()); // Black player has to pass
    model.makePass(RepresentativeColor.BLACK); // Black Player pass
    Assert.assertTrue(model.hasToPass()); // White player also need to pass
    Assert.assertFalse(model.isGameOver());
    // the pass time is 1 right now, so game will not end
  }

}
