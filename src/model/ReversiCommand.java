package model;

/**
 * A reversi command class represents a command that the model could make.
 */
public interface ReversiCommand {

  /**
   * execute the command inside the model.
   *
   * @param model the given model
   */
  void execute(MutableReversiModel model);
}
