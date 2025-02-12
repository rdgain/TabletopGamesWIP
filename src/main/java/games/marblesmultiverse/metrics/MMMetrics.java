package games.marblesmultiverse.metrics;

import core.interfaces.IGameEvent;
import evaluation.listeners.MetricsGameListener;
import evaluation.metrics.AbstractMetric;
import evaluation.metrics.Event;
import evaluation.metrics.IMetricsCollection;
import games.marblesmultiverse.MMGameState;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MMMetrics implements IMetricsCollection {
    public static class MarbelsPerPlayer extends AbstractMetric {

        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            MMGameState state= (MMGameState) e.state;
            for (int i=0; i< state.getNPlayers() ;i++){
                records.put("Player"+ i+ "Remaining Marbles",state.getPlayerMarblesOnBoard().get(i));
            }
            return true;
        }

        @Override
        public Set<IGameEvent> getDefaultEventTypes() {
            return Collections.singleton(Event.GameEvent.GAME_OVER);
        }

        @Override
        public Map<String, Class<?>> getColumns(int nPlayersPerGame, Set<String> playerNames) {
            Map<String, Class<?>> columns = new HashMap<>();
            for (int i=0; i< nPlayersPerGame ;i++){
                columns.put("Player"+ i+ "Remaining Marbles", Integer.class);
            }
            return columns;
        }
    }

    public static class MarbelsPushedOut extends AbstractMetric {

        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            MMGameState state= (MMGameState) e.state;
            for (int i=0; i< state.getNPlayers() ;i++){
                records.put("Player"+ i+ "Pushed Marbles",state.getPlayerMarblesPushedOut().get(i).size());
            }
            return true;
        }

        @Override
        public Set<IGameEvent> getDefaultEventTypes() {
            return Collections.singleton(Event.GameEvent.GAME_OVER);
        }

        @Override
        public Map<String, Class<?>> getColumns(int nPlayersPerGame, Set<String> playerNames) {
            Map<String, Class<?>> columns = new HashMap<>();
            for (int i=0; i< nPlayersPerGame ;i++){
                columns.put("Player"+ i+ "Pushed Marbles", Integer.class);
            }
            return columns;
        }
    }

    // TODO: add metric for cards used? or this goes automagically?
}
