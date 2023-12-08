package view;

import java.io.IOException;

import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

public class SquareTextualView implements IView{
  private final ReadOnlyReversiModel model;

  private final Appendable out;

  /**
   * construct the ReversiTextualView with given model and appendable.
   *
   * @param model given model
   * @param out   given appendable
   */
  public SquareTextualView(ReadOnlyReversiModel model, Appendable out) {
    if (model == null || out == null) {
      throw new IllegalArgumentException("Invalid model or appendable");
    }
    this.model = model;
    this.out = out;
  }

  /**
   * construct the ReversiTextualView with given model.
   *
   * @param model given model
   */
  public SquareTextualView(ReadOnlyReversiModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Invalid model");
    }
    this.model = model;
    this.out = null;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (int row = 0; row < model.getSize(); row ++) {
      for (int col = 0; col < model.getSize(); col ++) {
        RepresentativeColor color = model.getColorAt(new RowColPair(row, col));
        if (color == RepresentativeColor.BLACK) {
          builder.append("X");
        } else if (color == RepresentativeColor.WHITE) {
          builder.append("O");
        } else {
          builder.append("_");
        }
        if (col != model.getSize() - 1) {
          builder.append(" ");
        }
      }
      builder.append("\n");
    }
    return builder.toString();
  }

  private void render() {
    if (out == null) {
      throw new IllegalArgumentException();
    }
    try {
      out.append(toString());
    } catch (IOException e) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public void display() {
    // no action
  }

  @Override
  public void addFeatures(Features features) {
    // no action
  }

  @Override
  public void showMessage(String s) {
    // no action
  }

  @Override
  public void lockInteractionWithViewForNonHumanPlayer() {
    // no action
  }

  @Override
  public void showHints(RepresentativeColor color) {
    // no action
  }

  @Override
  public void setColor(RepresentativeColor color) {
    // no action
  }

  @Override
  public void update(ReadOnlyReversiModel model, RepresentativeColor player) {
    // no action
  }
}
