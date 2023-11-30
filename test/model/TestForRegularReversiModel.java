package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import model.CubeCoordinateTrio;
import model.Direction;
import model.Hexagon;
import model.RepresentativeColor;
import model.MutableReversiModel;
import model.RegularReversiModel;
import model.RowColPair;
import view.ReversiTextualView;

import static org.junit.Assert.assertThrows;

/**
 * test cases for regular reversi model.
 */
public class TestForRegularReversiModel {
  MutableReversiModel model;

  @Before
  public void setUp() {
    model = new RegularReversiModel.ModelBuilder().build();
    model.startGame();
  }

  @Test
  public void testTextualView() {
    ReversiTextualView view = new ReversiTextualView(model);
    Assert.assertEquals(
        "     _ _ _ _ _ _ \n"
        +
        "    _ _ _ _ _ _ _ \n"
        +
        "   _ _ _ _ _ _ _ _ \n"
        +
        "  _ _ _ _ _ _ _ _ _ \n"
        +
        " _ _ _ _ X O _ _ _ _ \n"
        +
        "_ _ _ _ O _ X _ _ _ _ \n"
        +
        " _ _ _ _ X O _ _ _ _ \n"
        +
        "  _ _ _ _ _ _ _ _ _ \n"
        +
        "   _ _ _ _ _ _ _ _ \n"
        +
        "    _ _ _ _ _ _ _ \n"
        +
        "     _ _ _ _ _ _ ", view.toString());
  }

  @Test
  public void testTextualViewForGivenSize() {
    ReversiTextualView view = new ReversiTextualView(
        new RegularReversiModel.ModelBuilder().setSize(2).build());
    Assert.assertEquals(" X O \n"
        +
        "O _ X \n"
        +
        " X O ", view.toString());
  }

  @Test
  public void testRenderForGivenSize() {
    StringBuilder builder = new StringBuilder();
    ReversiTextualView view = new ReversiTextualView(
        new RegularReversiModel.ModelBuilder().setSize(2).build(), builder);
    view.render();
    Assert.assertEquals(" X O \n"
        +
        "O _ X \n"
        +
        " X O ", builder.toString());
  }

  @Test
  public void testTextualViewRender() {
    StringBuilder builder = new StringBuilder();
    ReversiTextualView view = new ReversiTextualView(model, builder);
    view.render();
    Assert.assertEquals(
        "     _ _ _ _ _ _ \n"
        +
        "    _ _ _ _ _ _ _ \n"
        +
        "   _ _ _ _ _ _ _ _ \n"
        +
        "  _ _ _ _ _ _ _ _ _ \n"
        +
        " _ _ _ _ X O _ _ _ _ \n"
        +
        "_ _ _ _ O _ X _ _ _ _ \n"
        +
        " _ _ _ _ X O _ _ _ _ \n"
        +
        "  _ _ _ _ _ _ _ _ _ \n"
        +
        "   _ _ _ _ _ _ _ _ \n"
        +
        "    _ _ _ _ _ _ _ \n"
        +
        "     _ _ _ _ _ _ ", builder.toString());
  }

  @Test
  public void testTextualViewRenderUpdateAfterValidMove() {
    StringBuilder builder = new StringBuilder();
    ReversiTextualView view = new ReversiTextualView(model, builder);
    view.render();
    Assert.assertEquals(
        "     _ _ _ _ _ _ \n"
        +
        "    _ _ _ _ _ _ _ \n"
        +
        "   _ _ _ _ _ _ _ _ \n"
        +
        "  _ _ _ _ _ _ _ _ _ \n"
        +
        " _ _ _ _ X O _ _ _ _ \n"
        +
        "_ _ _ _ O _ X _ _ _ _ \n"
        +
        " _ _ _ _ X O _ _ _ _ \n"
        +
        "  _ _ _ _ _ _ _ _ _ \n"
        +
        "   _ _ _ _ _ _ _ _ \n"
        +
        "    _ _ _ _ _ _ _ \n"
        +
        "     _ _ _ _ _ _ ", builder.toString());
    model.placeMove(new RowColPair(-1, 2), RepresentativeColor.BLACK);
    view.render();
    Assert.assertTrue(builder.toString().contains(
        "     _ _ _ _ _ _ \n"
        +
        "    _ _ _ _ _ _ _ \n"
        +
        "   _ _ _ _ _ _ _ _ \n"
        +
        "  _ _ _ _ _ _ _ _ _ \n"
        +
        " _ _ _ _ X X X _ _ _ \n"
        +
        "_ _ _ _ O _ X _ _ _ _ \n"
        +
        " _ _ _ _ X O _ _ _ _ \n"
        +
        "  _ _ _ _ _ _ _ _ _ \n"
        +
        "   _ _ _ _ _ _ _ _ \n"
        +
        "    _ _ _ _ _ _ _ \n"
        +
        "     _ _ _ _ _ _ "));
  }

  /**
   * the original statement of board in row -1 is _XO, After place the white cell at the leftmost,
   * it will flip the middle cell to white, which will become OOO.
   */
  @Test
  public void testValidPlaceMoveFlipCells() {
    Assert.assertEquals(RepresentativeColor.NONE, model.getColorAt(new RowColPair(-1, 2)));
    Assert.assertEquals(RepresentativeColor.WHITE, model.getColorAt(new RowColPair(-1, 1)));
    model.placeMove(new RowColPair(-1, 2), RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(-1, 2)));
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(-1, 1)));
  }

  @Test
  public void testCheckMoveInvalidCoordinators() {
    assertThrows(IllegalArgumentException.class, ()
        -> model.checkMove(new RowColPair(0, 100), RepresentativeColor.BLACK));
    assertThrows(IllegalArgumentException.class, ()
        -> model.checkMove(new RowColPair(-1, -100), RepresentativeColor.BLACK));
  }

  @Test
  public void testCheckMoveInvalidCoordinatorsOnExistingCells() {
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(-1, 0)));
    assertThrows(IllegalStateException.class, ()
        -> model.checkMove(new RowColPair(-1, 0), RepresentativeColor.BLACK));
  }

  @Test
  public void testPlaceMoveInvalidCoordinatorsOutOfBound() {
    assertThrows(IllegalArgumentException.class, ()
        -> model.placeMove(new RowColPair(100, -1), RepresentativeColor.BLACK));
  }

  /**
   * the original state of the board is __XO, we can not place a black cell in the
   * leftmost position, because there is no adjacent cell around it.
   */
  @Test
  public void testInValidPlaceMoveThrowException() {
    Assert.assertEquals(RepresentativeColor.NONE, model.getColorAt(new RowColPair(-1, -1)));
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(-1, 0)));
    assertThrows(IllegalStateException.class, ()
        -> model.placeMove(new RowColPair(-1, -2), RepresentativeColor.BLACK));
  }

  @Test
  public void testInvalidGetColorAtThrowException() {
    assertThrows(IllegalArgumentException.class, ()
        -> model.getColorAt(new RowColPair(-100, 1)));
    assertThrows(IllegalArgumentException.class, ()
        -> model.getColorAt(new RowColPair(0, 100)));
  }

  @Test
  public void testValidGetColorAt() {
    Assert.assertEquals(RepresentativeColor.NONE, model.getColorAt(new RowColPair(-1, -1)));
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(-1, 0)));
    Assert.assertEquals(RepresentativeColor.WHITE, model.getColorAt(new RowColPair(-1, 1)));
  }

  @Test
  public void testCheckPassFalseWhenThereIsValidMove() {
    Assert.assertFalse(model.hasToPass());
    model.makePass(RepresentativeColor.BLACK);
    Assert.assertFalse(model.hasToPass());
  }

  @Test
  public void testGameOver() {
    model.makePass(RepresentativeColor.BLACK);
    model.makePass(RepresentativeColor.WHITE);
    Assert.assertTrue(model.isGameOver());
  }

  /**
   * The row -2,-1,0 in the board looks like
   *         "  _ _ _ _ _ _ _ _ _ \n"
   *         " _ _ _ _ X O _ _ _ _ \n"
   *         "_ _ _ _ O _ X _ _ _ _ \n"
   * we can not flip the cells in all the directions except the left,
   * if we place a black cell in (-1,7) it can flip the the cell in (-1,1)
   * thus, in the left direction, it should return one and other directions
   * should return 0.
   */
  @Test
  public void testCheckMoveReturnCorrectNumberWhenCanFlipCells() {
    Map<Direction, Integer> map = model.checkMove(new RowColPair(-1, 2), RepresentativeColor.BLACK);
    for (Direction direction : Direction.values()) {
      int num = map.get(direction);
      if (direction == Direction.LEfT) {
        Assert.assertEquals(1, num);
      } else {
        Assert.assertEquals(0, num);
      }
    }
  }

  @Test
  public void testCheckMoveReturnCorrectNumberWhenCanNotFlipCells() {
    Map<Direction, Integer> map = model.checkMove(new RowColPair(0, 0), RepresentativeColor.BLACK);
    for (Direction direction : Direction.values()) {
      int num = map.get(direction);
      Assert.assertEquals(0, num);
    }
  }

  @Test
  public void testInvalidPlaceCellNoSurroundingCells() {
    assertThrows(IllegalStateException.class, ()
        -> model.placeMove(new RowColPair(3, 0), RepresentativeColor.BLACK));
  }

  @Test
  public void testInvalidPlaceCellOnExistingCells() {
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(-1, 0)));
    assertThrows(IllegalStateException.class, ()
        -> model.placeMove(new RowColPair(0, -1), RepresentativeColor.BLACK));
  }

  /**
   * the row -1,0,1 looks like
   *" _ _ _ _ X O _ _ _ _ "
   *"_ _ _ _ O _ X _ _ _ _ "
   *" _ _ _ _ X O _ _ _ _ " place a black cell in(0,0) can not flip anything.
   */
  @Test
  public void testInvalidPlaceCanNotFlipAnything() {
    assertThrows(IllegalStateException.class, ()
        -> model.placeMove(new RowColPair(0, 0), RepresentativeColor.BLACK));
  }

  @Test
  public void testInvalidPlaceCanNotFormStraightLine() {
    assertThrows(IllegalStateException.class, ()
        -> model.placeMove(new RowColPair(0, 0), RepresentativeColor.BLACK));
  }

  @Test
  public void testGetWinnerWhenGameIsNotOver() {
    model.placeMove(new RowColPair(-1, 2), RepresentativeColor.BLACK);
    assertThrows(IllegalStateException.class, ()
        -> model.getWinner());
  }

  /**
   * if players pass twice the game will end, but if either of the players placed a move
   * successfully, the passTimes will be reset.
   */
  @Test
  public void testPlaceMoveSuccessfullyResetThePassTime() {
    model.makePass(RepresentativeColor.BLACK);
    model.placeMove(new RowColPair(-1, -1), RepresentativeColor.WHITE);
    model.makePass(RepresentativeColor.BLACK);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testPlaceMoveFailedDoNotResetThePassTime() {
    model.makePass(RepresentativeColor.BLACK);
    assertThrows(IllegalStateException.class, ()
        -> model.placeMove(new RowColPair(0, 5), RepresentativeColor.WHITE));
    model.makePass(RepresentativeColor.WHITE);
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testViewThrowsExceptionForNullModel() {
    assertThrows(IllegalArgumentException.class, () -> new ReversiTextualView(null));
    assertThrows(IllegalArgumentException.class, () -> new ReversiTextualView(model, null));
  }

  @Test
  public void testModelDisallowsInvalidSize() {
    assertThrows(IllegalArgumentException.class, () ->
        new RegularReversiModel.ModelBuilder().setSize(0).build());
  }

  @Test
  public void testInvalidColorGetOpposite() {
    assertThrows(IllegalArgumentException.class, ()
        -> RepresentativeColor.NONE.getOpposite());
  }

  @Test
  public void testCallMethodsAlreadyGameOver() {
    model.makePass(RepresentativeColor.BLACK);
    model.makePass(RepresentativeColor.WHITE);
    Assert.assertTrue(model.isGameOver());
    assertThrows(IllegalStateException.class, ()
        -> model.placeMove(new RowColPair(-1, -1), RepresentativeColor.BLACK));
    assertThrows(IllegalStateException.class, ()
        -> model.checkMove(new RowColPair(-1, -1), RepresentativeColor.BLACK));
    assertThrows(IllegalStateException.class, ()
        -> model.makePass(RepresentativeColor.WHITE));
  }

  /**
   * the row 0 looks like "_ _ _ _ O _ X _ _ _ _", if we put a cell in 3th position,
   * it is next to a white cell but it can not flip anything.
   */
  @Test
  public void testInvalidPlaceHasAdjacentCellsCanNotFlipAnything() {
    assertThrows(IllegalStateException.class, ()
        -> model.placeMove(new RowColPair(0, -2), RepresentativeColor.BLACK));
  }

  @Test
  public void testGetWinnerBlackWinWhenGameOver() {
    model.placeMove(new RowColPair(-1, 2), RepresentativeColor.BLACK);
    model.placeMove(new RowColPair(1, -2), RepresentativeColor.WHITE);
    model.placeMove(new RowColPair(2, -3), RepresentativeColor.BLACK);
    model.makePass(RepresentativeColor.WHITE);
    model.makePass(RepresentativeColor.BLACK);
    Assert.assertTrue(model.isGameOver());
    Assert.assertEquals(RepresentativeColor.BLACK, model.getWinner());
  }

  @Test
  public void testGetWinnerTieGameWhenGameOver() {
    model.makePass(RepresentativeColor.BLACK);
    model.makePass(RepresentativeColor.WHITE);
    Assert.assertTrue(model.isGameOver());
    Assert.assertNull(model.getWinner());
  }

  @Test
  public void testPlaceMoveSuccessfullyAlterTheTurn() {
    Assert.assertEquals(RepresentativeColor.BLACK, model.getTurn());
    model.placeMove(new RowColPair(-1, 2), RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.WHITE, model.getTurn());
  }

  @Test
  public void testPlaceMoveFailedNotAlterTheTurn() {
    Assert.assertEquals(RepresentativeColor.BLACK, model.getTurn());
    assertThrows(IllegalArgumentException.class, ()
        -> model.placeMove(new RowColPair(-100, -1), RepresentativeColor.BLACK));
    Assert.assertEquals(RepresentativeColor.BLACK, model.getTurn());
  }

  @Test
  public void testMakePassAlterTheTurn() {
    Assert.assertEquals(RepresentativeColor.BLACK, model.getTurn());
    model.makePass(RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.WHITE, model.getTurn());
  }

  @Test
  public void testMakePassAlterTheTurnAndEndGame() {
    Assert.assertEquals(RepresentativeColor.BLACK, model.getTurn());
    model.makePass(RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.WHITE, model.getTurn());
    model.makePass(RepresentativeColor.WHITE);
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testSomeActionNotAllowedAfterGameOver() {
    model.makePass(RepresentativeColor.BLACK);
    model.makePass(RepresentativeColor.WHITE);
    Assert.assertTrue(model.isGameOver());
    assertThrows(IllegalStateException.class, ()
        -> model.makePass(RepresentativeColor.BLACK));
    assertThrows(IllegalStateException.class, ()
        -> model.placeMove(new RowColPair(-1, 2), RepresentativeColor.BLACK));
    assertThrows(IllegalStateException.class, ()
        -> model.checkMove(new RowColPair(-1, 2), RepresentativeColor.BLACK));
    assertThrows(IllegalStateException.class, ()
        -> model.getTurn());
  }

  @Test
  public void testSomeActionAllowedAfterGameOver() {
    model.makePass(RepresentativeColor.BLACK);
    model.makePass(RepresentativeColor.WHITE);
    Assert.assertTrue(model.isGameOver());
    Assert.assertEquals(RepresentativeColor.WHITE, model.getColorAt(new RowColPair(-1, 1)));
    Assert.assertEquals(6, model.getSize());
    Assert.assertNull(model.getWinner());
  }

  @Test
  public void testInvalidBoardSize() {
    assertThrows(IllegalArgumentException.class, ()
        -> new RegularReversiModel.ModelBuilder().setSize(1).build());
    assertThrows(IllegalArgumentException.class, ()
        -> new RegularReversiModel.ModelBuilder().setSize(-1).build());
  }

  @Test
  public void testUserActionMustFollowTurnDuringGame() {
    Assert.assertEquals(RepresentativeColor.BLACK, model.getTurn());
    assertThrows(IllegalStateException.class, ()
        -> model.placeMove(new RowColPair(-1, -1), RepresentativeColor.WHITE));
    assertThrows(IllegalStateException.class, ()
        -> model.makePass(RepresentativeColor.WHITE));
    model.makePass(RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.WHITE, model.getTurn());
    assertThrows(IllegalStateException.class, ()
        -> model.placeMove(new RowColPair(-1, 2), RepresentativeColor.BLACK));
    assertThrows(IllegalStateException.class, ()
        -> model.makePass(RepresentativeColor.BLACK));
  }

  @Test
  public void testSystemConvert() {
    CubeCoordinateTrio cube = new CubeCoordinateTrio(0, -1, 1);
    Assert.assertEquals(new RowColPair(0, -1), cube.convertToRowCol());
    RowColPair rowCol = new RowColPair(1, 1);
    Assert.assertEquals(new CubeCoordinateTrio(1, 1, -2), rowCol.convertToCube());
  }

  @Test
  public void testDeepCopy() {
    Map<RowColPair, Hexagon> board = model.getBoard();
    Assert.assertEquals(RepresentativeColor.NONE, board.get(new RowColPair(-1, 2)).getColor());
    model.placeMove(new RowColPair(-1, 2), RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.NONE, board.get(new RowColPair(-1, 2)).getColor());
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(-1, 2)));
  }
}
