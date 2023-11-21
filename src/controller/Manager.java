package controller;

import model.ReadOnlyReversiModel;

/**
 * A Manager interface represents a observer pattern, once the
 * model is updated, notify all the element that interest in it.
 */
public interface Manager<T> {

  /**
   * Register for current manager.
   *
   * @param element the element that wants to register
   */
  void register(T element);

  /**
   * update the current game state to all the elements that shared the same model.
   *
   * @param model if there exist valid move in the current game board
   */
  void update(ReadOnlyReversiModel model);
}
