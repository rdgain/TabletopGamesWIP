package games.marblesmultiverse.actions;

import core.actions.AbstractAction;
import utilities.Vector2D;

public abstract class DirectionalAction extends AbstractAction {
    final int playerID;
    final Vector2D from, to;

    protected DirectionalAction(int playerID, Vector2D from, Vector2D vector2D) {
        this.playerID = playerID;
        this.from = from;
        to = vector2D;
    }

    public Vector2D from() {
        return from;
    }

    public Vector2D to() {
        return to;
    }

    public int getPlayerID() {
        return playerID;
    }
}
