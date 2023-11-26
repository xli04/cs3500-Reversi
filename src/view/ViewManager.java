package view;

import java.util.ArrayList;
import java.util.List;

import controller.Manager;
import model.ReadOnlyReversiModel;

/**
 * A ViewManager raises events to all the views that it manages. Views can subsrcibe
 * to be managed by this manager. Whenever the game state is updated, the view manager is
 * called-back, and it notifies all its view subscribers to update their display.
 */
public final class ViewManager implements Manager<GUIView> {
  private final List<GUIView> reversiViews;

  /**
   * Construct the current ViewManager.
   */
  public ViewManager() {
    reversiViews = new ArrayList<>();
  }

  /**
   * Register for current manager.
   *
   * @param view the view that wants to subscribe to the current manager.
   */
  @Override
  public void register(GUIView view) {
    reversiViews.add(view);
  }

  /**
   * update the current game state to all the view that shared the same model.
   *
   * @param model updated model
   */
  @Override
  public void update(ReadOnlyReversiModel model) {
    for (GUIView reversiView : reversiViews) {
      reversiView.resetPanel(model);
    }
  }

}
