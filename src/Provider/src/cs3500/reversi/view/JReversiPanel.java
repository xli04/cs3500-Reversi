package Provider.src.cs3500.reversi.view;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import java.awt.event.MouseEvent;

import Provider.src.cs3500.reversi.model.ReadOnlyReversiModel;
import Provider.src.cs3500.reversi.model.CellPosition;

/**
 * A JReversiPanel will draw all the hexagons on the board with the current game piece it has
 * according to the model. It allows users to highlight a space by clicking it, and it will change
 * colors when clicked and return the coordinates of the cell that has been clicked.
 */
public class JReversiPanel extends JPanel {
  // the reversi model the view is displaying
  private final ReadOnlyReversiModel model;
  // the list of hexagon buttons displayed in the view
  private final List<HexagonButton> buttonList;
  // the width of each hexagon displayed
  private final double hexWidth;
  // the side length of one hexagon on the board
  private final double sideLength;
  // the height of the initial JPanel
  private final int height;
  // a map containing a hexagon button and the cell it is representing
  private final Map<HexagonButton, CellPosition> cellMap;
  // a list of the features listening to the panel
  private final List<ViewFeatures> featuresListeners;
  // the cell that has been clicked
  private CellPosition cell;

  /**
   * Constructs a JReversiPanel and initializes all the fields, it also adds a mouse listener to
   * handle clicking in the panel, as well as setting the background color of the panel to gray.
   *
   * @param model takes in a ReadOnlyReversiModel to allow the view to access information about the
   *              current state of the model.
   */
  public JReversiPanel(ReadOnlyReversiModel model) {
    this.model = Objects.requireNonNull(model);
    this.featuresListeners = new ArrayList<>();
    MouseEventsListener listener = new MouseEventsListener();
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);
    this.setBackground(Color.DARK_GRAY);
    this.buttonList = new ArrayList<>(List.of());
    this.hexWidth = (double) 600 / (model.getBoardWidth() * 2 - 1);
    this.sideLength = 2 * ((hexWidth / 2) * Math.tan(Math.PI / 6));
    this.height = (int) (sideLength * (this.model.getBoardWidth() * 2 + this.model.getBoardWidth()
        - 1)) + 1;
    this.cellMap = new HashMap<>();
    // creates all the hexagons that should be on the board
    this.createButtonList(new Rectangle(600, height));
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(600, height);
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();

    for (HexagonButton hex : buttonList) {
      // gets the current game piece in the hexagon
      hex.updateGamePiece(this.model.getBoard().get(this.cellMap.get(hex)));
      g2d.setColor(hex.getFillColor());
      g2d.fill(hex.getPath());
      g2d.setColor(hex.getColor());
      // does not draw a game piece if the piece color is gray
      if (!hex.getColor().equals(Color.GRAY)) {
        g2d.fill(hex.createGamePiece());
      }
      g2d.setColor(Color.BLACK);
      g2d.draw(hex.getPath());
    }

  }

  // creates a list of HexagonButtons to be drawn on the JPanel
  private void createButtonList(Rectangle bounds) {
    // initializes and sorts the list of cells
    List<CellPosition> cellList = new ArrayList<>(List.of());
    cellList.addAll(this.model.getBoard().keySet());
    cellList.sort(Comparator.comparingInt(CellPosition::getY));
    cellList.sort(Comparator.comparingInt(CellPosition::getX).reversed());

    // gets the width of the board
    int boardWidth = this.model.getBoardWidth();

    // creates the hexagons in the top half of the board including the middle row
    double startY = bounds.y + sideLength / 2;
    for (int x = boardWidth - 1; x >= 0; x--) {
      double startX = bounds.x + x * (hexWidth / 2);
      for (int cellNum = x + 1; cellNum <= boardWidth * 2 - 1; cellNum++) {
        startX = createButton(cellList, startY, startX);
      }
      startY += sideLength * 1.5;
    }

    // creates the hexagons in the bottom half of the board including the middle row
    int numberOfCells = (boardWidth * 2) - 2;
    for (int x = -1; x > -boardWidth; x--) {
      double startX = bounds.x + -x * (hexWidth / 2);
      for (int cellNum = numberOfCells; cellNum > 0; cellNum--) {
        startX = createButton(cellList, startY, startX);
      }
      numberOfCells--;
      startY += sideLength * 1.5;
    }
  }

  // creates instances of HexagonButtons based off the given inputs, and adds them to a hashmap with
  // the cell that corresponds to it and adds it to a list of hexagons
  private double createButton(List<CellPosition> cellList, double startY, double startX) {
    buttonList.add(new HexagonButton(this.model.getBoard().get(cellList.get(0)), sideLength, startX,
        startY));
    cellMap.put(buttonList.get(buttonList.size() - 1), cellList.remove(0));
    buttonList.get(buttonList.size() - 1).createHexagon();
    startX += hexWidth;
    return startX;
  }

  // returns the currently highlighted cell
  public CellPosition getCurCell() {
    return this.cell;
  }

  // adds the given feature as a listener of the view, so when things are updated and notifications
  // of those updates are sent then the feature will be on the list to receive the notification
  public void addFeaturesListener(ViewFeatures features) {
    this.featuresListeners.add(Objects.requireNonNull(features));
  }

  /**
   * Resets all the hexagons in the button list back to gray.
   */
  public void resetHex() {
    for (HexagonButton hex: buttonList) {
      hex.changeFillColor(Color.GRAY);
    }
  }

  /**
   * Returns true is there are any highlighted cells on the board.
   *
   * @return true if there are any cyan cells, false if they are all gray.
   */
  public boolean anyCyanCells() {
    for (HexagonButton hex: buttonList) {
      if (hex.getFillColor().equals(Color.CYAN)) {
        return true;
      }
    }
    return false;
  }

  /**
   * A MouseEventsListener implementation to handle mouse events for the JReversiPanel.
   */
  private class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mouseReleased(MouseEvent e) {
      // only allows the player to click anything if it is their turn
      if (JReversiPanel.this.model.getCurrentPlayer().equals(JReversiPanel.this.featuresListeners
          .get(0).controllerPlayer()) && !JReversiPanel.this.model.isGameOver()) {
        // checks to see which hexagon was clicked if any
        for (HexagonButton hex : JReversiPanel.this.buttonList) {
          if (hex.getPath().contains(e.getX(), e.getY())) {
            if (hex.getFillColor().equals(Color.CYAN)) {
              // changes a cyan hexagon back to gray when clicked
              hex.changeFillColor(Color.GRAY);
            } else if (hex.getColor().equals(Color.GRAY)) {
              // changes a hexagon to cyan if it is gray and has been clicked
              hex.changeFillColor(Color.CYAN);
              // returns the coordinate of the cell that was clicked
              JReversiPanel.this.cell = JReversiPanel.this.cellMap.get(hex);
            }
          } else {
            // if the hexagon was not clicked it should be gray
            if (hex.getFillColor().equals(Color.CYAN)) {
              hex.changeFillColor(Color.GRAY);
            }
          }
        }

      }
      JReversiPanel.this.repaint();
    }
  }

}