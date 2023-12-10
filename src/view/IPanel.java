package view;

import javax.swing.JPanel;
import model.ReadOnlyReversiModel;
import model.RowColPair;

/**
 * The PanelDecorator interface represents decorators that can enhance the functionality
 * of a graphical panel in a UI widget library.
 */
public interface IPanel {

  /**
   * Sets the mouse lock to the panel, preventing user interactions if locked.
   *
   * @param lock A boolean indicating whether to lock or unlock mouse events.
   */
  void setMouseLock(Boolean lock);

  /**
   * Updates the current model game state to the view by resetting the hexagonal grid.
   *
   * @param model The current model state to update the view.
   */
  void resetGrid(ReadOnlyReversiModel model);

  /**
   * Resets the selected position to null and resets its color.
   */
  void resetSelectedPosition();

  /**
   * Gets the user's currently selected position in the system of RowColPair.
   *
   * @return A RowColPair representing the current selected position.
   */
  RowColPair getSelectedPosition();

  /**
   * Returns the JPanel associated with this view.
   *
   * @return The JPanel representing the view.
   */
  JPanel getPanel();

}
