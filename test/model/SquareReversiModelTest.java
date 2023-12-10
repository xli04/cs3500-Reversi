package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import model.AbstractModelTest;
import model.CellPiece;
import model.HexReversiModel;
import model.ModelDirection;
import model.MutableReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import model.SquareDirection;
import model.SquareReversiModel;
import view.ReversiTextualView;
import view.SquareTextualView;

import static org.junit.Assert.assertThrows;

public final class SquareReversiModelTest extends AbstractModelTest {

  @Override
  public MutableReversiModel createRiggedEmptyModel(int size) {
    return new SquareReversiModel(fillBoardWithColor(size, RepresentativeColor.NONE)
            , size, RepresentativeColor.BLACK);
  }

  @Override
  public MutableReversiModel createRiggedFullModel(int size) {
    return new SquareReversiModel(fillBoardWithColor(size, RepresentativeColor.BLACK),
            size, RepresentativeColor.BLACK);
  }

  @Override
  public MutableReversiModel createDefaultModel() {
    return new SquareReversiModel.ModelBuilder().build();
  }

  /**
   * make the board with the given size as the side length and set each cell
   * to contain a piece of the given color
   *
   * @param size  the given side length
   * @param color the color to fill the baord with
   * @return the 2d array represent the board
   */
  private Map<RowColPair, CellPiece> fillBoardWithColor(int size, RepresentativeColor color) {
    Map<RowColPair, CellPiece> board = new HashMap<>();
    int difference = size / 2 - 1;
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        RowColPair currPair = new RowColPair(row - difference, col - difference);
        CellPiece currPiece = new CellPiece(color);
        board.put(currPair, currPiece);
      }
    }
    return board;
  }

  private SquareReversiModel model;

  /**
   * Set up method annotated with Before to initialize a common state before each test case.
   * It creates a new instance of code SquareReversiModel using the model builder and starts
   * a new game.
   */
  @Before
  public void setUp() {
    // Initialize a new SquareReversiModel before each test
    model = new SquareReversiModel.ModelBuilder().build();
    model.startGame();
  }

  @Test
  public void testInitialConditions() {
    // Test initial conditions after constructing the model
    Assert.assertEquals(8, model.getSize());
    Assert.assertFalse(model.isGameOver());
    Assert.assertEquals(RepresentativeColor.BLACK, model.getTurn());
  }

  @Test
  public void testPlaceMove() {
    RowColPair move = new RowColPair(0, 2);
    model.placeMove(move, RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(move));
    Assert.assertEquals(RepresentativeColor.WHITE, model.getTurn());
  }

  @Test
  public void testPassTurn() {
    // Test passing the turn
    model.makePass(RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.WHITE, model.getTurn());
    model.makePass(RepresentativeColor.WHITE);
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testHasToPass() {
    // Test the condition when a player has to pass
    Assert.assertFalse(model.hasToPass());
    model.placeMove(new RowColPair(0, 2), RepresentativeColor.BLACK);
    model.makePass(RepresentativeColor.WHITE);
    model.placeMove(new RowColPair(1, -1), RepresentativeColor.BLACK);
    Assert.assertTrue(model.hasToPass());
  }

  @Test
  public void testGetWinner() {
    // Test determining the winner
    model.makePass(RepresentativeColor.BLACK);
    model.makePass(RepresentativeColor.WHITE);
    Assert.assertNull(model.getWinner());
  }

  @Test
  public void testGetScore() {
    // Test getting the score for a color
    Assert.assertEquals(2, model.getScore(RepresentativeColor.BLACK));
    Assert.assertEquals(2, model.getScore(RepresentativeColor.WHITE));
    RowColPair move = new RowColPair(0, 2);
    model.placeMove(move, RepresentativeColor.BLACK);
    Assert.assertEquals(4, model.getScore(RepresentativeColor.BLACK));
  }

  @Test
  public void testTextualView() {
    SquareTextualView view = new SquareTextualView(model);
    Assert.assertEquals(
            "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ X O _ _ _ \n"
                    + "_ _ _ O X _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n", view.toString());
  }

  @Test
  public void testTextualViewForGivenSize() {
    SquareReversiModel model1 = new SquareReversiModel.ModelBuilder().setSize(2).build();
    SquareTextualView view = new SquareTextualView(model1);
    Assert.assertEquals("X O\n"
            + "O X\n", view.toString());
  }

  @Test
  public void testRenderForGivenSize() {
    StringBuilder builder = new StringBuilder();
    SquareTextualView view = new SquareTextualView(
            new SquareReversiModel.ModelBuilder().setSize(2).build(), builder);
    view.display();
    Assert.assertEquals("X O\n"
            + "O X\n", builder.toString());
  }

  @Test
  public void testTextualViewRender() {
    StringBuilder builder = new StringBuilder();
    SquareTextualView view = new SquareTextualView(model, builder);
    view.display();
    Assert.assertEquals(
            "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ X O _ _ _ \n"
                    + "_ _ _ O X _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n", builder.toString());
  }

  @Test
  public void testTextualViewRenderUpdateAfterValidMove() {
    StringBuilder builder = new StringBuilder();
    SquareTextualView view = new SquareTextualView(model, builder);
    view.display();
    Assert.assertEquals(
            "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ X O _ _ _ \n"
                    + "_ _ _ O X _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n", builder.toString());
    model.placeMove(new RowColPair(0, 2), RepresentativeColor.BLACK);
    view.display();
    Assert.assertTrue(builder.toString().contains(
            "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ X X X _ _ \n"
                    + "_ _ _ O X _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _ \n"
                    + "_ _ _ _ _ _ _ _"));
  }

  /**
   * the original statement of board in row -1 is _XO, After place the white cell at the leftmost,
   * it will flip the middle cell to white, which will become OOO.
   */
  @Test
  public void testValidPlaceMoveFlipCells() {
    Assert.assertEquals(RepresentativeColor.NONE, model.getColorAt(new RowColPair(0, 2)));
    Assert.assertEquals(RepresentativeColor.WHITE, model.getColorAt(new RowColPair(0, 1)));
    model.placeMove(new RowColPair(0, 2), RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(0, 2)));
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(0, 1)));
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
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(0, 0)));
    assertThrows(IllegalStateException.class, ()
            -> model.checkMove(new RowColPair(0, 0), RepresentativeColor.BLACK));
  }

  @Test
  public void testPlaceMoveInvalidCoordinatorsOutOfBound() {
    assertThrows(IllegalArgumentException.class, ()
            -> model.placeMove(new RowColPair(100, -1), RepresentativeColor.BLACK));
  }

  /**
   * the original state of the board is _XO, we can not place a black cell in the
   * leftmost position, because there is no adjacent cell around it.
   */
  @Test
  public void testInValidPlaceMoveThrowException() {
    Assert.assertEquals(RepresentativeColor.NONE, model.getColorAt(new RowColPair(0, 2)));
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(0, 0)));
    assertThrows(IllegalStateException.class, ()
            -> model.placeMove(new RowColPair(0, -1), RepresentativeColor.BLACK));
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
    Assert.assertEquals(RepresentativeColor.NONE, model.getColorAt(new RowColPair(0, 2)));
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(0, 0)));
    Assert.assertEquals(RepresentativeColor.WHITE, model.getColorAt(new RowColPair(0, 1)));
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
   * "  _ _ _ _ _ _ _ _ _ \n"
   * " _ _ _ _ X O _ _ _ _ \n"
   * "_ _ _ _ O _ X _ _ _ _ \n"
   * we can not flip the cells in all the directions except the left,
   * if we place a black cell in (-1,7) it can flip the the cell in (-1,1)
   * thus, in the left direction, it should return one and other directions
   * should return 0.
   */
  @Test
  public void testCheckMoveReturnCorrectNumberWhenCanFlipCells() {
    Map<ModelDirection, Integer> map = model.checkMove(new RowColPair(0, 2),
            RepresentativeColor.BLACK);
    for (ModelDirection direction : SquareDirection.values()) {
      int num = map.get(direction);
      if (direction == SquareDirection.SQUARELEFT) {
        Assert.assertEquals(1, num);
      } else {
        Assert.assertEquals(0, num);
      }
    }
  }

  @Test
  public void testCheckMoveReturnCorrectNumberWhenCanNotFlipCells() {
    Map<ModelDirection, Integer> map = model.checkMove(new RowColPair(0, -1),
            RepresentativeColor.BLACK);
    for (SquareDirection hexDirection : SquareDirection.values()) {
      int num = map.get(hexDirection);
      Assert.assertEquals(0, num);
    }
  }

  @Test
  public void testInvalidPlaceCellNoSurroundingCells() {
    assertThrows(IllegalStateException.class, ()
            -> model.placeMove(new RowColPair(-3, -3), RepresentativeColor.BLACK));
  }

  @Test
  public void testInvalidPlaceCellOnExistingCells() {
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(0, 0)));
    assertThrows(IllegalStateException.class, ()
            -> model.placeMove(new RowColPair(0, 0), RepresentativeColor.BLACK));
  }

  /**
   * the row -1,0,1 looks like
   * " _ _ _ _ X O _ _ _ _ "
   * "_ _ _ _ O _ X _ _ _ _ "
   * " _ _ _ _ X O _ _ _ _ " place a black cell in(0,0) can not flip anything.
   */
  @Test
  public void testInvalidPlaceCanNotFlipAnything() {
    assertThrows(IllegalStateException.class, ()
            -> model.placeMove(new RowColPair(-3, -3), RepresentativeColor.BLACK));
  }

  @Test
  public void testGetWinnerWhenGameIsNotOver() {
    model.placeMove(new RowColPair(0, 2), RepresentativeColor.BLACK);
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
    model.placeMove(new RowColPair(1, 2), RepresentativeColor.WHITE);
    model.makePass(RepresentativeColor.BLACK);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testPlaceMoveFailedDoNotResetThePassTime() {
    model.makePass(RepresentativeColor.BLACK);
    assertThrows(IllegalArgumentException.class, ()
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
            new HexReversiModel.ModelBuilder().setSize(0).build());
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
            -> model.placeMove(new RowColPair(0, 2), RepresentativeColor.BLACK));
    assertThrows(IllegalStateException.class, ()
            -> model.checkMove(new RowColPair(0, 2), RepresentativeColor.BLACK));
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
    model.placeMove(new RowColPair(0, 2), RepresentativeColor.BLACK);
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
    model.placeMove(new RowColPair(0, 2), RepresentativeColor.BLACK);
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
            -> model.placeMove(new RowColPair(0, 2), RepresentativeColor.BLACK));
    assertThrows(IllegalStateException.class, ()
            -> model.checkMove(new RowColPair(0, 2), RepresentativeColor.BLACK));
    assertThrows(IllegalStateException.class, ()
            -> model.getTurn());
  }

  @Test
  public void testSomeActionAllowedAfterGameOver() {
    model.makePass(RepresentativeColor.BLACK);
    model.makePass(RepresentativeColor.WHITE);
    Assert.assertTrue(model.isGameOver());
    Assert.assertEquals(RepresentativeColor.WHITE, model.getColorAt(new RowColPair(1, 0)));
    Assert.assertEquals(8, model.getSize());
    Assert.assertNull(model.getWinner());
  }

  @Test
  public void testInvalidBoardSize() {
    assertThrows(IllegalArgumentException.class, ()
            -> new HexReversiModel.ModelBuilder().setSize(1).build());
    assertThrows(IllegalArgumentException.class, ()
            -> new HexReversiModel.ModelBuilder().setSize(-1).build());
  }

  @Test
  public void testUserActionMustFollowTurnDuringGame() {
    Assert.assertEquals(RepresentativeColor.BLACK, model.getTurn());
    assertThrows(IllegalStateException.class, ()
            -> model.placeMove(new RowColPair(1, -1), RepresentativeColor.WHITE));
    assertThrows(IllegalStateException.class, ()
            -> model.makePass(RepresentativeColor.WHITE));
    model.makePass(RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.WHITE, model.getTurn());
    assertThrows(IllegalStateException.class, ()
            -> model.placeMove(new RowColPair(0, 2), RepresentativeColor.BLACK));
    assertThrows(IllegalStateException.class, ()
            -> model.makePass(RepresentativeColor.BLACK));
  }

  @Test
  public void testDeepCopy() {
    Map<RowColPair, CellPiece> board = model.getBoard();
    Assert.assertEquals(RepresentativeColor.NONE, board.get(new RowColPair(0, 2)).getColor());
    model.placeMove(new RowColPair(0, 2), RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.NONE, board.get(new RowColPair(0, 2)).getColor());
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(0, 2)));
  }

//  @Test
//  public void tryPlaceMoveThrowsExceptionForNullTurn() {
//    //before the game has started, the turn is null
//    SquareReversiModel unstartedModel = new SquareReversiModel.ModelBuilder().build();
//    RowColPair validPair = new RowColPair(0, 2);
//    RepresentativeColor turn = null;
//    assertThrows(IllegalArgumentException.class, () -> model.placeMove(validPair, turn));
//  }

  @Test
  public void squareBoardDisallowsStartGameBeingCalledTwice(){
    SquareReversiModel unstartedModel = new SquareReversiModel.ModelBuilder().build();
    unstartedModel.startGame();
    assertThrows(IllegalStateException.class, unstartedModel::startGame);
  }

  @Test
  public void squareBoardPreventsMutationBeforeGameHasStarted(){
    SquareReversiModel unstartedModel = new SquareReversiModel.ModelBuilder().build();
    assertThrows(IllegalStateException.class, () ->
            unstartedModel.placeMove(new RowColPair(0,3), RepresentativeColor.BLACK));
  }


}
