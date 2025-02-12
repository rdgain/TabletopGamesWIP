package games.marblesmultiverse.metrics;

import core.interfaces.IGameEvent;
import evaluation.listeners.MetricsGameListener;
import evaluation.metrics.AbstractMetric;
import evaluation.metrics.Event;
import evaluation.metrics.IMetricsCollection;
import games.marblesmultiverse.MMGameState;

import java.util.Map;
import java.util.Set;

public class MMMetrics implements IMetricsCollection {
    public static class MarbelsPerPlayer extends AbstractMetric {

        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            MMGameState state= (MMGameState) e.state;
            for (int i=0; i< state.getNPlayers() ;i++){
                records.put("Player"+ i+ "Marbles",state.getPlayerMarblesOnBoard().get(i));
            }
            return false;
        }

        @Override
        public Set<IGameEvent> getDefaultEventTypes() {
            return null;
        }

        @Override
        public Map<String, Class<?>> getColumns(int nPlayersPerGame, Set<String> playerNames) {
            return null;
        }
    }
}
