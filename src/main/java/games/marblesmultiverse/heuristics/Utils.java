package games.marblesmultiverse.heuristics;
import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.components.BoardSpot;
import games.marblesmultiverse.components.MMTypes;
import utilities.Vector2D;

import java.util.ArrayList;


public class Utils {
    public static ArrayList<Vector2D> getPieces(MMGameState mmgs, int playerId) {
        ArrayList<Vector2D> pieces = new ArrayList<>();
        for (int i = 0; i < mmgs.getBoard().getHeight(); i++) {
            for (int j = 0; j < mmgs.getBoard().getWidth(); j++) {
                BoardSpot boardSpot = mmgs.getBoard().getElement(j, i);
                if (boardSpot != null && boardSpot.getOccupant() != null && boardSpot.getOccupant().ordinal() == playerId) {
                    pieces.add(new Vector2D(i, j));
                }
            }
        }
        return pieces;
    }

    public static ArrayList<BoardSpot> getVictorySpots(MMGameState mmgs, int playerId) {
        ArrayList<BoardSpot> victorySpots = new ArrayList<>();
        for (int i = 0; i < mmgs.getBoard().getHeight(); i++) {
            for (int j = 0; j < mmgs.getBoard().getWidth(); j++) {
                BoardSpot boardSpot = mmgs.getBoard().getElement(j, i);
                if (boardSpot != null && boardSpot.getVictoryOwner() != null
                        && boardSpot.getSpotType() == MMTypes.SpotType.VICTORY
                        && boardSpot.getVictoryOwner().ordinal() == playerId) {
                    victorySpots.add(boardSpot);
                }
            }
        }
        return victorySpots;
    }
}
