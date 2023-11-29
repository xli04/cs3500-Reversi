package model;

/**
 * An interface ModelListener that used to represent a type of the listener to the model
 * they will get notified once the model was changed.
 */
public interface ModelListener {

  /**
   * update the listener when the model was changed.
   */
  void update();
}
