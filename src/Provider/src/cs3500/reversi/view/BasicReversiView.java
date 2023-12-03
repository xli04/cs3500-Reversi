package Provider.src.cs3500.reversi.view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Provider.src.cs3500.reversi.model.GamePiece;
import Provider.src.cs3500.reversi.model.PlayerTurn;
import Provider.src.cs3500.reversi.model.ReadOnlyReversiModel;


/**
 * Represents the graphical user interface for the Reversi game. This view includes a game board
 * represented by a JPanel added to the JFrame, it allows inputs from keys and handles mouse clicks.
 */
public class BasicReversiView extends JFrame implements ReversiView {
  JReversiPanel panel;
  private final List<ViewFeatures> featuresListeners;
  ReadOnlyReversiModel model;

  /**
   * Constructs a ReversiView with the given ReadOnlyReversiModel.
   *
   * @param model takes in a ReadOnlyReversiModel to allow the view to access information about the
   *              current state of the model.
   */
  public BasicReversiView(ReadOnlyReversiModel model) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.model = model;
    this.panel = new JReversiPanel(this.model);
    this.add(panel);
    KeyListener listener = new KeyListener();
    this.addKeyListener(listener);
    this.featuresListeners = new ArrayList<>();
    this.pack();
  }

  @Override
  public void display(boolean show) {
    this.setVisible(show);
  }

  @Override
  public void addFeatureListener(ViewFeatures features) {
    this.featuresListeners.add(Objects.requireNonNull(features));
    this.panel.addFeaturesListener(features);
  }

  // notifies a view if it is that players turn
  public void notifyPlayer() {
    JOptionPane.showMessageDialog(this, "It's your turn!");
  }

  @Override
  public void showGameOverMessage(PlayerTurn player, GamePiece winner) {
    switch (winner) {
      case BLACK:
        if (player.equals(PlayerTurn.BLACK)) {
          JOptionPane.showMessageDialog(this,
              "The game is over.\nBlack won!");
        } else {
          JOptionPane.showMessageDialog(this,
              "The game is over.\nWhite lost.");
        }
        break;
      case WHITE:
        if (player.equals(PlayerTurn.WHITE)) {
          JOptionPane.showMessageDialog(this,
              "The game is over.\nWhite won!");
        } else {
          JOptionPane.showMessageDialog(this,
              "The game is over.\nBlack lost.");
        }
        break;
      default:
        JOptionPane.showMessageDialog(this,
            "The game is over.\nThe game was tied.");
        break;
    }
  }

  @Override
  public void update() {
    this.panel.repaint();
  }

  /**
   * A KeyListener implementation to handle key events for the ReversiView. It sends notifications
   * to anything that is a listener for BasicReversiView.
   */
  private class KeyListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      boolean mOrP = false;
      // passes a players turn
      if (BasicReversiView.this.model.getCurrentPlayer()
          .equals(BasicReversiView.this.featuresListeners.get(0).controllerPlayer())) {
        if (e.getKeyChar() == 'p') {
          BasicReversiView.this.featuresListeners.get(0).passTurn();
          mOrP = true;
        }
        // plays a turn with the currently highlighted cell
        if (e.getKeyChar() == 'm') {
          if (BasicReversiView.this.panel.anyCyanCells()) {
            try {
              BasicReversiView.this.featuresListeners.get(0)
                  .playTurn(BasicReversiView.this.panel.getCurCell());
              mOrP = true;
            } catch (IllegalStateException ex) {
              JOptionPane.showMessageDialog(BasicReversiView.this,
                  "This is a not a legal move.");
            }
          }

        }
        if (mOrP) {
          // resets all the hexagons to gray once a turn has been played
          BasicReversiView.this.panel.resetHex();
          BasicReversiView.this.panel.repaint();
        }
      }
    }
  }
}
