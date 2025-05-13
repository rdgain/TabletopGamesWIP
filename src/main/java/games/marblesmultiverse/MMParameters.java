package games.marblesmultiverse;

import core.AbstractGameState;
import core.AbstractParameters;
import evaluation.optimisation.TunableParameters;
import games.marblesmultiverse.components.Card;
import games.marblesmultiverse.components.MMTypes;
import utilities.Vector2D;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>This class should hold a series of variables representing game parameters (e.g. number of cards dealt to players,
 * maximum number of rounds in the game etc.). These parameters should be used everywhere in the code instead of
 * local variables or hard-coded numbers, by accessing these parameters from the game state via {@link AbstractGameState#getGameParameters()}.</p>
 *
 * <p>It should then implement appropriate {@link #_copy()}, {@link #_equals(Object)} and {@link #hashCode()} functions.</p>
 *
 * <p>The class can optionally extend from {@link TunableParameters} instead, which allows to use
 * automatic game parameter optimisation tools in the framework.</p>
 */
public class MMParameters extends AbstractParameters {

    public boolean saveTraceEnabled = true;
    public String savePath = "MMPlayTraceTest/";
    public int maxTurns = 200;
    public int gridSize = 9;
    public Vector2D gridCenter = new Vector2D(4,4);  // depending on gridsize

    public Map<MMTypes.CardType, Card> initialSetup = new HashMap<>() {{
        put(MMTypes.CardType.Setup, Card.TWO_SIDES);
        put(MMTypes.CardType.Victory, Card.YOUR_COLOR);
        put(MMTypes.CardType.Movement, Card.MOVE_1);
        put(MMTypes.CardType.Push, Card.PUSH_1);
        put(MMTypes.CardType.PushRequirement, Card.MORE);
        put(MMTypes.CardType.PushOut, Card.OUT_IS_GONE);
    }};
    public boolean mutationFromInitialSetup = false;  // If true, then the game setup will instead use the initial setup with {nMutations} of the cards changes
    public int nMutations = 1;  // Number between 0-N rules (capped to number of possible rules that can change because they have >1 cards implemented)
    public boolean useInitialSetup = false;  // If true, initial setup is used exactly, all other params for setup ignored

    @Override
    protected MMParameters _copy() {
        return this;
    }

    @Override
    public boolean _equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MMParameters that = (MMParameters) o;
        return maxTurns == that.maxTurns && gridSize == that.gridSize && mutationFromInitialSetup == that.mutationFromInitialSetup && nMutations == that.nMutations && useInitialSetup == that.useInitialSetup && Objects.equals(gridCenter, that.gridCenter) && Objects.equals(initialSetup, that.initialSetup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), maxTurns, gridSize, gridCenter, initialSetup, mutationFromInitialSetup, nMutations, useInitialSetup);
    }
}
