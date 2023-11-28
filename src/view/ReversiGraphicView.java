package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import model.ReadOnlyReversiModel;
import model.RowColPair;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import model.RepresentativeColor;

/**
 * Represents a graphic view for the reversi game, it will contains a board to show the
 * current states for the model and a panel that shows the current score for both black
 * and white players, and the hasToPassWarning. It also has a hint button used to determin
 * whether the player wants to get some hints.
 */
public class ReversiGraphicView extends JFrame implements GraphicView {
  private final ReversiBoardPanel panel;
  private final JLabel whiteScore;
  private final JLabel blackScore;
  private final JLabel turn;
  private final JLabel hasToPassWarning;
  private final JButton hint;

  /**
   * construct the ReversiView with the given parameter. Register the current
   * view into the manager in order to get update once the model is changed,
   * add a board to show the current states for the model and a panel that shows
   * the current score for both black and white players, and the hasToPassWarning.
   * also add a hint button used to determine whether the player wants to get some hints.
   *
   * @param model the given model
   */
  public ReversiGraphicView(ReadOnlyReversiModel model) {
    this.panel = new ReversiBoardPanel(model, null);
    this.add(panel);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    hint = new JButton("Hint");
    hint.setFocusable(false);
    whiteScore = new JLabel("White: " + model.getScore(RepresentativeColor.WHITE));
    blackScore = new JLabel("Black: " + model.getScore(RepresentativeColor.BLACK));
    turn = new JLabel("");
    hasToPassWarning = new JLabel("");
    JPanel northPan = new JPanel();
    northPan.add(hint);
    northPan.add(whiteScore);
    northPan.add(blackScore);
    northPan.add(turn);
    northPan.add(hasToPassWarning);
    add(northPan, BorderLayout.NORTH);
    this.pack();
  }

  @Override
  public void setColor(RepresentativeColor color) {
    panel.setColor(color);
  }

  /**
   * set the default features, target is to notify the user they are
   * doing something.
   */
  private void setTestFeature() {
    addFeatures(new Features() {
      @Override
      public void placeMove(RowColPair pair) {
        // no action for test feature.
      }

      @Override
      public void makePass() {
        // no action for test feature.
      }

      @Override
      public void showHints(){
        // no action for test feature.
      }
    });
  }

  @Override
  public void display() {
    this.setVisible(true);
  }

  @Override
  public RowColPair getSelectedPosition() {
    return panel.getSelectedPosition();
  }


  @Override
  public void resetPanel(ReadOnlyReversiModel model) {
    panel.resetHexGrid(model);
    resetScore(model.getScore(RepresentativeColor.BLACK),
        model.getScore(RepresentativeColor.WHITE));
    this.repaint();
  }

  /**
   * Add the features to the view, when the player press the enter, it means the player
   * wants to place a cell that in the color this user represent to the selected position,
   * which should be marked as cyan, when player press space, it means the player wants to
   * make pass in this turn, and if the player click the hint button, it means the player
   * need some hints then show the hints for that player.
   *
   * @param features the feature gonna to add.
   */
  @Override
  public void addFeatures(Features features) {
    hint.addActionListener(e -> features.showHints());
    this.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        // no action for keyTyped.
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          features.placeMove(getSelectedPosition());
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          features.makePass();
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // no action for keyReleased.
      }
    });
  }

  /**
   * Reset the message to notify the users the score.
   *
   * @param black the current score for black player
   * @param white the current score for white player
   */
  private void resetScore(int black, int white) {
    whiteScore.setText("White: " + white);
    blackScore.setText("Black: " + black);
  }

  @Override
  public void setHasToPassWarning(boolean hasToPass, boolean isYourTurn) {
    if (hasToPass && isYourTurn) {
      hasToPassWarning.setText("You can only pass");
      hasToPassWarning.setForeground(Color.RED);
    } else {
      hasToPassWarning.setText("");
    }
  }

  @Override
  public void toggleTurn(RepresentativeColor color, boolean isYourTurn) {
    if (isYourTurn) {
      turn.setText("Current turn: " + color.getName() + " Is Your turn");
    } else {
      turn.setText("Current turn: " + color.getName());
    }
  }

  @Override
  public void setGameOverState(RepresentativeColor winner, boolean winOrNot) {
    hasToPassWarning.setText("");
    if (winOrNot) {
      turn.setText("Game is over, winner is " + winner + " You win!!");
    } else {
      turn.setText("Game is over, winner is " + winner);
    }
  }

  @Override
  public void showHints(RepresentativeColor color) {
    panel.setShowHints(color);
  }

  @Override
  public void lockMouseForNonHumanPlayer() {
    panel.setMouseLock(true);
  }

  @Override
  public void showMessage(String s) {
    JOptionPane.showMessageDialog(null, s);
  }

  @Override
  public void resetSelectedPosition() {
    panel.resetSelectedPosition();
  }

}
