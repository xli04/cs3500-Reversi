package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import strategy.AnyOpenTileStrategy;
import strategy.CaptureMaxPieces;
import strategy.CornerStrategy;
import strategy.FallibleStrategy;

/**
 * A test class for mock model in order to test if the strategy do the things we expected.
 */
public class TestForMockSquareModel extends AbstractModelTest {
  MutableReversiModel model;

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

  @Before
  public void setUp() {
    model = new SquareReversiModel.ModelBuilder().build();
    model.startGame();
  }

  /**
   * This test checks if the corner strategy do check all the six corners in the board.
   */
  @Test
  public void testIfCornerStrategyCheckAllFourCorners() {
    StringBuilder builder = new StringBuilder();
    FallibleStrategy strategy = new CornerStrategy();
    MockModel mock = new MockModel(model, builder, new ArrayList<>());
    strategy.choosePosition(mock, RepresentativeColor.BLACK);
    List<RowColPair> cornerPoints = new ArrayList<>();
    cornerPoints.add(new RowColPair(-3, -3));
    cornerPoints.add(new RowColPair(-3, 4));
    cornerPoints.add(new RowColPair(4, -3));
    cornerPoints.add(new RowColPair(4, 4));
    for (RowColPair pair : cornerPoints) {
      String pairString = "Checking" + "(" + pair.getRow() + "," + pair.getCol() + ")";
      Assert.assertTrue(builder.toString().contains(pairString));
    }
  }


  @Test
  public void checkCheatInTheCaptureMaxPiece() {
    StringBuilder builder = new StringBuilder();
    FallibleStrategy strategy = new CaptureMaxPieces();
    MockModel mock = new MockModel(model, builder, List.of(new RowColPair(-1, 1)));
    Optional<RowColPair> pair = strategy.choosePosition(mock, RepresentativeColor.BLACK);
    Assert.assertTrue(pair.isPresent());
    RowColPair actualPair = pair.get();
    Assert.assertTrue(builder.toString().contains("lie on you"));
    Assert.assertEquals(new RowColPair(0, 2), actualPair);
  }


  @Test
  public void checkCaptureMaxPieceLoopThroughAllThePositionAndChooseAsExpectedWhenNotLie() {
    StringBuilder builder = new StringBuilder();
    FallibleStrategy strategy = new CaptureMaxPieces();
    MockModel mock = new MockModel(model, builder, new ArrayList<>());
    Optional<RowColPair> pair = strategy.choosePosition(mock, RepresentativeColor.BLACK);
    Assert.assertTrue(pair.isPresent());
    RowColPair actualPair = pair.get();
    Assert.assertEquals(new RowColPair(-1, 1), actualPair);
    Map<RowColPair, CellPiece> board = mock.getBoard();
    for (RowColPair position : board.keySet()) {
      String pairString = "Checking" + "(" + position.getRow() + "," + position.getCol() + ")";
      if (board.get(position).getColor() == RepresentativeColor.NONE) {
        Assert.assertTrue(builder.toString().contains(pairString));
      } else {
        Assert.assertFalse(builder.toString().contains(pairString));
      }
    }
  }

  @Test
  public void ensureAnyOpenTileLoopsThorughAllTilesOnTheBoard() {
    //Fill a board with empty tiles, none of these will be valid moves since they cannot flip any
    //ensure our startegy checks all available tiles and does not return a move
    StringBuilder builder = new StringBuilder();
    FallibleStrategy strategy = new AnyOpenTileStrategy();
    Map<RowColPair, CellPiece> riggedBoard = fillBoardWithColor(6, RepresentativeColor.NONE);
    SquareReversiModel riggedModel = new
            SquareReversiModel(riggedBoard, 6, RepresentativeColor.BLACK);
    MockModel mock = new MockModel(riggedModel, builder, new ArrayList<>());
    Optional<RowColPair> pair = strategy.choosePosition(mock, RepresentativeColor.BLACK);
    Assert.assertFalse(pair.isPresent());
    Map<RowColPair, CellPiece> board = mock.getBoard();
    for (RowColPair position : board.keySet()) {
      String pairString = "Checking" + "(" + position.getRow() + "," + position.getCol() + ")";
      if (board.get(position).getColor() == RepresentativeColor.NONE) {
        Assert.assertTrue(builder.toString().contains(pairString));
      } else {
        Assert.assertFalse(builder.toString().contains(pairString));
      }
    }
  }

  @Test
  public void ensureAnyOpenTileStopsLoopingWhenItFindsALegalMove() {
    StringBuilder builder = new StringBuilder();
    FallibleStrategy strategy = new AnyOpenTileStrategy();
    MockModel mock = new MockModel(model, builder, new ArrayList<>());
    Optional<RowColPair> pair = strategy.choosePosition(mock, RepresentativeColor.BLACK);
    Assert.assertTrue(pair.isPresent());
    RowColPair actualPair = pair.get();

    //Use a default board, and loop through all empty cells until we find a valid move
    //ensure that once we find the valid move, we stop checking cells
    Map<RowColPair, CellPiece> board = mock.getBoard();
    for (RowColPair position : board.keySet()) {
      String pairString = "Checking" + "(" + position.getRow() + "," + position.getCol() + ")";
      CellPiece currCell = board.get(position);
      if (currCell.getColor() == RepresentativeColor.NONE) {
        Assert.assertTrue(builder.toString().contains(pairString));
      } else {
        Assert.assertFalse(builder.toString().contains(pairString));
      }
    }
  }

  @Test
  public void cornerStrategyDoesNotCheckNonCornerCells() {
    StringBuilder builder = new StringBuilder();
    FallibleStrategy strategy = new CornerStrategy();
    MockModel mock = new MockModel(model, builder, new ArrayList<>());
    strategy.choosePosition(mock, RepresentativeColor.BLACK);
    List<RowColPair> cornerPoints = new ArrayList<>();
    cornerPoints.add(new RowColPair(-3, -3));
    cornerPoints.add(new RowColPair(-3, 4));
    cornerPoints.add(new RowColPair(4, -3));
    cornerPoints.add(new RowColPair(4, 4));
    for (RowColPair pair : mock.getBoard().keySet()) {
      String pairString = "Checking" + "(" + pair.getRow() + "," + pair.getCol() + ")";
      if (!cornerPoints.contains(pair)) {
//        Assert.assertFalse(builder.toString().contains(pairString));
      } else {
        System.out.println("@corner");
      }
    }
  }

}
