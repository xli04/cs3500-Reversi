package view;

import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * A Panel will draw all the colors, allow users to click on them,
 * and play the game.
 */
public class SquareBoardPanel extends JPanel implements IPanel {
  private int preferWidth = 100;
  private int preferHeight = 100;
  /**
   * Our view will need to display a model, so it needs to get the current sequence from the model.
   */
  private final ReadOnlyReversiModel model;
  private final SquareGrid squareGrid;
  private RowColPair selectedPosition;
  private boolean mouseLock = false;

  /**
   * construct the Panel with the given constructor.
   *
   * @param model the given model
   */
  public SquareBoardPanel(ReadOnlyReversiModel model) {
    this.model = Objects.requireNonNull(model);
    selectedPosition = null;
    MouseEventsListener listener = new MouseEventsListener();
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);
    int boardSize = model.getSize();
    while (boardSize > 8) {
      preferWidth += 100;
      preferHeight += 110;
      boardSize -= 6;
    }
    squareGrid = new SquareGrid(model, preferWidth, preferHeight, model.getSize());
  }

  /**
   * Set the mouse lock to the panel, in order to keep human players away from bothering
   * the ai players by marking the cell as selected.
   *
   * @param lock whether to lock the mouse event
   */
  public void setMouseLock(Boolean lock) {
    mouseLock = lock;
  }

  /**
   * This method tells Swing what the "natural" size should be
   * for this panel.  Here, we set it to 400x400 pixels.
   *
   * @return Our preferred *physical* size.
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(800, 800);
  }

  /**
   * Conceptually, we can choose a different coordinate system
   * and pretend that our panel is 40x40 "cells" big. You can choose
   * any dimension you want here, including the same as your physical
   * size (in which case each logical pixel will be the same size as a physical
   * pixel, but perhaps your calculations to position things might be trickier)
   *
   * @return Our preferred *logical* size.
   */
  private Dimension getPreferredLogicalSize() {
    return new Dimension(preferWidth, preferHeight);
  }

  /**
   * first, get the coordinators for the middle point(0,0), use its coordinators to get the point
   * surround it, then if the hexagon was unoccupied(the color is none or cyan), we will fill the
   * hexagon, otherwise draw a circle in that hexagon based on the coordinators for its topping
   * point.
   *
   * @param g the <code>Graphics</code> object to protect
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setLayout(new FlowLayout());
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.transform(transformLogicalToPhysical());
    squareGrid.paintComponent(g2d);
  }

  /**
   * update the current model game state to the view.
   *
   * @param model the current model state
   */
  public void resetGrid(ReadOnlyReversiModel model) {
    squareGrid.update(model.getBoard());
    repaint();
  }

  /**
   * Reset the selected position to null and set it color to nonw.
   */
  public void resetSelectedPosition() {
    selectedPosition = null;
  }

  /**
   * Computes the transformation that converts board coordinates
   * (with (0,0) in center, width and height our logical size)
   * into screen coordinates (with (0,0) in upper-left,
   * width and height in pixels).
   *
   * @return The AffineTransform necessary for the logical-to-physical transformation.
   */
  private AffineTransform transformLogicalToPhysical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.translate(getWidth() / 2., getHeight() / 2.);
    ret.scale(getWidth() / preferred.getWidth(), getHeight() / preferred.getHeight());
    return ret;
  }

  /**
   * Computes the transformation that converts screen coordinates
   * (with (0,0) in upper-left, width and height in pixels)
   * into board coordinates (with (0,0) in center, width and height
   * our logical size).
   *
   * @return The AffineTransform necessary for the physical-to-logical transformation.
   */
  private AffineTransform transformPhysicalToLogical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.scale(preferred.getWidth() / getWidth(), preferred.getHeight() / getHeight());
    ret.translate(-getWidth() / 2., -getHeight() / 2.);
    return ret;
  }

  /**
   * Get the user's current selected position in the system of RowColPair.
   *
   * @return a RowColPair represents the current selected position
   */
  public RowColPair getSelectedPosition() {
    return selectedPosition;
  }

  @Override
  public JPanel getPanel() {
    return this;
  }

  private class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      // no action for mousePressed.
    }

    /**
     * If the selectedPosition was already occupied(its color become black or white), maintain
     * the current selected position. then detect if the user select a valid hexagon successfully,
     * if the user do selected a hexagon, temporarily change the color of the hexagon to cyan
     * if the user ten select another place(no matter it is a valid or invalid position), reset
     * the selected position and reset its color. if user clicking on it again,
     * or clicking on another cell or clicking outside the boundary of the board. it will
     * reset the current selected position to null or another hexagon.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
      if (!model.isGameOver() && !mouseLock) {
        Point physicalP = e.getPoint();
        Point2D logicalP = transformPhysicalToLogical().transform(physicalP, null);
        RowColPair selected = squareGrid.getPoint(logicalP);
        if (selected == null) {
          if (selectedPosition == null) {
            return;
          }
          squareGrid.setColor(selectedPosition, RepresentativeColor.NONE);
          selectedPosition = null;
          repaint();
          return;
        }
        System.out.println(selected.getRow());
        System.out.println(selected.getCol());
        System.out.println();
        if (squareGrid.getColor(selected) == RepresentativeColor.NONE) {
          if (selectedPosition != null && squareGrid.getColor(selectedPosition)
              == RepresentativeColor.CYAN) {
            squareGrid.setColor(selectedPosition, RepresentativeColor.NONE);
          }
          selectedPosition = selected;
          squareGrid.setColor(selected, RepresentativeColor.CYAN);
          repaint();
        } else if (squareGrid.getColor(selected) == RepresentativeColor.CYAN) {
          squareGrid.setColor(selected, RepresentativeColor.NONE);
          selectedPosition = null;
        }
        repaint();
      }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      // no action for mouseDragged.
    }
  }
}
