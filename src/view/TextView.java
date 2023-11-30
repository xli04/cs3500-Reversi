package view;

import java.io.IOException;

/**
 * A marker interface for all text-based views, to be used in the Reversi game.
 */
public interface TextView {
  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   * @throws IllegalArgumentException if there is no appendable object
   */
  void render() throws IOException;
}
