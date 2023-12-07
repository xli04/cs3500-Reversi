package view;

import java.awt.*;

import javax.swing.*;

import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

public interface IPanel {
  void setColor(RepresentativeColor color);
  void setMouseLock(Boolean lock);
  void paintComponent(Graphics g);
  void resetHexGrid(ReadOnlyReversiModel model);
  void resetSelectedPosition();
  void setShowHints(RepresentativeColor color);
  RowColPair getSelectedPosition();

}
