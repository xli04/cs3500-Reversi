package view;

import java.util.ArrayList;
import java.util.List;

import controller.Manager;
import model.ReadOnlyReversiModel;

/**
 * A ViewManager represents a observer pattern to all the views, once the
 * model is updated, notify all the view.
 */
public final class ViewManager implements Manager<ReversiView> {
  private final List<ReversiView> reversiViews;

  /**
   * Construct the current ViewManager.
   */
  public ViewManager() {
    reversiViews = new ArrayList<>();
  }

  /**
   * Register for current manager.
   *
   * @param element the view that wants to register
   */
  @Override
  public void register(ReversiView element) {
    reversiViews.add(element);
  }

  /**
   * update the current game state to all the view that shared the same model.
   *
   * @param model updated model
   */
  @Override
  public void update(ReadOnlyReversiModel model) {
    for (ReversiView reversiView : reversiViews) {
      reversiView.resetPanel(model);
    }
  }

}
