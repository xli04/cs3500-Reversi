import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import controller.Controller;
import controller.ControllerManager;
import controller.Manager;
import model.MutableReversiModel;
import model.RegularReversiModel;
import model.RepresentativeColor;
import model.ReversiPlayer;
import model.RowColPair;
import strategy.CompleteStrategy;
import strategy.MinimaxStrategy;
import view.MockView;
import view.ReversiView;
import view.ViewManager;

/**
 * A test class for the if the controller interact with the view and model as we expected.
 */
public class TestInteractionsBetweenControllerAndViewAndModel {
  Controller controller1;
  ReversiView mockView1;
  MutableReversiModel model;
  StringBuilder builder1;
  Controller controller2;
  ReversiView mockView2;
  StringBuilder builder2;
  Manager<Controller> manager;
  Manager<ReversiView> viewManager;

  /**
   * set up the fields.
   */
  @Before
  public void setUp() {
    manager = new ControllerManager();
    viewManager = new ViewManager();
    model = new RegularReversiModel(Arrays.asList(manager, viewManager));
    builder1 = new StringBuilder();
    mockView1 = new MockView(builder1, viewManager);
    controller1 = new Controller(model, mockView1, new ReversiPlayer(null), manager);
    builder2 = new StringBuilder();
    mockView2 = new MockView(builder2, viewManager);
    controller2 = new Controller(model, mockView2, new ReversiPlayer(null), manager);
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
    model = new RegularReversiModel(2, Arrays.asList(manager, viewManager));
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
    manager = new ControllerManager();
    viewManager = new ViewManager();
    model = new RegularReversiModel(Arrays.asList(manager, viewManager));
    builder1 = new StringBuilder();
    mockView1 = new MockView(builder1, viewManager);
    controller1 = new Controller(model, mockView1, new ReversiPlayer(new
      CompleteStrategy(new MinimaxStrategy())), manager);
    builder2 = new StringBuilder();
    mockView2 = new MockView(builder2, viewManager);
    controller2 = new Controller(model, mockView2, new ReversiPlayer(null), manager);
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
    Assert.assertTrue(builder1.toString().contains("Can not place here BLACK"));
    // notify the user if the given move is invalid.
  }

  @Test
  public void testNotifyTheUserCanOnlyPass() {
    manager = new ControllerManager();
    viewManager = new ViewManager();
    model = new RegularReversiModel(2, Arrays.asList(manager, viewManager));
    builder1 = new StringBuilder();
    mockView1 = new MockView(builder1, viewManager);
    controller1 = new Controller(model, mockView1, new ReversiPlayer(null), manager);
    builder2 = new StringBuilder();
    mockView2 = new MockView(builder2, viewManager);
    controller2 = new Controller(model, mockView2, new ReversiPlayer(null), manager);
    model.startGame();
    controller1.placeMove(new RowColPair(0, 0));
    Assert.assertTrue(builder1.toString().contains("No valid move, can only choose to pass BLACK"));
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
    ViewManager manager = new ViewManager();
    ControllerManager cm = new ControllerManager();
    MutableReversiModel model = new RegularReversiModel(Arrays.asList(cm, manager));
    ReversiView view = new MockView(new StringBuilder(), manager);
    ReversiView view2 = new MockView(new StringBuilder(), manager);
    Controller controller = new Controller(model, view, new ReversiPlayer(null), cm);
    Controller controller2 = new Controller(model, view2, new ReversiPlayer(null), cm);
    Assert.assertNull(controller.checkPlayer().getColor());
    Assert.assertNull(controller2.checkPlayer().getColor());
    model.startGame();
    Assert.assertEquals(RepresentativeColor.BLACK, controller.checkPlayer().getColor());
    Assert.assertEquals(RepresentativeColor.WHITE, controller2.checkPlayer().getColor());
  }

  @Test
  public void testThrowExceptionWhenMoreThanTwoPlayersWasAdded() {
    manager = new ControllerManager();
    viewManager = new ViewManager();
    model = new RegularReversiModel(Arrays.asList(manager, viewManager));
    builder1 = new StringBuilder();
    mockView1 = new MockView(builder1, viewManager);
    controller1 = new Controller(model, mockView1, new ReversiPlayer(null), manager);
    builder2 = new StringBuilder();
    mockView2 = new MockView(builder2, viewManager);
    controller2 = new Controller(model, mockView2, new ReversiPlayer(null), manager);
    StringBuilder builder3 = new StringBuilder();
    ReversiView mockView3 = new MockView(builder3, viewManager);
    Controller controller3 = new Controller(model, mockView3, new ReversiPlayer(null), manager);
    Assert.assertThrows(IllegalStateException.class, () -> model.startGame());
    // since the current game is only for two players, when the controller manager detect the
    // third player wants to join the game, throw the exception
  }
}
