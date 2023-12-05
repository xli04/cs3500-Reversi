package view;

import model.ReadOnlyReversiModel;
import model.RepresentativeColor;

/**
 * a Mock view, represents a mock of the regular view, used to test if the strategy did the
 * things we expected. it will give feedback to determine if the controller interact with
 * the view correctly.
 */
public class MockView implements IView {
  private final StringBuilder builder;

  /**
   * Initialize the mock view, string builder to record the action.
   *
   * @param builder the string builder that to record the actions
   */
  public MockView(StringBuilder builder) {
    this.builder = builder;
  }

  @Override
  public void display() {
    builder.append("display").append("\n");
  }

  /**
   * Append a message to the string builder to show that the view will reset selectedPosition.
   */
  private void resetSelectedPosition() {
    builder.append("reset selectedPosition").append("\n");
  }

  /**
   * Append a message to the string builder to show that the view will resetPanel.
   */
  private void resetPanel(ReadOnlyReversiModel model) {
    builder.append("Update the view").append("\n");
  }

  @Override
  public void addFeatures(Features features) {
    builder.append("add Features").append("\n");
  }

  @Override
  public void showMessage(String s) {
    builder.append(s).append("\n");
  }

  @Override
  public void lockInteractionWithViewForNonHumanPlayer() {
    builder.append("Lock the mouse").append("\n");
  }

  @Override
  public void showHints(RepresentativeColor color) {
    builder.append("show hints").append("\n");
  }

  /**
   * Append a message to the string builder to show that the view will setGameOverState.
   */
  private void setGameOverState(RepresentativeColor winner, boolean winOrNot) {
    builder.append("Show game over winner: ").append(winner).append("\n");
  }

  /**
   * Append a message to the string builder to show that the view will setHasToPassWarning.
   */
  private void setHasToPassWarning(boolean hasToPass, boolean isYourTurn) {
    builder.append("Set Warning: ").append(hasToPass).append("\n");
  }

  @Override
  public void setColor(RepresentativeColor color) {
    builder.append("set Color to ").append(color).append("\n");
  }

  @Override
  public void update(ReadOnlyReversiModel model, RepresentativeColor player) {
    resetPanel(model);
    resetSelectedPosition();
    if (model.isGameOver()) {
      RepresentativeColor winner = model.getWinner();
      boolean win = winner == player;
      setGameOverState(model.getWinner(), win);
      showMessage("Game is over Winner is " + winner);
      return;
    }
    resetSelectedPosition();
    setColor(player);
    boolean yourTurn = model.getTurn() == player;
    toggleTurn(model.getTurn(), yourTurn);
    setHasToPassWarning(model.hasToPass(), yourTurn);
    builder.append("Updated").append("\n");
  }

  /**
   * Append a message to the string builder to show that the view will toggleTurn.
   */
  private void toggleTurn(RepresentativeColor color, boolean isYourTurn) {
    builder.append("You have toggle the turn to ").append(color).append("\n");
  }
}
