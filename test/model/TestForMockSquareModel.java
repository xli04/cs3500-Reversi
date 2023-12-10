package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import model.CellPiece;
import model.MockModel;
import model.MutableReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import model.SquareReversiModel;
import strategy.CaptureMaxPieces;
import strategy.CornerStrategy;
import strategy.FallibleStrategy;

/**
 * A test class for mock model in order to test if the strategy do the things we expected.
 */
public class TestForMockSquareModel {
  MutableReversiModel model;

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
      Assert.assertTrue(builder.toString().contains(pairString));
    }
  }
}
