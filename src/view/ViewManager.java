package view;

import java.util.ArrayList;
import java.util.List;
import controller.Manager;
import model.ReadOnlyReversiModel;

/**
 * A ViewManager raises events to all the views that it manages. Views can subscribe
 * to be managed by this manager. Whenever the game state is updated, the view manager is
 * called-back, and it notifies all its view subscribers to update their display.
 */
public class ViewManager implements Manager<ReversiView>  {
  List<ReversiView> reversiViews;

  /**
   * Construct the current ViewManager.
   */
  public ViewManager() {
    reversiViews = new ArrayList<>();
  }

  /**
   * Register for current manager.so that all the views that registered in can be notified
   * once an interesting event occurs (such as a move is made on the model).
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
