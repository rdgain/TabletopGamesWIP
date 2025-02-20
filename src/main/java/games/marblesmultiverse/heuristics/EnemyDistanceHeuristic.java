package games.marblesmultiverse.heuristics;

import core.AbstractGameState;
import core.CoreConstants;
import core.interfaces.IStateHeuristic;
import games.marblesmultiverse.MMGameState;
import utilities.Vector2D;

import java.util.ArrayList;

import static games.marblesmultiverse.heuristics.Utils.getPieces;

public class EnemyDistanceHeuristic implements IStateHeuristic {
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
        ArrayList<Vector2D> enemyPieces = getPieces(mmgs, (playerId+1)%2);
        Vector2D maxVec = new Vector2D(mmgs.getBoard().getHeight(), mmgs.getBoard().getWidth());
        int upperBoundMax = maxVec.magnitude();
        float avgDist = 0;
        for (Vector2D p: pieces) {
            int dist = Integer.MAX_VALUE;
            for (Vector2D mp : enemyPieces) {
                dist = Math.min(p.subtract(mp).magnitude(), dist);
            }
            avgDist += dist;
        }
        avgDist = avgDist/pieces.size();
        return -avgDist/upperBoundMax;
    }
}
