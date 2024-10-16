package games.marblesmultiverse.actions;

import core.AbstractGameState;
import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.components.BoardSpot;
import utilities.Vector2D;

import java.util.Objects;

public class Move extends DirectionalAction {

    public Move(int playerID, Vector2D from, Vector2D to) {
        super(playerID, from, to);
    }

    /**
     * Executes this action, applying its effect to the given game state. Can access any component IDs stored
     * through the {@link AbstractGameState#getComponentById(int)} method.
     * @param gs - game state which should be modified by this action.
     * @return - true if successfully executed, false otherwise.
     */
    @Override
    public boolean execute(AbstractGameState gs) {
        MMGameState state = (MMGameState) gs;
        BoardSpot boardSpotFrom = state.getBoard().getElement(from);
        BoardSpot boardSpotTo = state.getBoard().getElement(to);
        boardSpotTo.addMarble(boardSpotFrom.getOccupant());
        boardSpotFrom.removeMarble();
        return true;
    }

    /**
     * @return Make sure to return an exact <b>deep</b> copy of the object, including all of its variables.
     * Make sure the return type is this class (e.g. GTAction) and NOT the super class AbstractAction.
     * <p>If all variables in this class are final or effectively final (which they should be),
     * then you can just return <code>`this`</code>.</p>
     */
    @Override
    public Move copy() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return playerID == move.playerID && Objects.equals(from, move.from) && Objects.equals(to, move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, from, to);
    }

    @Override
    public String toString() {
        return "p" + playerID + " move from " + from + " to " + to;
    }

    /**
     * @param gameState - game state provided for context.
     * @return A more descriptive alternative to the toString action, after access to the game state to e.g.
     * retrieve components for which only the ID is stored on the action object, and include the name of those components.
     * Optional.
     */
    @Override
    public String getString(AbstractGameState gameState) {
        return toString();
    }

}
