package Adapter.Strategy;

import Provider.src.cs3500.reversi.model.CellPosition;
import Provider.src.cs3500.reversi.model.PlayerTurn;
import Provider.src.cs3500.reversi.model.ReversiModel;
import Provider.src.cs3500.reversi.strategy.AvoidingNextToCorners;
import model.Direction;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import strategy.FallibleStrategy;

import java.util.Map;
import java.util.Optional;

public class ProvideAvoidingNextToCornersStrategy extends AvoidingNextToCorners implements FallibleStrategy {
    @Override
    public Optional<RowColPair> choosePosition(ReadOnlyReversiModel model, RepresentativeColor player) {
        PlayerTurn turn = player == RepresentativeColor.WHITE ? PlayerTurn.WHITE : PlayerTurn.BLACK;
        Optional<CellPosition> cell = super.chooseCellPosition((ReversiModel) model, turn);
        if (cell.isPresent()) {
            Map<Direction, Integer> move = model.checkMove(convert(cell.get()), model.getTurn());
            int value = 0;
            for (int i : move.values()) {
                value += i;
            }
            if (value == 0) {
                return Optional.empty();
            } else {
                RowColPair pair = convert(cell.get());
                return Optional.of(pair);
            }
        }
        return Optional.empty();
    }
    private RowColPair convert(CellPosition position) {
        return new RowColPair(-position.getX(), position.getZ());
    }
}
