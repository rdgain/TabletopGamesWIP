package games.marblesmultiverse.heuristics;

import core.AbstractGameState;
import core.CoreConstants;
import core.interfaces.IStateHeuristic;
import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.components.BoardSpot;
import utilities.Vector2D;

import java.util.ArrayList;

import static games.marblesmultiverse.heuristics.Utils.getPieces;
import static games.marblesmultiverse.heuristics.Utils.getVictorySpots;

public class VictoryDistanceHeuristic implements IStateHeuristic {
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
        ArrayList<BoardSpot> victorySpots = getVictorySpots(mmgs, playerId);
        Vector2D maxVec = new Vector2D(mmgs.getBoard().getHeight(), mmgs.getBoard().getWidth());
        float upperBoundMax = maxVec.magnitude();
        float avgDist = 0;
        for (Vector2D p: pieces) {
            int dist = Integer.MAX_VALUE;
            for (BoardSpot spot: victorySpots) {
                Vector2D spotVec = new Vector2D(spot.x, spot.y);
                dist = Math.min((p.subtract(spotVec)).magnitude(), dist);
            }
            avgDist += dist;
        }
        avgDist = avgDist / pieces.size();
        return -avgDist/upperBoundMax;
    }
}
