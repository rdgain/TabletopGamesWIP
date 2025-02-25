package games.marblesmultiverse.heuristics;

import core.AbstractGameState;
import core.CoreConstants;
import core.interfaces.IStateHeuristic;
import games.marblesmultiverse.MMGameState;

import static games.marblesmultiverse.heuristics.Utils.getPieces;

public class PieceCountHeuristic implements IStateHeuristic {
    @Override
    public double evaluateState(AbstractGameState gs, int playerId) {
        if (gs.getPlayerResults()[playerId] == CoreConstants.GameResult.WIN_GAME) {
            return maxValue();
        }
        if (gs.getPlayerResults()[playerId] == CoreConstants.GameResult.LOSE_GAME) {
            return minValue();
        }
        int totalPieces = 10;  // TODO get this from somewhere else
        MMGameState mmgs = (MMGameState) gs;
        return (double)getPieces(mmgs, playerId).size() / totalPieces;
    }
}
