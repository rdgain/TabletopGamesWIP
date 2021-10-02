package core.rules.nodetypes;

import core.AbstractGameState;
import core.rules.Node;

/**
 * Tests condition, next node depends on the result of the test:
 *
 * parent
 *  - yes (condition == true)
 *  - no (condition == false
 */
public abstract class ConditionNode extends Node {
    Node childYes;  // Node to execute if the condition test returns true
    Node childNo;  // Node to execute if the condition test returns false
    boolean passed;  // The result of the test, true if condition test returns true, false otherwise

    /**
     * Tests a condition given a game state, returns true if condition passes, false otherwise.
     * @param gs - game state to test condition in.
     * @return - boolean, the result of the condition test.
     */
    protected abstract boolean test(AbstractGameState gs);

    @Override
    public final Node execute(AbstractGameState gs) {
        passed = test(gs);
        return getNext();
    }

    @Override
    public final Node getNext() {
        if (passed) return childYes;
        else return childNo;
    }

    // Getters & Setters
    public final void setYesNo(Node childYes, Node childNo) {
        this.childYes = childYes;
        this.childNo = childNo;
    }
    public final Node[] getYesNo() {
        return new Node[]{childYes, childNo};
    }
}
