package players;

import core.AbstractGameState;
import core.AbstractPlayer;
import core.actions.AbstractAction;

import java.util.ArrayList;
import java.util.List;

public class TraceReplay extends AbstractPlayer {
    private final List<Integer> playTraces = new ArrayList<>();

    public TraceReplay(List<Integer> playTraces) {
        super(null, "TraceReplay");
        this.playTraces.addAll(playTraces);
    }

    @Override
    public AbstractAction _getAction(AbstractGameState gameState, List<AbstractAction> possibleActions) {
        int turn = gameState.getTurnCounter();
        return possibleActions.get(playTraces.get(turn));
    }

    @Override
    public TraceReplay copy() {
        return new TraceReplay(playTraces);
    }
}
