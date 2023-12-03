package Adapter.Strategy;

import Provider.src.cs3500.reversi.model.CellPosition;
import Provider.src.cs3500.reversi.model.PlayerTurn;
import Provider.src.cs3500.reversi.model.ReversiModel;
import Provider.src.cs3500.reversi.strategy.FinalStrategy;
import Provider.src.cs3500.reversi.strategy.ReversiStrategy;
import model.Direction;
import model.ReadOnlyReversiModel;
import model.RepresentativeColor;
import model.RowColPair;
import strategy.InfallibleStrategy;

import java.util.Map;
import java.util.Optional;

public class ProviderCompleteStrategy extends FinalStrategy implements InfallibleStrategy {

    /**
     * Constructs a FinalStrategy that will work as an AI.
     *
     * @param strategyToTry a strategy to be used.
     */
    public ProviderCompleteStrategy(ReversiStrategy strategyToTry) {
        super(strategyToTry);
    }

    @Override
    public RowColPair choosePosition(ReadOnlyReversiModel model, RepresentativeColor player) {
        PlayerTurn turn = player == RepresentativeColor.WHITE ? PlayerTurn.WHITE : PlayerTurn.BLACK;
       CellPosition cell = super.chooseCellPosition((ReversiModel) model, turn);
        RowColPair pair = convert(cell);
        Map<Direction, Integer> move = model.checkMove(pair, model.getTurn());
        int value = 0;
        for (int i : move.values()) {
            value += i;
        }
        if (value == 0) {
            throw new IllegalStateException("There are no possible moves chosen by this strategy");
        }
        return pair;
    }
    private RowColPair convert(CellPosition position) {
        return new RowColPair(-position.getX(), position.getZ());
    }

}
