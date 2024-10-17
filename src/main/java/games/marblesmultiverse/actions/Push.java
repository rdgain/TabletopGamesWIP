package games.marblesmultiverse.actions;

import core.AbstractGameState;
import core.components.GridBoard;
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
    final int nColumnsInPush;  // BBBB0 = 1; BBBBCC = 2; BBBCCB = 3 etc.

    public Push(int playerID, Vector2D from, Vector2D vector2D, int nColumnsInPush) {
        super(playerID, from, vector2D);
        this.nColumnsInPush = nColumnsInPush;
    }

    @Override
    public boolean execute(AbstractGameState gs) {
        MMGameState state = (MMGameState) gs;
        Card pushOutRule = state.getRulesInPlay().get(MMTypes.CardType.PushOut);
        GridBoard<BoardSpot> board = state.getBoard();

        // todo hardcoded to PUSH_1

        int direction = Constants.direction(from, to);
        Vector2D current = from;
        int nColumns = 0;
        MMTypes.MarbleType player = MMTypes.MarbleType.player(playerID);
        List<MMTypes.MarbleType> spots = new ArrayList<>();

        // Iterate from 'from' onwards in the direction of 'to' for as many columns as we need.
        while (board.isInBounds(current.getX(), current.getY())
                && board.getElement(current) != null
                && nColumns < nColumnsInPush) {
            BoardSpot currentSpot = board.getElement(current);

            // Check if we changed player (or spot is empty), increase number of columns and update player of current column
            if (currentSpot.getOccupant() != player) {
                nColumns ++;
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
            if (board.isInBounds(next.getX(), next.getY()) && board.getElement(next) != null) {
                current = next;
            } else if (player != null) {
                // push out rule, going off the grid
                pushOutRule.pushOut(state, player, playerID);
                break;
            }
        }
        // Remove first marble which was pushed into others
        state.getBoard().getElement(from).removeMarble();

        return true;
    }

    @Override
    public Push copy() {
        return new Push(playerID, from.copy(), to.copy(), nColumnsInPush);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Push push)) return false;
        return playerID == push.playerID && nColumnsInPush == push.nColumnsInPush && Objects.equals(from, push.from) && Objects.equals(to, push.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, from, to, nColumnsInPush);
    }

    @Override
    public String getString(AbstractGameState gameState) {
        return toString();
    }

    @Override
    public String toString() {
        return "p" + playerID + " push from " + from + " to " + to;
    }
}
