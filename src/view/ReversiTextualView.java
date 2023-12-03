package view;

import java.io.IOException;
import java.util.Map;
import model.Hexagon;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * class used to display the model in textual. using _ for empty cells,
 * X for black player, and O for white player.
 */
public class ReversiTextualView implements IView {
  private final ReadOnlyReversiModel model;

  private final Appendable out;

  /**
   * construct the ReversiTextualView with given model and appendable.
   *
   * @param model given model
   * @param out   given appendable
   */
  public ReversiTextualView(ReadOnlyReversiModel model, Appendable out) {
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
  public ReversiTextualView(ReadOnlyReversiModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Invalid model");
    }
    this.model = model;
    this.out = null;
  }

  /**
   * The toString method will use the size of the board, which refers to the side length of the
   * board to generate the textual view. If the cell is empty cell it will be represented be _,
   * if it is black, we use X to represent it and for the white, it's O.
   *
   * @return the board's textual view
   */
  @Override
  public String toString() {
    Map<RowColPair, Hexagon> board = model.getCurrentBoard();
    StringBuilder builder = new StringBuilder();
    int size = model.getSize();
    int row = 2 * size - 1;
    int half = row - size;
    int upHalfStarter = 0;
    for (int i = half; i > 0; i--) {
      int blankLine = Math.abs(i);
      builder.append(" ".repeat(blankLine));
      for (int j = upHalfStarter; j <= half; j++) {
        draw(board, builder, -i, j);
      }
      builder.append("\n");
      upHalfStarter--;
    }
    for (int i = -half; i <= half; i++) {
      RepresentativeColor color = board.get(new RowColPair(0, i)).getColor();
      drawGraph(builder, color);
    }
    builder.append("\n");
    int downHalfStarter = half;
    for (int i = 1; i <= half; i++) {
      int blankLine = Math.abs(i);
      builder.append(" ".repeat(blankLine));
      for (int j = -half; j < downHalfStarter; j++) {
        draw(board, builder, i, j);
      }
      if (i != half) {
        builder.append("\n");
      }
      downHalfStarter--;
    }
    return builder.toString();
  }

  /**
   * Drawing _ for empty cells, X for black player, and O for white player.
   *
   * @param builder the current string builder
   * @param color the color of the player
   */
  private void drawGraph(StringBuilder builder, RepresentativeColor color) {
    if (color == RepresentativeColor.NONE) {
      builder.append("_");
      builder.append(" ");
    } else if (color == RepresentativeColor.BLACK) {
      builder.append("X");
      builder.append(" ");
    } else {
      builder.append("O");
      builder.append(" ");
    }
  }

  /**
   * Draw the graph in the color in the given position in given board.
   *
   * @param board the current board
   * @param builder the current builder
   * @param i the row coordinator
   * @param j the column coordinator
   */
  private void draw(Map<RowColPair, Hexagon> board, StringBuilder builder, int i, int j) {
    RepresentativeColor color = board.get(new RowColPair(i, j)).getColor();
    drawGraph(builder, color);
  }

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   * @throws IllegalArgumentException if there is no appendable object
   */
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
    render();
  }

  @Override
  public void addFeatures(Features features) {
    // no features needed for textual view.
  }

  @Override
  public void showMessage(String s) {
    if (out == null) {
      System.out.println(s);
    } else {
      try {
        out.append(s);
      } catch (IOException e) {
        throw new IllegalArgumentException();
      }
    }
  }

  @Override
  public void lockInteractionWithViewForNonHumanPlayer() {
    // no lock needed for textual view because the no button to click.
  }

  @Override
  public void showHints(RepresentativeColor color) {
    // temporary not implement.
  }

  @Override
  public void setColor(RepresentativeColor color) {
    // no need to set color since the textual view do not have panel.
  }

  @Override
  public void update(ReadOnlyReversiModel model, RepresentativeColor player) {
    render();
  }
}
