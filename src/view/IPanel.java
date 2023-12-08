package view;

import java.util.Map;

import javax.swing.*;

import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;

public interface IPanel {
  void setColor(RepresentativeColor color);
  void setMouseLock(Boolean lock);
  void resetGrid(ReadOnlyReversiModel model);
  void resetSelectedPosition();
  void setDecorator(RepresentativeColor color);
  RowColPair getSelectedPosition();
  JPanel getPanel();
  Map<RowColPair, RowColPair> getDrawingPoints();
  void addDecorator(PanelDecorator decorator);

}
