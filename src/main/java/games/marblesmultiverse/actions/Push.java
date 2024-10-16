package games.marblesmultiverse.actions;

import core.AbstractGameState;
import core.actions.AbstractAction;
import core.components.GridBoard;
import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.components.BoardSpot;
import games.marblesmultiverse.components.Card;
import games.marblesmultiverse.components.MMTypes;
import utilities.Vector2D;

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

        Vector2D direction = to.subtract(from);
        Vector2D current = from;
        int nColumns = 0;
        MMTypes.MarbleType player = MMTypes.MarbleType.player(playerID);
        while (board.isInBounds(current.getX(), current.getY())
                && board.getElement(current) != null
                && nColumns < nColumnsInPush) {
            if (board.getElement(current).getOccupant() != player) {
                nColumns ++;
                player = board.getElement(current).getOccupant();
            }
            Vector2D next = current.add(direction);
            BoardSpot boardSpotFrom = state.getBoard().getElement(current);
            if (board.isInBounds(next.getX(), next.getY()) && board.getElement(next) != null) {
                BoardSpot boardSpotTo = state.getBoard().getElement(next);
                boardSpotTo.addMarble(boardSpotFrom.getOccupant());
                current = next;
            } else {
                // push out rule
                pushOutRule.pushOut(state, player);
                break;
            }
        }
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
