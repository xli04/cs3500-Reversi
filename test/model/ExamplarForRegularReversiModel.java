package model;

import org.junit.Assert;
import org.junit.Test;

import model.MutableReversiModel;
import model.RegularReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import view.ReversiTextualView;

/**
 * a test class that used to give readers a quick understanding of model.
 */
public class ExamplarForRegularReversiModel {
  @Test
  public void testGeneralUnderstanding() {
    MutableReversiModel model = new RegularReversiModel();
    Assert.assertEquals(6, model.getSize()); // the default size and side length of the board is 6
    Assert.assertEquals(RepresentativeColor.BLACK, model.getTurn()); // Player Black moves first
    ReversiTextualView view = new ReversiTextualView(model);
    Assert.assertEquals(
        "     _ _ _ _ _ _ \n"
        +
        "    _ _ _ _ _ _ _ \n"
        +
        "   _ _ _ _ _ _ _ _ \n"
        +
        "  _ _ _ _ _ _ _ _ _ \n"
        +
        " _ _ _ _ X O _ _ _ _ \n"
        +
        "_ _ _ _ O _ X _ _ _ _ \n"
        +
        " _ _ _ _ X O _ _ _ _ \n"
        +
        "  _ _ _ _ _ _ _ _ _ \n"
        +
        "   _ _ _ _ _ _ _ _ \n"
        +
        "    _ _ _ _ _ _ _ \n"
        +
        "     _ _ _ _ _ _ ", view.toString()); // overview of the start board and start cells
    Assert.assertThrows(IllegalStateException.class, ()
        -> model.placeMove(new RowColPair(0, 0),
        RepresentativeColor.BLACK)); // place at (4,4) can not flip anything
    Assert.assertEquals(RepresentativeColor.BLACK, model.getTurn()); // the turn will not be alter
    Assert.assertEquals(RepresentativeColor.WHITE, model.getColorAt(new RowColPair(-1, 1)));
    model.placeMove(new RowColPair(-1, 2), RepresentativeColor.BLACK);
    Assert.assertEquals(RepresentativeColor.BLACK, model.getColorAt(new RowColPair(-1, 1)));
    // successfully placed a cell and flip the cell in position(4,6)
    Assert.assertEquals(RepresentativeColor.WHITE, model.getTurn());
    // alter the turn after successfully placed a cell
    model.makePass(RepresentativeColor.WHITE);
    model.makePass(RepresentativeColor.BLACK);
    Assert.assertTrue(model.isGameOver()); // game ends if two players pass in a row
  }
}
