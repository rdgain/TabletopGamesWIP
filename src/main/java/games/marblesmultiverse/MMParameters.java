package games.marblesmultiverse;

import core.AbstractGameState;
import core.AbstractParameters;
import evaluation.optimisation.TunableParameters;
import utilities.Vector2D;

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

    public int gridSize = 9;
    public Vector2D gridCenter = new Vector2D(4,4);  // depending on gridsize

    @Override
    protected AbstractParameters _copy() {
        // TODO: deep copy of all variables.
        return this;
    }

    @Override
    public boolean _equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MMParameters that = (MMParameters) o;
        return gridSize == that.gridSize && Objects.equals(gridCenter, that.gridCenter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gridSize, gridCenter);
    }
}
