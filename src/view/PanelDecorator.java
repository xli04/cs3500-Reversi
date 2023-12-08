package view;

import java.awt.*;
import java.util.Map;

import model.RepresentativeColor;
import model.RowColPair;

public interface PanelDecorator {
  void setColor(RepresentativeColor color);
  void setFunctionality(RepresentativeColor color);
  void paint(Graphics2D g2d, RowColPair pair);
  void setDrawingPoints(Map<RowColPair, RowColPair> map);
}
