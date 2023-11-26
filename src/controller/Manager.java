package controller;

import model.ReadOnlyReversiModel;

/**
 * Represents a Manager for a model of Reversi. Managers represent a type of observer, and
 * are all observers of the given type whenever an interesting event occurs on the model.
 * observers of a given type can be updated when an event that they care about occurs.
 */
public interface Manager<T> {

  /**
   * Register for current manager.
   *
   * @param observer the observer that wants to register to be managed.
   */
  void register(T observer);

  /**
   * Broadcast to all the observers of that are managed by this manager to update.
   *
   * @param model the model to be updated.
   */
  void update(ReadOnlyReversiModel model);
}
