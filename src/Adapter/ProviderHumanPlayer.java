package Adapter;

import Provider.src.cs3500.reversi.controller.Player;
import Provider.src.cs3500.reversi.model.CellPosition;
import Provider.src.cs3500.reversi.model.PlayerTurn;
import Provider.src.cs3500.reversi.model.ReversiModel;
import model.*;
import strategy.InfallibleStrategy;

public class ProviderHumanPlayer extends ReversiHumanPlayer implements Player {

    /**
     * Construct the current player with the strategy the player will use for choose next move.
     */
    public ProviderHumanPlayer() {
    }

    @Override
    public CellPosition takeTurn(ReversiModel model) {
        return convertBack(super.chooseNextMove((ReadOnlyReversiModel) model).get());
    }

    @Override
    public PlayerTurn getPlayer() {
        if (super.getColor() == RepresentativeColor.BLACK) {
            return PlayerTurn.BLACK;
        }
        return PlayerTurn.WHITE;
    }

    @Override
    public boolean isAi() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProviderHumanPlayer)) {
            return false;
        }
        return getPlayer() == ((ProviderHumanPlayer) obj).getPlayer();
    }

    private CellPosition convertBack(RowColPair pair) {
        CubeCoordinateTrio trio = pair.convertToCube();
        return new CellPosition(-trio.getRow(), -trio.getRightCol(), trio.getLeftCol());
    }
}
