package view;

import java.util.ArrayList;
import java.util.List;
import controller.Manager;
import model.ReadOnlyReversiModel;

/**
 * A ViewManager represents a observer pattern to all the views, once the
 * model is updated, notify all the view.
 */
public class ViewManager implements Manager<View>  {
  List<View> views;

  /**
   * Construct the current ViewManager.
   */
  public ViewManager() {
    views = new ArrayList<>();
  }

  /**
   * Register for current manager.
   *
   * @param element the view that wants to register
   */
  @Override
  public void register(View element) {
    views.add(element);
  }

  /**
   * update the current game state to all the view that shared the same model.
   *
   * @param model updated model
   */
  @Override
  public void update(ReadOnlyReversiModel model) {
    for (View view : views) {
      view.resetPanel(model);
    }
  }

}
