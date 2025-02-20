package games.marblesmultiverse.heuristics;

import core.AbstractGameState;
import core.CoreConstants;
import core.interfaces.IStateHeuristic;
import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.components.BoardSpot;
import games.marblesmultiverse.components.MMTypes;
import utilities.Vector2D;
import java.util.ArrayList;

import static games.marblesmultiverse.heuristics.Utils.getPieces;

public class OwnDistanceHeuristic implements IStateHeuristic {
    @Override
    public double evaluateState(AbstractGameState gs, int playerId) {
        if (gs.getPlayerResults()[playerId] == CoreConstants.GameResult.WIN_GAME) {
            return maxValue();
        }
        if (gs.getPlayerResults()[playerId] == CoreConstants.GameResult.LOSE_GAME) {
            return minValue();
        }
        MMGameState mmgs = (MMGameState) gs;
        ArrayList<Vector2D> pieces = getPieces(mmgs, playerId);
        Vector2D maxVec = new Vector2D(mmgs.getBoard().getHeight(), mmgs.getBoard().getWidth());
        int upperBoundMax = maxVec.magnitude();
        float avgDist = 0;
        int count = 0;
        for (int i = 0; i < pieces.size(); i++) {
            for (int j = i + 1; j < pieces.size(); j++) {
                int dist = (pieces.get(i).subtract(pieces.get(j))).magnitude();
                avgDist += dist;
                count++;
            }
        }
        avgDist = avgDist / count;
        return -avgDist/upperBoundMax;
    }
}
