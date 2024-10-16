package games.marblesmultiverse.actions;

import core.AbstractGameState;
import core.components.GridBoard;
import games.marblesmultiverse.Constants;
import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.components.BoardSpot;
import games.marblesmultiverse.components.Card;
import games.marblesmultiverse.components.MMTypes;
import spire.random.Const;
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
        List<BoardSpot> spots = new ArrayList<>();
        while (board.isInBounds(current.getX(), current.getY())
                && board.getElement(current) != null
                && nColumns < nColumnsInPush) {
            BoardSpot currentSpot = board.getElement(current);
            if (currentSpot.getOccupant() != player) {
                nColumns ++;
                player = currentSpot.getOccupant();
            }
            // Put the marble from the last spot saved into the current spot
            if (!spots.isEmpty()) {
                BoardSpot boardSpotFrom = spots.get(spots.size() - 1);
                currentSpot.addMarble(boardSpotFrom.getOccupant());
            }
            spots.add(currentSpot);

            Vector2D next = Constants.add_direction(current, direction);
            if (board.isInBounds(next.getX(), next.getY()) && board.getElement(next) != null) {
                current = next;
            } else {
                // push out rule
                pushOutRule.pushOut(state, player);
                break;
            }
        }
        // Remove first marble
        state.getBoard().getElement(from).removeMarble();

        return false;
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
