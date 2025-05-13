package games.marblesmultiverse.metrics;

import core.interfaces.IGameEvent;
import evaluation.listeners.MetricsGameListener;
import evaluation.metrics.AbstractMetric;
import evaluation.metrics.Event;
import evaluation.metrics.IMetricsCollection;
import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.components.Card;
import games.marblesmultiverse.components.MMTypes;


import java.util.*;

import static games.marblesmultiverse.components.MMTypes.CardType.*;

public class MMMetrics implements IMetricsCollection {

        public static class RuleCardsUsed extends AbstractMetric {

            @Override
            protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
                MMGameState gs = (MMGameState) e.state;
                Map<MMTypes.CardType, Card> rules = gs.getRulesInPlay();
                for (Map.Entry<MMTypes.CardType, Card> r : rules.entrySet()) {
                    //records.put("Rule-" + r.getKey().name(), r.getValue().description); //using rule description
                    records.put("Rule-" + r.getKey().name(), r.getValue().name()); // using the rule name
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
                MMTypes.CardType[] ruleTypes = {Setup, Victory, Movement, Push, PushRequirement, PushOut};
                for (int i = 0; i < 6; i++) {
                    columns.put("Rule-" + ruleTypes[i].name(), String.class);
//                    System.out.println("Rule-" + ruleTypes[i].name());
                }
                return columns;
            }
        }
    public static class MarbelsPerPlayer extends AbstractMetric {

        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            MMGameState state= (MMGameState) e.state;
            for (int i=0; i< state.getNPlayers() ;i++){
                records.put("Player"+ i+ "Remaining Marbles",state.getPlayerMarblesOnBoard().get(i).getValue());
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
//
    public static class MarbelsRemoved extends AbstractMetric {

        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            MMGameState state= (MMGameState) e.state;
            for (int i=0; i< state.getNPlayers() ;i++){
                records.put("Player"+ i+ "removed marbles",state.getPlayerMarblesRemoved().get(i).toString());
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
                columns.put("Player"+ i+ "removed marbles", String.class);
            }
            return columns;
        }
    }

    public static class WhoPushedOut extends AbstractMetric {

        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            MMGameState state= (MMGameState) e.state;
            for (int i=0; i< state.getNPlayers() ;i++){
                records.put("Player"+ i+ "Pushed Marbles",state.getPlayerMarblesPushedOut().get(i).toString());
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
                columns.put("Player"+ i+ "Pushed Marbles", String.class);
            }
            return columns;
        }
    }

    public static class ActionTrace extends AbstractMetric {

        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
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


