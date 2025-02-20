package games.marblesmultiverse.heuristics;

import core.AbstractGameState;
import core.CoreConstants;
import core.interfaces.IStateHeuristic;
import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.components.BoardSpot;
import games.marblesmultiverse.components.MMTypes;

public class OwnVictorySpotHeuristic implements IStateHeuristic {
    @Override
    public double evaluateState(AbstractGameState gs, int playerId) {
        if (gs.getPlayerResults()[playerId] == CoreConstants.GameResult.WIN_GAME) {
            return maxValue();
        }
        if (gs.getPlayerResults()[playerId] == CoreConstants.GameResult.LOSE_GAME) {
            return minValue();
        }
        MMGameState mmgs = (MMGameState) gs;
        int totalOwnVictorySpot = 0;
        int occupiedOwnVictorySpot = 0;
        for (int i = 0; i < mmgs.getBoard().getHeight(); i++) {
            for (int j = 0; j < mmgs.getBoard().getWidth(); j++) {
                BoardSpot boardSpot = mmgs.getBoard().getElement(j, i);
                if (boardSpot != null && boardSpot.getVictoryOwner() != null
                        && boardSpot.getSpotType() == MMTypes.SpotType.VICTORY
                        && boardSpot.getVictoryOwner().ordinal() == playerId) {
                    totalOwnVictorySpot++;
                    if (boardSpot.getOccupant() != null && boardSpot.getOccupant().ordinal() == playerId) {
                        occupiedOwnVictorySpot++;
                    }
                }
            }
        }
        return (double)occupiedOwnVictorySpot/totalOwnVictorySpot;
    }
}
