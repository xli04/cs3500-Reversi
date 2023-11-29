import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import controller.Controller;
import controller.ControllerListeners;
import model.Hexagon;
import model.ModelStatus;
import model.MutableReversiModel;
import model.RegularReversiModel;
import model.RepresentativeColor;
import model.ReversiAiPlayer;
import model.ReversiHumanPlayer;
import model.ReversiModelStatus;
import model.RowColPair;
import strategy.CaptureMaxPieces;
import strategy.CompleteStrategy;
import strategy.MinimaxStrategy;
import view.GraphicView;
import view.MockView;

/**
 * A test class for the if the controller interact with the view and model as we expected.
 */
public class TestInteractionsBetweenControllerAndViewAndModel {
  Controller controller1;
  GraphicView mockView1;
  MutableReversiModel model;
  StringBuilder builder1;
  Controller controller2;
  GraphicView mockView2;
  StringBuilder builder2;
  ModelStatus status;


  /**
   * set up the fields.
   */
  @Before
  public void setUp() {
    status = new ReversiModelStatus();
    model = new RegularReversiModel.Builder(status).build();
    builder1 = new StringBuilder();
    mockView1 = new MockView(builder1);
    controller1 = new Controller(model, mockView1, new ReversiHumanPlayer(), status);
    builder2 = new StringBuilder();
    mockView2 = new MockView(builder2);
    controller2 = new Controller(model, mockView2, new ReversiHumanPlayer(), status);
    ControllerListeners listeners = new ControllerListeners();
    listeners.register(controller1);
    listeners.register(controller2);
    model.addListener(listeners);
    model.startGame();
  }

  @Test
  public void testForToggleTurn() {
    Assert.assertTrue(builder1.toString().contains("You have toggle the turn to BLACK"));
    Assert.assertTrue(builder1.toString().contains("Set Warning: false"));
    Assert.assertTrue(builder2.toString().contains("You have toggle the turn to BLACK"));
    Assert.assertTrue(builder2.toString().contains("Set Warning: false"));
    // at the beginning, the model will set the turn to black after start game and there is no
    // has to pass waring to both players.
    controller1.placeMove(new RowColPair(-1, 2));
    Assert.assertTrue(builder1.toString().contains("Update the view"));
    Assert.assertTrue(builder2.toString().contains("Update the view"));
    // After one of the player place the move, the controller will update all the view that
    // Care about the model so that both players can check move on the same board. The model
    // will switch the turn after one of the player make the move.
    Assert.assertTrue(builder1.toString().contains("You have toggle the turn to WHITE"));
    Assert.assertTrue(builder2.toString().contains("You have toggle the turn to WHITE"));
    // model will switch the turn after one of the player placed a move.
  }

  @Test
  public void testHasToPassWarning() {
    model = new RegularReversiModel.Builder(status).setSize(2).build();
    ControllerListeners listeners = new ControllerListeners();
    listeners.register(controller1);
    listeners.register(controller2);
    model.addListener(listeners);
    model.startGame();
    Assert.assertTrue(builder1.toString().contains("Set Warning: true"));
    // If there is no valid move in the current model, set the warning.
    model.makePass(RepresentativeColor.BLACK);
    Assert.assertTrue(builder2.toString().contains("Set Warning: true"));
  }

  @Test
  public void testGameIsOver() {
    controller1.makePass();
    controller2.makePass();
    Assert.assertTrue(builder1.toString().contains("Show game over"));
    Assert.assertTrue(builder2.toString().contains("Show game over"));
    // If the game is over, notify the users.
  }

  @Test
  public void testAiPlayerLockTheMouse() {
    model = new RegularReversiModel.Builder(status).build();
    builder1 = new StringBuilder();
    mockView1 = new MockView(builder1);
    controller1 = new Controller(model, mockView1, new ReversiAiPlayer(new
            CompleteStrategy(new MinimaxStrategy())), status);
    builder2 = new StringBuilder();
    mockView2 = new MockView(builder2);
    controller2 = new Controller(model, mockView2, new ReversiHumanPlayer(), status);
    ControllerListeners listeners = new ControllerListeners();
    listeners.register(controller1);
    listeners.register(controller2);
    model.addListener(listeners);
    model.startGame();
    Assert.assertTrue(builder1.toString().contains("Lock the mouse"));
    // only lock the mouse for the Non human player.
    Assert.assertFalse(builder2.toString().contains("Lock the mouse"));
  }

  @Test
  public void testReSetSelectedPositionAfterMove() {
    controller1.placeMove(new RowColPair(-1, 2));
    Assert.assertTrue(builder1.toString().contains("reset selectedPosition"));
  }

  @Test
  public void testNotifyTheUserInvalidMove() {
    controller1.placeMove(new RowColPair(0, 0));
    Assert.assertTrue(builder1.toString().contains("Can not place here (0,0)  Player: BLACK"));
    // notify the user if the given move is invalid.
  }

  @Test
  public void testNotifyTheUserCanOnlyPass() {
    status = new ReversiModelStatus();
    model = new RegularReversiModel.Builder(status).setSize(2).build();
    builder1 = new StringBuilder();
    mockView1 = new MockView(builder1);
    controller1 = new Controller(model, mockView1, new ReversiHumanPlayer(), status);
    builder2 = new StringBuilder();
    mockView2 = new MockView(builder2);
    controller2 = new Controller(model, mockView2, new ReversiHumanPlayer(), status);
    ControllerListeners listeners = new ControllerListeners();
    listeners.register(controller1);
    listeners.register(controller2);
    model.addListener(listeners);
    model.startGame();
    controller1.placeMove(new RowColPair(0, 0));
    Assert.assertTrue(builder1.toString()
            .contains("No valid move, can only choose to pass Player: BLACK"));
    // If there is no valid move in the board and the user still wants to make a move, notify
    // the user.
  }

  @Test
  public void testAddFeaturesAndSetColor() {
    Assert.assertTrue(builder1.toString().contains("add Features"));
    Assert.assertTrue(builder2.toString().contains("add Features"));
    // the controller will add it self as the features to the view in the constructor.
    Assert.assertTrue(builder1.toString().contains("set Color to BLACK"));
    Assert.assertTrue(builder2.toString().contains("set Color to WHITE"));
  }

  @Test
  public void testControllerModifyTheModelCorrectlyPlaceMove() {
    Assert.assertEquals(RepresentativeColor.NONE, model.getColorAt(new RowColPair(-1, 2)));
    controller1.placeMove(new RowColPair(-1, 2));
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(-1, 2)));
    Assert.assertEquals(RepresentativeColor.NONE, model.getColorAt(new RowColPair(1, -2)));
    controller2.placeMove(new RowColPair(1, -2));
    Assert.assertEquals(RepresentativeColor.WHITE, model.getColorAt(new RowColPair(1, -2)));
  }

  @Test
  public void testControllerModifyTheModelCorrectlyWhenNotRightTurn() {
    Assert.assertEquals(RepresentativeColor.NONE, model.getColorAt(new RowColPair(1, -2)));
    Assert.assertEquals(RepresentativeColor.BLACK, model.getTurn());
    controller2.placeMove(new RowColPair(1, -2));
    Assert.assertEquals(RepresentativeColor.NONE, model.getColorAt(new RowColPair(1, -2)));
    controller2.makePass();
    Assert.assertEquals(RepresentativeColor.BLACK, model.getTurn());
    // even though place at (1,-2) is a valid move, but the player in controller2 is placing
    // for the white cell, but the current turn Black, so the plater in controller2 can not make
    // an action.
  }

  @Test
  public void testControllerModifyTheModelCorrectlyMakePass() {
    Assert.assertEquals(RepresentativeColor.BLACK, model.getTurn());
    controller1.makePass();
    Assert.assertEquals(RepresentativeColor.WHITE, model.getTurn());
    controller2.makePass();
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testStartGameAssignColorCorrectly() {
    MutableReversiModel model = new RegularReversiModel.Builder(status).build();
    GraphicView view = new MockView(new StringBuilder());
    GraphicView view2 = new MockView(new StringBuilder());
    Controller controller = new Controller(model, view, new ReversiHumanPlayer(), status);
    Controller controller2 = new Controller(model, view2, new ReversiHumanPlayer(), status);
    ControllerListeners listeners = new ControllerListeners();
    listeners.register(controller);
    listeners.register(controller2);
    model.addListener(listeners);
    Assert.assertNull(controller.checkPlayer().getColor());
    Assert.assertNull(controller2.checkPlayer().getColor());
    model.startGame();
    Assert.assertEquals(RepresentativeColor.BLACK, controller.checkPlayer().getColor());
    Assert.assertEquals(RepresentativeColor.WHITE, controller2.checkPlayer().getColor());
  }

  @Test
  public void testThrowExceptionWhenMoreThanTwoPlayersWasAdded() {
    status = new ReversiModelStatus();
    model = new RegularReversiModel.Builder(status).build();
    builder1 = new StringBuilder();
    mockView1 = new MockView(builder1);
    controller1 = new Controller(model, mockView1, new ReversiHumanPlayer(), status);
    builder2 = new StringBuilder();
    mockView2 = new MockView(builder2);
    controller2 = new Controller(model, mockView2, new ReversiHumanPlayer(), status);
    StringBuilder builder3 = new StringBuilder();
    GraphicView mockView3 = new MockView(builder3);
    Controller controller3 = new Controller(model, mockView3, new ReversiHumanPlayer(), status);
    ControllerListeners listeners = new ControllerListeners();
    listeners.register(controller1);
    listeners.register(controller2);
    listeners.register(controller3);
    model.addListener(listeners);
    Assert.assertThrows(IllegalStateException.class, () -> model.startGame());
    // since the current game is only for two players, when the controller manager detect the
    // third player wants to join the game, throw the exception
  }

  @Test
  public void testTryToPlaceWorksProperlyWhenPlacingValidMove() {
    status = new ReversiModelStatus();
    model = new RegularReversiModel.Builder(status).build();
    builder1 = new StringBuilder();
    mockView1 = new MockView(builder1);
    controller2 = new Controller(model, mockView1, new ReversiAiPlayer(new
            CompleteStrategy(new CaptureMaxPieces())), status);
    builder2 = new StringBuilder();
    mockView2 = new MockView(builder2);
    controller1 = new Controller(model, mockView2, new ReversiHumanPlayer(), status);
    ControllerListeners listeners = new ControllerListeners();
    listeners.register(controller1);
    listeners.register(controller2);
    model.addListener(listeners);
    model.startGame();
    // if the current player is a human player, nothing happened.
    Map<RowColPair, Hexagon> original = model.getBoard();
    Map<RowColPair, Hexagon> after = model.getBoard();
    controller1.tryToPlace();
    for (RowColPair pair : original.keySet()) {
      Assert.assertEquals(original.get(pair).getColor(), after.get(pair).getColor());
    }
    controller1.makePass();
    // if the player is an ai player, the controller will ask the player to choose the next move
    // and then execute it.
    Assert.assertEquals(RepresentativeColor.WHITE, model.getColorAt(new RowColPair(-2, 1)));
  }

  @Test
  public void testTryToPlaceWorksProperlyWhenNoValidMoveExist() {
    status = new ReversiModelStatus();
    model = new RegularReversiModel.Builder(status).setSize(2).build();
    builder1 = new StringBuilder();
    mockView1 = new MockView(builder1);
    controller2 = new Controller(model, mockView1, new ReversiAiPlayer(new
            CompleteStrategy(new CaptureMaxPieces())), status);
    builder2 = new StringBuilder();
    mockView2 = new MockView(builder2);
    controller1 = new Controller(model, mockView2, new ReversiHumanPlayer(), status);
    ControllerListeners listeners = new ControllerListeners();
    listeners.register(controller1);
    listeners.register(controller2);
    model.addListener(listeners);
    model.startGame();
    // if the current player is a human player, nothing happened.
    Map<RowColPair, Hexagon> original = model.getBoard();
    Map<RowColPair, Hexagon> after = model.getBoard();
    controller1.tryToPlace();
    for (RowColPair pair : original.keySet()) {
      Assert.assertEquals(original.get(pair).getColor(), after.get(pair).getColor());
    }
    controller1.makePass();
    // if there is no valid move, the ai player will choose to make pass.
    Assert.assertTrue(model.isGameOver());
  }
}
