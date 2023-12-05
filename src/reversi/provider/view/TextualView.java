package reversi.provider.view;

/**
 * A marker interface for all text-based views, to be used in the Reversi game.
 */
public interface TextualView {
  /**
   * Renders the current state of the Reversi Model as a string to represent all the cells
   * in the model.
   *
   * @return the formatted board as a string
   */
  String toString();
}
