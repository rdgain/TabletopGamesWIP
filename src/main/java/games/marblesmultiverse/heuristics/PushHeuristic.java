package games.marblesmultiverse.heuristics;

import core.AbstractGameState;
import core.CoreConstants;
import core.interfaces.IStateHeuristic;
import games.marblesmultiverse.MMGameState;

public class PushHeuristic implements IStateHeuristic {
    @Override
    public double evaluateState(AbstractGameState gs, int playerId) {
        if (gs.getPlayerResults()[playerId] == CoreConstants.GameResult.WIN_GAME) {
            return maxValue();
        }
        if (gs.getPlayerResults()[playerId] == CoreConstants.GameResult.LOSE_GAME) {
            return minValue();
        }
            MMGameState mmgs = (MMGameState) gs;
        int[] pushedOutCounts = new int[gs.getNPlayers()];
        for (int i = 0; i < gs.getNPlayers(); i++) {
            for (int pushedOut : mmgs.getPlayerMarblesPushedOut().get(i)) {
                if (pushedOut != i) { pushedOutCounts[pushedOut]++; }
            }
        }
        return pushedOutCounts[playerId] - mmgs.getPlayerMarblesPushedOut().get(playerId).size();
    }

    @Override
    public double minValue() {
        return -3;
    }

    @Override
    public double maxValue() {
        return 3;
    }
}
