package view;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Objects;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import model.ModelDirection;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

/**
 * A Panel will draw all the colors, allow users to click on them,
 * and play the game.
 */
public class SquareBoardPanel extends JPanel{
  private static int preferWidth = 100;
  private static int preferHeight = 100;
  /**
   * Our view will need to display a model, so it needs to get the current sequence from the model.
   */
  private final ReadOnlyReversiModel model;
  private final HexGrid hexGrid;
  private RowColPair selectedPosition;
  private RepresentativeColor color;
  private boolean showHints;
  private boolean mouseLock = false;

  private Graphics graphics = null;

  /**
   * construct the Panel with the given constructor.
   *
   * @param model the given model
   */
  public SquareBoardPanel(ReadOnlyReversiModel model, RepresentativeColor color) {
    this.model = Objects.requireNonNull(model);
    selectedPosition = null;
    MouseEventsListener listener = new MouseEventsListener();
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);
    int boardSize = model.getSize();
    while (boardSize > 6) {
      preferWidth += 100;
      preferHeight += 110;
      boardSize -= 6;
    }
    hexGrid = new HexGrid(model, preferWidth, preferHeight, model.getSize());
    this.color = color;
    showHints = false;
  }

  /**
   * Set the color to the panel, represent this panel is for the player in given color.
   *
   * @param color the given color, represents the player
   */
  public void setColor(RepresentativeColor color) {
    this.color = color;
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
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setLayout(new FlowLayout());
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.transform(transformLogicalToPhysical());
    hexGrid.paintComponent(g2d);
    paintNumbers(g2d, selectedPosition);
  }

  /**
   * Paint the number on the specific hexagon represents the number of cells that the current
   * player place here can flipped.
   */
  protected void paintNumbers(Graphics2D g2d, RowColPair pair) {
    if (pair == null) {
      return;
    }
    try {
      if (!model.isGameOver() && color == model.getTurn() && showHints) {
        Font font = new Font("Arial", Font.PLAIN, 4);
        g2d.setFont(font);
        g2d.scale(1, -1);
        g2d.setColor(Color.gray);
        Map<RowColPair, RowColPair> location = hexGrid.getThePositionForDrawingNumber();
        Map<ModelDirection, Integer> values = model.checkMove(pair, model.getTurn());
        int value = 0;
        for (int i : values.values()) {
          value += i;
        }
        Point2D p2d = new Point2D() {
          @Override
          public double getX() {
            return location.get(pair).getRow();
          }

          @Override
          public double getY() {
            return location.get(pair).getCol() - 6;
          }

          @Override
          public void setLocation(double x, double y) {
            // no action for setLocation.
          }
        };
        g2d.drawString(String.valueOf(value),
          (int) inverse().transform(p2d, null).getX(),
          (int) inverse().transform(p2d, null).getY());
        repaint();
      }
    } catch (IllegalStateException e) {

    }

  }

  /**
   * update the current model game state to the view.
   *
   * @param model the current model state
   */
  public void resetHexGrid(ReadOnlyReversiModel model) {
    hexGrid.update(model.getBoard());
    repaint();
  }

  /**
   * Reset the selected position to null and set it color to nonw.
   */
  public void resetSelectedPosition() {
    selectedPosition = null;
  }

  /**
   * flipping the graphics vertically by applying a scaling transformation with
   * a scaling factor of 1 along the x-axis (no change) and a scaling factor of -1
   * along the y-axis.
   *
   * @return the transformation after flipping
   */
  private AffineTransform inverse() {
    AffineTransform ret = new AffineTransform();
    ret.scale(1, -1);
    return ret;
  }

  /**
   * Computes the transformation that converts board coordinates
   * (with (0,0) in center, width and height our logical size)
   * into screen coordinates (with (0,0) in upper-left,
   * width and height in pixels).
   *
   * @return The necessary transformation
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
   * @return The necessary transformation
   */
  private AffineTransform transformPhysicalToLogical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.scale(preferred.getWidth() / getWidth(), preferred.getHeight() / getHeight());
    ret.translate(-getWidth() / 2., -getHeight() / 2.);
    return ret;
  }

  /**
   * If the player is the player need to make move in the turn, show hints. If the game is already
   * over, the player can not interact with panel anymore thus do nothing.
   *
   * @param color the current player
   */
  public void setShowHints(RepresentativeColor color) {
    if (model.isGameOver()) {
      return;
    }
    if (color == model.getTurn()) {
      showHints = !showHints;
      repaint();
    }
  }

  /**
   * Get the user's current selected position in the system of RowColPair.
   *
   * @return a RowColPair represents the current selected position
   */
  public RowColPair getSelectedPosition() {
    return selectedPosition;
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
      if (model.isGameOver() || mouseLock) {
        return;
      }
      Point physicalP = e.getPoint();
      Point2D logicalP = transformPhysicalToLogical().transform(physicalP, null);
      RowColPair selected = hexGrid.getPoint(logicalP);
      System.out.println(selected.getRow());
      System.out.println(selected.getCol());
      if (selected == null) {
        if (selectedPosition == null) {
          return;
        }
        hexGrid.setColor(selectedPosition, RepresentativeColor.NONE);
        selectedPosition = null;
        repaint();
        return;
      }
      if (hexGrid.getColor(selected) == RepresentativeColor.NONE) {
        if (selectedPosition != null && hexGrid.getColor(selectedPosition)
          == RepresentativeColor.CYAN) {
          hexGrid.setColor(selectedPosition, RepresentativeColor.NONE);
        }
        selectedPosition = selected;
        hexGrid.setColor(selected, RepresentativeColor.CYAN);
        repaint();
//        paintNumbers(selectedPosition);
      } else if (hexGrid.getColor(selected) == RepresentativeColor.CYAN) {
        hexGrid.setColor(selected, RepresentativeColor.NONE);
        selectedPosition = null;
      }
      repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      // no action for mouseDragged.
    }
  }
}
