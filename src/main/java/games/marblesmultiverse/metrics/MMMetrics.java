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
                    records.put("Rule-" + r.getKey().name(), r.getValue().description);
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
}
