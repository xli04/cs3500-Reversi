package view;

import controller.Manager;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * a Mock view, represents a mock of the regular view, used to test if the strategy did the
 * things we expected. it will give feedback if the controller interact with the view correctly.
 */
public class MockView implements GraphicView {
  private final StringBuilder builder;
  private final Manager<GraphicView> manager;

  /**
   * Initialize the mock view, string builder to record the action.
   *
   * @param builder the string builder that to record the actions
   * @param manager the manager of views
   */
  public MockView(StringBuilder builder, Manager<GraphicView> manager) {
    this.builder = builder;
    this.manager = manager;
    manager.register(this);
  }

  @Override
  public void display() {
    builder.append("display").append("\n");
  }

  @Override
  public RowColPair getSelectedPosition() {
    return null;
  }

  @Override
  public void resetSelectedPosition() {
    builder.append("reset selectedPosition").append("\n");
  }

  @Override
  public void resetPanel(ReadOnlyReversiModel model) {
    builder.append("Update the view").append("\n");
  }

  @Override
  public void addFeatures(Features features) {
    builder.append("add Features").append("\n");
  }

  @Override
  public void update(ReadOnlyReversiModel model) {
    manager.update(model);
  }

  @Override
  public void showStates(String s) {
    builder.append(s).append("\n");
  }

  @Override
  public void lockMouseForNonHumanPlayer() {
    builder.append("Lock the mouse").append("\n");
  }

  @Override
  public void showHints(RepresentativeColor color) {
    builder.append("show hints").append("\n");
  }

  @Override
  public void setGameOverState(RepresentativeColor winner, boolean winOrNot) {
    builder.append("Show game over").append("\n");
  }

  @Override
  public void setHasToPassWarning(boolean hasToPass, boolean isYourTurn) {
    builder.append("Set Warning: ").append(hasToPass).append("\n");
  }

  @Override
  public void setColor(RepresentativeColor color) {
    builder.append("set Color to ").append(color).append("\n");
  }

  @Override
  public void toggleTurn(RepresentativeColor color, boolean isYourTurn) {
    builder.append("You have toggle the turn to ").append(color).append("\n");
  }
}
