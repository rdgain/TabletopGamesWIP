package games.marblesmultiverse.heuristics;

import core.AbstractGameState;
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
        double pushScore = pushHeuristic.evaluateState(gs, playerId) / pushHeuristic.maxValue();
        double ownDistScore = ownDistance.evaluateState(gs, playerId) / ownDistance.maxValue();
        double enemyDistScore = enemyDistance.evaluateState(gs, playerId) / enemyDistance.maxValue();
        return 0.8*pushScore + 0.1*ownDistScore + 0.1*enemyDistScore;
    }
}
