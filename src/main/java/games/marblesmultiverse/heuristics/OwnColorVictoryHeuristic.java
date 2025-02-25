package games.marblesmultiverse.heuristics;

import core.AbstractGameState;
import core.CoreConstants;
import core.interfaces.IStateHeuristic;

public class OwnColorVictoryHeuristic implements IStateHeuristic {
    VictoryDistanceHeuristic victoryDistance;
    PushHeuristic pushHeuristic;

    OwnVictorySpotHeuristic victorySpotHeuristic;

    // TODO Learnable/Customizable weights
    public OwnColorVictoryHeuristic() {
        victoryDistance = new VictoryDistanceHeuristic();
        pushHeuristic = new PushHeuristic();
        victorySpotHeuristic = new OwnVictorySpotHeuristic();
    }

    @Override
    public double evaluateState(AbstractGameState gs, int playerId) {
        if (gs.getPlayerResults()[playerId] == CoreConstants.GameResult.WIN_GAME) {
            return maxValue();
        }
        if (gs.getPlayerResults()[playerId] == CoreConstants.GameResult.LOSE_GAME) {
            return minValue();
        }
        double victoryDistScore = victoryDistance.evaluateState(gs, playerId) / victoryDistance.maxValue();
        double pushScore = pushHeuristic.evaluateState(gs, playerId) / pushHeuristic.maxValue();
        double ownVictoryScore = victorySpotHeuristic.evaluateState(gs, playerId) / victorySpotHeuristic.maxValue();
        return 0.6*ownVictoryScore + 0.3*victoryDistScore + 0.1*pushScore;
    }
}
