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
 * ReversiView will update the board.
 */
public class RegularReversiView extends JFrame implements ReversiView {
  private final ReversiBoardPanel panel;
  private final JLabel whiteScore;
  private final JLabel blackScore;
  private final JLabel turn;
  private final JLabel hasToPassWarning;
  private final ViewManager manager;
  private final JButton hint;

  /**
   * construct the ReversiView with the given parameter.
   *
   * @param model the given model
   */
  public RegularReversiView(ReadOnlyReversiModel model, ViewManager manager) {
    this.panel = new ReversiBoardPanel(model, null);
    this.add(panel);
    this.manager = manager;
    this.manager.register(this);
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
  }

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
          if (getSelectedPosition() == null) {
            showStates("Your are not selecting anything");
            return;
          }
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
    repaint();
  }

  @Override
  public void lockMouseForNonHumanPlayer() {
    panel.setMouseLock(true);
  }

  @Override
  public void showStates(String s) {
    JOptionPane.showMessageDialog(null, s);
  }

  @Override
  public void resetSelectedPosition() {
    panel.resetSelectedPosition();
  }

  @Override
  public void update(ReadOnlyReversiModel model) {
    manager.update(model);
  }
  //score, turn, winner, button,

}
