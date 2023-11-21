package view;

import model.Player;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * A marker interface for all GUI views, to be used in the Reversi game.
 */
public interface View {

  /**
   * display the view.
   */
  void display();

  /**
   * Get the user's current selected position in the board.
   *
   * @return the coordinators that chosen by the user
   */
  RowColPair getSelectedPosition();

  /**
   * repaint the board with the modification.
   */
  void refresh();

  /**
   * after the user successfully place a cell in the selected position,
   * reset the select position to null.
   */
  void resetSelectedPosition();

  /**
   * update the current game state to the drawing board.
   *
   * @param model the current model
   */
  void resetPanel(ReadOnlyReversiModel model);

  /**
   * Add the features to the view.
   *
   * @param features the feature gonna to add.
   */
  void addFeatures(Features features);
}
