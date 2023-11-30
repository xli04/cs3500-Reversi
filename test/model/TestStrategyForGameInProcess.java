package model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import model.Hexagon;
import model.MockModel;
import model.MutableReversiModel;
import model.ReadOnlyReversiModel;
import model.RegularReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import strategy.AvoidCellsNextToCornersStrategy;
import strategy.CaptureMaxPieces;
import strategy.CompleteStrategy;
import strategy.CompositeStrategy;
import strategy.CornerStrategy;
import strategy.InfallibleStrategy;
import strategy.MinimaxStrategy;
import strategy.FallibleStrategy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for the strategies.
 */
public class TestStrategyForGameInProcess {
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
   *      _ _ _ _ _ _
   *     _ _ _ _ _ _ _
   *    _ _ _ _ _ _ _ _
   *   _ _ _ _ X _ _ _ _
   *  _ _ _ _ O X X _ _ _
   * _ _ _ _ O _ X _ _ _ _
   *  _ _ _ _ O X _ _ _ _
   *   _ _ _ _ O _ _ _ _
   *    _ _ _ O _ _ _ _
   *     _ _ O _ _ _ _
   *      _ _ _ _ _ _
   * The initial board is above, there are two points that can get the same score,
   * (-1,2) and (5,-4), since (5,-4) is close to the corner, we should choose the
   * (-1,2) in order to block another player.
   */
  @Test
  public void testAvoidCorner() {
    Map<RowColPair, Hexagon> board = makeBoard(6);
    board.put(new RowColPair(0, -1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-1, 0), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(1, -1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(2, -1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(3, -2), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(4, -3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-2, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-1, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-1, 2), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(0, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(1, 0), new Hexagon(RepresentativeColor.BLACK));
    InfallibleStrategy avoidCorner = new CompleteStrategy(new AvoidCellsNextToCornersStrategy());
    MutableReversiModel model = new RegularReversiModel(board, 6, RepresentativeColor.BLACK);
    RowColPair pair = avoidCorner.choosePosition(model, RepresentativeColor.BLACK);
    Assert.assertEquals(new RowColPair(1, -2), pair);
  }

  /**
   *      _ _ _ _ _ _
   *     _ _ _ _ X _ _
   *    _ _ _ _ O _ _ _
   *   _ _ _ _ O _ _ _ _
   *  _ _ _ _ O X _ _ _ _
   * _ _ _ _ O _ _ _ _ _ _
   *  _ _ _ A X _ _ _ _ _
   *   _ _ _ O _ _ _ _ _
   *    _ _ O _ _ _ _ _
   *     _ O O _ _ _ _
   *      B _ _ _ _ _
   * The board is looks like above, even though place in the A position can flip more cells,
   * B is in the corner, thus it can generate more benefit since the corner cell can not
   * be changed.
   */
  @Test
  public void testChooseCorner() {
    Map<RowColPair, Hexagon> board = makeBoard(6);
    board.put(new RowColPair(-1, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(4, -3), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(1, -1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-2, 1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-1, 0), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(0, -1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(4, -3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(2, -2), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(3, -3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(4, -4), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-3, 2), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-4, 3), new Hexagon(RepresentativeColor.BLACK));
    InfallibleStrategy cornerFirst = new CompleteStrategy(new CornerStrategy());
    MutableReversiModel model = new RegularReversiModel(board, 6, RepresentativeColor.BLACK);
    RowColPair nextPosition = cornerFirst.choosePosition(model, RepresentativeColor.BLACK);
    Assert.assertEquals(new RowColPair(5, -5), nextPosition);
  }

  /**
   * in the beginning state of the game, even though there are many position to place the cell
   * and with the value one, the cell (-2, 1) is the most upper one.
   */
  @Test
  public void testForStrategyReturnCorrectPositionSameValueUpperLeftMost() {
    InfallibleStrategy getHigherScore = new CompleteStrategy(new CaptureMaxPieces());
    MutableReversiModel model = new RegularReversiModel.ModelBuilder().build();
    Assert.assertEquals(new RowColPair(-2, 1),
        getHigherScore.choosePosition(model, RepresentativeColor.BLACK));
  }

  /**
   *      _ _ _ _ _ _
   *     _ _ _ _ X _ _
   *    _ _ _ _ O _ _ _
   *   _ _ _ _ O _ _ _ _
   *  _ _ _ _ O X _ _ _ _
   * _ _ _ _ O _ _ _ _ _ _
   *  _ _ _ A X _ _ _ _ _
   *   _ _ _ O _ _ _ _ _
   *    _ _ O _ _ _ _ _
   *     _ O O _ _ _ _
   *      B _ _ _ _ _
   * The board is looks like above, in the A position can flip more cells.
   */
  @Test
  public void testForStrategyReturnCorrectPositionHighestValue() {
    Map<RowColPair, Hexagon> board = makeBoard(6);
    board.put(new RowColPair(-1, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(4, -3), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(1, -1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-2, 1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-1, 0), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(0, -1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(4, -3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(2, -2), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(3, -3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(4, -4), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-3, 2), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-4, 3), new Hexagon(RepresentativeColor.BLACK));
    MutableReversiModel model = new RegularReversiModel(board, 6, RepresentativeColor.BLACK);
    InfallibleStrategy strategy = new CompleteStrategy(new CaptureMaxPieces());
    RowColPair nextPosition = strategy.choosePosition(model, RepresentativeColor.BLACK);
    Assert.assertEquals(new RowColPair(1, -2), nextPosition);
  }

  /**
   *      _ _ _ _ _ _
   *     _ _ _ _ _ _ _
   *    _ _ _ _ _ _ _ _
   *   _ _ _ _ X _ _ _ _
   *  _ _ _ _ O X X _ _ _
   * _ _ _ _ O _ X _ _ _ _
   *  _ _ _ _ O X _ _ _ _
   *   _ _ _ _ O _ _ _ _
   *    _ _ _ O _ _ _ _
   *     _ _ O _ _ _ _
   *      _ _ _ _ _ _
   * The initial board is above, can not place the cell in the corner
   * thus use the back up plan avoid corner strategy.
   */
  @Test
  public void testBackUp() {
    Map<RowColPair, Hexagon> board = makeBoard(6);
    board.put(new RowColPair(0, -1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-1, 0), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(1, -1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(2, -1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(3, -2), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(4, -3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-2, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-1, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-1, 2), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(0, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(1, 0), new Hexagon(RepresentativeColor.BLACK));
    InfallibleStrategy avoidCorner = new CompleteStrategy(new AvoidCellsNextToCornersStrategy());
    FallibleStrategy avoidCornerCombineCorner = new CompositeStrategy(new CornerStrategy(),
        new AvoidCellsNextToCornersStrategy());
    InfallibleStrategy complete = new CompleteStrategy(avoidCornerCombineCorner);
    MutableReversiModel model = new RegularReversiModel(board, 6, RepresentativeColor.BLACK);
    RowColPair avoidCornerPair = avoidCorner.choosePosition(model, RepresentativeColor.BLACK);
    RowColPair cornerPair = complete.choosePosition(model, RepresentativeColor.BLACK);
    Assert.assertEquals(avoidCornerPair, cornerPair);
  }

  /**
   *      O O O O X A
   *     _ _ X _ O _ _
   *    B X X X X O X X
   *   _ _ _ _ X _ O _ _
   *  _ _ _ X X X _ _ _ _
   * _ _ _ _ X _ X _ _ _ _
   *  _ _ _ C X O _ _ _ _
   *   _ _ _ _ _ _ _ _ _
   *    _ _ _ _ _ _ _ _
   *     _ _ _ _ _ _ _
   *      _ _ _ _ _ _
   * The board is shown above, placing at position A can build a line in that row which can not
   * be modified anymore, even if we do not place at A in this turn, it's still unlikely our
   * opposite can occupied the that row. even though placing at C can flip 5 cells, playing
   * at the position B can only flip 4 cells, placing at B can Block the action of the opposite
   * color on the top three rows which can produce more potential benefit thus B is the best
   * choice in this turn
   */
  @Test
  public void testMinimaxStrategyMakeTheBestChoice() {
    Map<RowColPair, Hexagon> board = makeBoard(6);
    board.put(new RowColPair(-5, 0), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-5, 1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-5, 2), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-5, 3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-5, 4), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-4, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-4, 3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-3, -1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-3, 0), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-3, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-3, 2), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-3, 3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-3, 4), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-3, 5), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-2, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-2, 3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-1, -1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-1, 0), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-1, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(0, -1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(0, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(1, -1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(1, 0), new Hexagon(RepresentativeColor.WHITE));
    MutableReversiModel model = new RegularReversiModel(board, 6, RepresentativeColor.BLACK);
    InfallibleStrategy player = new CompleteStrategy(new MinimaxStrategy());
    RowColPair pair = player.choosePosition(model, RepresentativeColor.WHITE);
    Assert.assertEquals(new RowColPair(-5, 5), pair);
  }

  /**
   *      O O O O O O
   *     C _ O _ O _ O
   *    _ X O X O O O O
   *   _ _ O _ O _ O _ O
   *  _ _ A X O X X X _ _
   * _ _ _ _ O _ X _ D _ _
   *  _ _ X X X X X _ _ _
   *   _ _ _ _ _ _ B _ _
   *    _ _ _ _ _ _ _ _
   *     _ _ _ _ _ _ _
   *      _ _ _ _ _ _
   * The initial board is shown above, the minimax strategy will choose position for white cell,
   * For position B, it can flip the 3 cells, which is the option that can flip the most cells,
   * For position C, it can flip 1 cell, but it is also close to the corner which means it can not
   * be flipped any more and we can use it as the base to do further actions. For position, it also
   * can flip 1 cell, but it is base on the row(-5), which is all white cells, it means the cell
   * we placed in this line can nor the flipped by the opposite and if the opposite color want to
   * flip the cell in the position A, we can always flip it back since the other four cells that in
   * the same line with A is not likely not be flipped. Under this circumstance, place in the
   * position A is the best choice since it can Extend a stable line and prevent opponents from
   * flipping our cells.
   *
   */
  @Test
  public void testMinimaxShowItsIntelligence() {
    Map<RowColPair, Hexagon> board = makeBoard(6);
    board.put(new RowColPair(-5, 0), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-5, 1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-5, 2), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-5, 3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-5, 4), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-5, 5), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-4, 1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-4, 3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-4, 5), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-3, -1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-3, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-3, 0), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-3, 2), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-3, 3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-3, 4), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-3, 5), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-2, -1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-2, 1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-2, 3), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-2, 5), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-1, -1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-1, 0), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-1, -1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-1, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-1, 2), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(-1, 3), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(0, -1), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(0, 1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(1, -3), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(1, -2), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(1, -1), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(1, 0), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(1, 1), new Hexagon(RepresentativeColor.BLACK));
    InfallibleStrategy strategy = new CompleteStrategy(new MinimaxStrategy());
    ReadOnlyReversiModel model = new RegularReversiModel(board, 6, RepresentativeColor.BLACK);
    RowColPair pair = strategy.choosePosition(model, RepresentativeColor.WHITE);
    Assert.assertEquals(new RowColPair(-1, -2), pair);
  }


  /**
   *      _ _ _ _ _ _
   *     _ A _ _ _ _ _
   *    _ _ O _ _ _ _ _
   *   _ _ _ O _ _ _ _ _
   *  _ _ _ _ X _ _ _ _ _
   * _ _ _ _ _ O _ _ _ _ _
   *   _ _ _ _ _ B _ _ _
   *   _ _ _ _ _ _ _ _ _
   *    _ _ _ _ _ _ _ _
   *    _ _ _ _ _ _ _
   *      _ _ _ _ _ _
   * There are two valid move in this board, but since we are using the avoidCellNextToCorner
   * strategy, the only valid move is B, but since we lie to the strategy to told it we can
   * not place on the B position, the strategy can not find a valid move.
   */
  @Test
  public void testCheatInAvoidCellNextToCornerStrategy() {
    Map<RowColPair, Hexagon> board = makeBoard(6);
    board.put(new RowColPair(-3, 0), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-2, 0), new Hexagon(RepresentativeColor.WHITE));
    board.put(new RowColPair(-1, 0), new Hexagon(RepresentativeColor.BLACK));
    board.put(new RowColPair(0, 0), new Hexagon(RepresentativeColor.WHITE));
    MutableReversiModel model = new RegularReversiModel(board, 6, RepresentativeColor.BLACK);
    FallibleStrategy strategy = new AvoidCellsNextToCornersStrategy();
    StringBuilder builder = new StringBuilder();
    MockModel mock = new MockModel(model, builder, List.of(new RowColPair(1, 0)));
    Optional<RowColPair> pair = strategy.choosePosition(mock, RepresentativeColor.BLACK);
    assertTrue(builder.toString().contains("lie on you"));
    assertFalse(pair.isPresent());
  }

  /**
   * Make a 3 x 3 board. Check that the corner player tries to play moves at
   * exactly all 6 corners, and then then it does not find a move
   */
  @Test
  public void testCornerPlayerChecksAllCornerMoves() {
    Map<RowColPair, Hexagon> board = makeBoard(3);
    List<RowColPair> cornerPoints = Arrays.asList(new RowColPair(-2, 0),
        new RowColPair(-2, 2),
        new RowColPair(2, -2),
        new RowColPair(2, 0),
        new RowColPair(0, 2),
        new RowColPair(0, -2));
    for (RowColPair point : cornerPoints) {
      board.replace(point, new Hexagon(RepresentativeColor.NONE));
    }
    MutableReversiModel delegate = new RegularReversiModel(board, 3, RepresentativeColor.BLACK);
    StringBuilder builder = new StringBuilder();
    MockModel mock = new MockModel(delegate, builder, new ArrayList<>());

    FallibleStrategy cornerStrategy = new CornerStrategy();
    Optional<RowColPair> move = cornerStrategy.choosePosition(mock, RepresentativeColor.BLACK);

    assertTrue(move.isEmpty()); //assert that the move is empty

    String output = builder.toString();

    //confirm that the strategy checked each corner move
    //confirm that the strategy did not check any other non-corner move
    for (RowColPair pair : delegate.getBoard().keySet()) {
      boolean containsRowColPair = output.contains(String.format(
          "Checking" + "(" + pair.getRow() + "," + pair.getCol() + ")"));
      if (cornerPoints.contains(pair)) {
        assertTrue(containsRowColPair);
      } else {
        assertFalse(containsRowColPair);
      }
    }
  }
}
