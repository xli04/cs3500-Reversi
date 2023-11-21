package model.command;

import model.MutableReversiModel;
import model.ReversiCommand;

/**
 * A makePass implementation of ReversiCommand represents the makePass method in model.
 */
public class MakePass implements ReversiCommand {
  @Override
  public void execute(MutableReversiModel model) {
    model.makePass(model.getTurn());
  }
}
