package Adapter;

import Provider.src.cs3500.reversi.view.BasicReversiView;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import view.Features;
import view.IView;

public class ProviderGraphicView extends BasicReversiView implements IView {

  /**
   * Constructs a ReversiView with the given ReadOnlyReversiModel.
   *
   * @param model takes in a ReadOnlyReversiModel to allow the view to access information about the
   *              current state of the model.
   */
  public ProviderGraphicView(Provider.src.cs3500.reversi.model.ReadOnlyReversiModel model) {
    super(model);
  }

  @Override
  public void display() {
    super.display(true);
  }

  @Override
  public void addFeatures(Features features) {
  }


  @Override
  public void showMessage(String s) {
  }

  @Override
  public void lockInteractionWithViewForNonHumanPlayer() {
  }

  @Override
  public void showHints(RepresentativeColor color) {
    // provider's code does not have this functionality
  }

  @Override
  public void setColor(RepresentativeColor color) {

  }

  @Override
  public void update(ReadOnlyReversiModel model, RepresentativeColor player) {
    super.update();
    if (model.getTurn() == player) {
      super.notifyPlayer();
    }
  }
}
