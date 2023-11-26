package model.command;

import model.MutableReversiModel;
import model.ReversiCommand;
import model.RowColPair;

/**
 * A place move implementation of ReversiCommand represents the place move method in model.
 */
public final class PlaceMove implements ReversiCommand {
  private final RowColPair pair;

  /**
   * construct the PlaceMove with the given parameter.
   *
   * @param pair the given position
   */
  public PlaceMove(RowColPair pair) {
    this.pair = pair;
  }

  @Override
  public void execute(MutableReversiModel model) {
    model.placeMove(pair, model.getTurn());
  }
}
