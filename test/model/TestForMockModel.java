package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import model.Hexagon;
import model.MockModel;
import model.MutableReversiModel;
import model.RegularReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import strategy.CaptureMaxPieces;
import strategy.CornerStrategy;
import strategy.FallibleStrategy;

/**
 * A test class for mock model in order to test if the strategy do the things we expected.
 */
public class TestForMockModel {
  MutableReversiModel model;

  @Before
  public void setUp() {
    model = new RegularReversiModel();
  }

  /**
   * This test checks if the corner strategy do check all the six corners in the board.
   */
  @Test
  public void testIfCornerStrategyCheckAllSixCorners() {
    StringBuilder builder = new StringBuilder();
    FallibleStrategy strategy = new CornerStrategy();
    MockModel mock = new MockModel(model, builder, new ArrayList<>());
    strategy.choosePosition(mock, RepresentativeColor.BLACK);
    List<RowColPair> cornerPoints = new ArrayList<>();
    cornerPoints.add(new RowColPair(-5, 0));
    cornerPoints.add(new RowColPair(-5, 5));
    cornerPoints.add(new RowColPair(5, -5));
    cornerPoints.add(new RowColPair(5, 0));
    cornerPoints.add(new RowColPair(0, 5));
    cornerPoints.add(new RowColPair(0, -5));
    for (RowColPair pair : cornerPoints) {
      String pairString = "Checking" + "(" + pair.getRow() + "," + pair.getCol() + ")";
      Assert.assertTrue(builder.toString().contains(pairString));
    }
  }

  /**
   *      _ _ _ _ _ _
   *     _ _ _ _ _ _ _
   *    _ _ _ _ _ _ _ _
   *   _ _ _ _ A _ _ _ _
   *  _ _ _ B X O _ _ _ _
   * _ _ _ _ O _ X _ _ _ _
   *  _ _ _ _ X O _ _ _ _
   *   _ _ _ _ _ _ _ _ _
   *    _ _ _ _ _ _ _ _
   *     _ _ _ _ _ _ _
   *      _ _ _ _ _ _
   * The initial board is above, point A should be the upper most left most position to choose since
   * all the move in board can flip one cell and A is the upper most one. but we lie to our strategy
   * so that A is can not flip anything thus the strategy will choose the second upper most left
   * most one, which is B.
   **/
  @Test
  public void checkCheatInTheCaptureMaxPiece() {
    StringBuilder builder = new StringBuilder();
    FallibleStrategy strategy = new CaptureMaxPieces();
    MockModel mock = new MockModel(model, builder, List.of(new RowColPair(-2, 1)));
    Optional<RowColPair> pair = strategy.choosePosition(mock, RepresentativeColor.BLACK);
    Assert.assertTrue(pair.isPresent());
    RowColPair actualPair = pair.get();
    Assert.assertTrue(builder.toString().contains("lie on you"));
    Assert.assertEquals(new RowColPair(-1, -1), actualPair);
  }


  @Test
  public void checkCaptureMaxPieceLoopThroughAllThePositionAndChooseAsExpectedWhenNotLie() {
    StringBuilder builder = new StringBuilder();
    FallibleStrategy strategy = new CaptureMaxPieces();
    MockModel mock = new MockModel(model, builder, new ArrayList<>());
    Optional<RowColPair> pair = strategy.choosePosition(mock, RepresentativeColor.BLACK);
    Assert.assertTrue(pair.isPresent());
    RowColPair actualPair = pair.get();
    Assert.assertEquals(new RowColPair(-2, 1), actualPair);
    Map<RowColPair, Hexagon> board = mock.getBoard();
    for (RowColPair position : board.keySet()) {
      String pairString = "Checking" + "(" + position.getRow() + "," + position.getCol() + ")";
      Assert.assertTrue(builder.toString().contains(pairString));
    }
  }
}
