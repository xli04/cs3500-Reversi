package adapter;

import reversi.provider.model.GamePiece;
import reversi.provider.model.PlayerTurn;
import reversi.provider.view.BasicReversiView;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import reversi.provider.view.ViewFeatures;
import view.Features;
import view.IView;

/**
 * ProviderGraphicView represents an adapter pattern that using the provider's BasicReversiView
 * logic with our view interface so that provider's view can be used in our controller.
 */
public class AdapterGraphicView implements IView {
  private final ViewFeatures features;
  private final BasicReversiView view;

  /**
   * Constructs a ReversiView with the given ReadOnlyReversiModel.
   *
   * @param features the features that will be added in this view.
   */
  public AdapterGraphicView(BasicReversiView view, ViewFeatures features) {
    this.view = view;
    this.features = features;
  }

  @Override
  public void display() {
    view.display(true);
  }

  @Override
  public void addFeatures(Features features) {
    // the provider features will be added in to provider's view in the main.
    view.addFeatureListener(this.features);
  }

  @Override
  public void showMessage(String s) {
    // the logic of showing message is in provider's view.
  }

  @Override
  public void lockInteractionWithViewForNonHumanPlayer() {
    // the logic of lock mouse is in the provider's mouse listener.
  }

  @Override
  public void showHints(RepresentativeColor color) {
    // provider's code does not have this functionality
  }

  @Override
  public void setColor(RepresentativeColor color) {
    // the provider's panel will take in a model thus it will know the color
  }

  @Override
  public void update(ReadOnlyReversiModel model, RepresentativeColor player) {
    view.update();
    if (model.isGameOver()) {
      GamePiece piece = GamePiece.BLACK;
      if (model.getWinner() == RepresentativeColor.WHITE) {
        piece = GamePiece.WHITE;
      } else if (model.getWinner() == null) {
        piece = GamePiece.EMPTY;
      }
      PlayerTurn turn = piece == GamePiece.BLACK ? PlayerTurn.BLACK : PlayerTurn.WHITE;
      view.showGameOverMessage(turn, piece);
    }
    if (!model.isGameOver() && model.getTurn() == player) {
      view.notifyPlayer();
    }
  }
}
