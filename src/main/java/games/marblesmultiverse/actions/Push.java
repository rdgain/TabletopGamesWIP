package games.marblesmultiverse.actions;

import core.AbstractGameState;
import core.components.GridBoard;
import evaluation.metrics.Event;
import games.marblesmultiverse.Constants;
import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.components.BoardSpot;
import games.marblesmultiverse.components.Card;
import games.marblesmultiverse.components.MMTypes;
import utilities.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Push extends DirectionalAction {

    public Push(int playerID, Vector2D from, Vector2D vector2D) {
        super(playerID, from, vector2D);
    }

    @Override
    public boolean execute(AbstractGameState gs) {
        MMGameState state = (MMGameState) gs;
        Card pushOutRule = state.getRulesInPlay().get(MMTypes.CardType.PushOut);
        GridBoard board = state.getBoard();

        // todo hardcoded to PUSH_1

        int direction = Constants.direction(from, to);
        Vector2D current = from.copy();
        MMTypes.MarbleType player = MMTypes.MarbleType.player(playerID);
        List<MMTypes.MarbleType> spots = new ArrayList<>();

        // Debug
//        List<Counter> marblesOnBoardBefore = new ArrayList<>();
//        for (int i = 0; i < state.getNPlayers(); i++) {
//            marblesOnBoardBefore.add(state.getPlayerMarblesOnBoard().get(i).copy());
//        }
        // End debug

        // Iterate from 'from' onwards in the direction of 'to' for as many columns as we need.
        while (board.isInBounds(current.getX(), current.getY())
                && board.getElement(current) != null
                && ((BoardSpot)board.getElement(current)).getOccupant() != null) {
            BoardSpot currentSpot = (BoardSpot) board.getElement(current);

            // Check if we changed player (or spot is empty), increase number of columns and update player of current column
            if (currentSpot.getOccupant() != player) {
                player = currentSpot.getOccupant();
            }

            // Save the current spot
            spots.add(currentSpot.getOccupant());

            // Put the marble from the last spot saved into the current spot
            if (spots.size() > 1) {
                currentSpot.addMarble(spots.get(spots.size() - 2));
            }

            // Move to the next spot
            Vector2D next = Constants.add_direction(current, direction);
            current = next;
            if (!board.isInBounds(next.getX(), next.getY()) || board.getElement(next) == null) {
                if (player != null) {
                    // push out rule, going off the grid
                    pushOutRule.pushOut(state, player, playerID);
                    state.logEvent(Event.GameEvent.GAME_EVENT, "p" + playerID + " push out " + "p" + player);
                }
                break;
            }
        }
        // Add last marble
        if (board.isInBounds(current.getX(), current.getY()) && board.getElement(current) != null) {
            ((BoardSpot)board.getElement(current)).addMarble(spots.get(spots.size()-1));
        }
        // Remove first marble which was pushed into others
        ((BoardSpot)state.getBoard().getElement(from)).removeMarble();


        // Debug verify counters in sync with board state
//        int[] counts = new int[state.getNPlayers()];
//        for (int i = 0; i < state.getBoard().getHeight(); i++) {
//            for (int j = 0; j < state.getBoard().getWidth(); j++) {
//                BoardSpot spot = state.getBoard().getElement(j,i);
//                if (spot != null && spot.getOccupant() != null) counts[spot.getOccupant().ordinal()] ++;
//            }
//        }
//        for (int i = 0; i < state.getNPlayers(); i++) {
//            if (counts[i] != state.getPlayerMarblesOnBoard().get(i).getValue()) {
//                int a = 0;
//            }
//        }
        // End debug

        return true;
    }

    @Override
    public Push copy() {
        return new Push(playerID, from.copy(), to.copy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Push push)) return false;
        return playerID == push.playerID && Objects.equals(from, push.from) && Objects.equals(to, push.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, from, to);
    }

    @Override
    public String getString(AbstractGameState gameState) {
        Card push = ((MMGameState)gameState).getRulesInPlay().get(MMTypes.CardType.Push);
        Card pushReq = ((MMGameState)gameState).getRulesInPlay().get(MMTypes.CardType.PushRequirement);
        Card pushOut = ((MMGameState)gameState).getRulesInPlay().get(MMTypes.CardType.PushOut);
        return "[" + push + ";" + pushReq + ";" + pushOut + "]: " + this;
    }

    @Override
    public String toString() {
        return "p" + playerID + " push from " + from + " to " + to;
    }
}
