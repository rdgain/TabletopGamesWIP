package games.marblesmultiverse.heuristics;

import core.AbstractGameState;
import core.CoreConstants;
import core.interfaces.IStateHeuristic;

public class PushVictoryHeuristic implements IStateHeuristic {

    OwnDistanceHeuristic ownDistance;
    EnemyDistanceHeuristic enemyDistance;
    PushHeuristic pushHeuristic;
    //TODO customizable weights, extends GLM?
    public PushVictoryHeuristic() {
        ownDistance = new OwnDistanceHeuristic();
        enemyDistance = new EnemyDistanceHeuristic();
        pushHeuristic = new PushHeuristic();
    }
    @Override
    public double evaluateState(AbstractGameState gs, int playerId) {
        if (gs.getPlayerResults()[playerId] == CoreConstants.GameResult.WIN_GAME) {
            return maxValue();
        }
        if (gs.getPlayerResults()[playerId] == CoreConstants.GameResult.LOSE_GAME) {
            return minValue();
        }
        double pushScore = pushHeuristic.evaluateState(gs, playerId) / 3;
        double ownDistScore = ownDistance.evaluateState(gs, playerId) / ownDistance.maxValue();
        double enemyDistScore = enemyDistance.evaluateState(gs, playerId) / enemyDistance.maxValue();
        return 0.7*pushScore + 0.1*ownDistScore + 0.2*enemyDistScore;
    }
}
