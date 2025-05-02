package games.marblesmultiverse;

import core.AbstractGameState;
import core.CoreConstants;
import core.StandardForwardModel;
import core.actions.AbstractAction;
import core.components.Counter;
import core.components.GridBoard;
import evaluation.metrics.Event;
import games.marblesmultiverse.components.*;

import java.util.*;

/**
 * <p>The forward model contains all the game rules and logic. It is mainly responsible for declaring rules for:</p>
 * <ol>
 *     <li>Game setup</li>
 *     <li>Actions available to players in a given game state</li>
 *     <li>Game events or rules applied after a player's action</li>
 *     <li>Game end</li>
 * </ol>
 */
public class MMForwardModel extends StandardForwardModel {

    /**
     * Initializes all variables in the given game state. Performs initial game setup according to game rules, e.g.:
     * <ul>
     *     <li>Sets up decks of cards and shuffles them</li>
     *     <li>Gives player cards</li>
     *     <li>Places tokens on boards</li>
     *     <li>...</li>
     * </ul>
     *
     * @param firstState - the state to be modified to the initial game state.
     */
    @Override
    protected void _setup(AbstractGameState firstState) {
        MMGameState state = (MMGameState) firstState;
        MMParameters params = (MMParameters) state.getGameParameters();

        // Create deck of all cards and shuffle
        List<Card> deck = new ArrayList<>();
//        Collections.addAll(deck, Card.values());
        //noinspection CollectionAddAllCanBeReplacedWithConstructor
        deck.addAll(Card.implementedCards);
        Collections.shuffle(deck, state.getRnd());

        // Select ruleset
        Map<MMTypes.CardType, Card> initialSetup = new HashMap<>();
        if (params.useInitialSetup) {
            // Using initial setup only.
            initialSetup.putAll(params.initialSetup);
            deck.removeAll(initialSetup.values());

        } else if (params.mutationFromInitialSetup) {
            // Using initial setup with some mutations
            initialSetup.putAll(params.initialSetup);
            // Find the card types that can change (because they have >1 implemented)
            List<MMTypes.CardType> possibleMutations = new ArrayList<>();
            for (MMTypes.CardType ct: MMTypes.CardType.values()) {
                if (Card.nImplemented(ct) > 1) possibleMutations.add(ct);
            }
            // Apply mutations
            int nMutations = Math.min(params.nMutations, possibleMutations.size());  // Cap number of mutations applied to max. available
            for (int i = 0; i < nMutations; i++) {
                // Random type of card to mutate from those possible
                int rndIdx = state.getRnd().nextInt(possibleMutations.size());
                MMTypes.CardType ct = possibleMutations.get(rndIdx);
                possibleMutations.remove(ct);
                // Random replacement for that type
                List<Card> possibleCardReplacements = new ArrayList<>();
                for (Card c: Card.values()) {
                    if (c.type == ct && c != initialSetup.get(ct) && Card.isImplemented(c)) possibleCardReplacements.add(c);
                }
                // Replace in rule setup
                initialSetup.put(ct, possibleCardReplacements.get(state.getRnd().nextInt(possibleCardReplacements.size())));
            }
            // Remove all cards that are part of our setup
            deck.removeAll(initialSetup.values());

        } else {
            // Completely random card for each type of rule
            int ct = 0;
            while (initialSetup.size() < MMTypes.CardType.values().length && ct < deck.size()) {
                Card card = deck.get(ct);
                if (initialSetup.containsKey(card.type)) {
                    ct++;
                    continue;
                }
                initialSetup.put(card.type, card);
                deck.remove(ct);
            }
            if (initialSetup.size() < MMTypes.CardType.values().length) {
                throw new AssertionError("Not enough cards to setup the game");
            }
        }

        state.rulesInPlay = initialSetup;
        state.deckOfRules = deck;
        state.board = new GridBoard(params.gridSize, params.gridSize);
        int nMarblesPerPlayer = state.rulesInPlay.get(MMTypes.CardType.Setup).parseSetup(state.board);

        state.playerMarblesOnBoard.clear();
        state.playerMarblesPushedOut.clear();
        state.playerMarblesRemoved.clear();
        for (int i = 0; i < state.getNPlayers(); i++) {
            state.playerMarblesOnBoard.add(new Counter(nMarblesPerPlayer, 0, nMarblesPerPlayer, "Marbles on board p" + i));
            state.playerMarblesPushedOut.add(new ArrayList<>());
            state.playerMarblesRemoved.add(new ArrayList<>());
        }

        state.setFirstPlayer(0);
    }

    /**
     * Calculates the list of currently available actions, possibly depending on the game phase.
     *
     * @return - List of AbstractAction objects.
     */
    @Override
    protected List<AbstractAction> _computeAvailableActions(AbstractGameState gameState) {
        MMGameState gs = (MMGameState) gameState;
        Map<MMTypes.CardType, Card> rules = gs.getRulesInPlay();
        int currentPlayer = gs.getCurrentPlayer();
        List<AbstractAction> actions = new ArrayList<>(rules.get(MMTypes.CardType.Movement).generateMoveActions(gs, currentPlayer));
        actions.addAll(rules.get(MMTypes.CardType.Push).generatePushActions(gs, currentPlayer));
        return actions;
    }

    @Override
    protected void _afterAction(AbstractGameState currentState, AbstractAction actionTaken) {
        if (currentState.isActionInProgress()) return;
        MMGameState gameState = (MMGameState) currentState;

        // Check default game end: 1 marble left for a player
        int loser = -1;
        for (int i = 0; i < currentState.getNPlayers(); i++) {
            if (gameState.getPlayerMarblesOnBoard().get(i).getValue() < 2) {
                loser = i;
                break;
            }
        }
        if (loser != -1) {
            currentState.setGameStatus(CoreConstants.GameResult.GAME_END);
            for (int i = 0; i < currentState.getNPlayers(); i++) {
                if (i == loser) currentState.setPlayerResult(CoreConstants.GameResult.LOSE_GAME, i);
                else currentState.setPlayerResult(CoreConstants.GameResult.WIN_GAME, i);
            }
            currentState.logEvent(Event.GameEvent.GAME_EVENT, "Game over: 1 marble left for p" + loser);
            return;
        }

        // Check victory rules active
        Card victoryCard = ((MMGameState) currentState).rulesInPlay.get(MMTypes.CardType.Victory);
        int winner = victoryCard.checkVictory((MMGameState) currentState);
        if (winner != -1) {
            currentState.setGameStatus(CoreConstants.GameResult.GAME_END);
            for (int i = 0; i < currentState.getNPlayers(); i++) {
                if (i == winner) currentState.setPlayerResult(CoreConstants.GameResult.WIN_GAME, i);
                else currentState.setPlayerResult(CoreConstants.GameResult.LOSE_GAME, i);
            }
            currentState.logEvent(Event.GameEvent.GAME_EVENT, "Game over: victory condition " + victoryCard.name() + " triggered by p" + winner);
            return;
        }

        endPlayerTurn(currentState);

        // Cap on length of games
        MMParameters params = (MMParameters) currentState.getGameParameters();
        if (params.maxTurns != -1 && currentState.getTurnCounter() >= params.maxTurns) {
            // Check max rounds counter
            currentState.setGameStatus(CoreConstants.GameResult.GAME_END);
            for (int i = 0; i < currentState.getNPlayers(); i++) {
                currentState.setPlayerResult(CoreConstants.GameResult.TIMEOUT, i);
            }
            currentState.logEvent(Event.GameEvent.GAME_EVENT, "Game over: max turns exceeded, all players timeout.");
            return;
        }

        // Check end game if next player has no actions
        List<AbstractAction> actions = computeAvailableActions(gameState);
        if(actions.isEmpty()){
            gameState.setGameStatus(CoreConstants.GameResult.GAME_END);
            for (int i = 0; i < gameState.getNPlayers(); i++) {
                if (i == gameState.getCurrentPlayer()) gameState.setPlayerResult(CoreConstants.GameResult.LOSE_GAME, i);
                else gameState.setPlayerResult(CoreConstants.GameResult.WIN_GAME, i);
            }
            currentState.logEvent(Event.GameEvent.GAME_EVENT, "Game over: no moves remaining for p" + gameState.getCurrentPlayer());
        }
    }

    @Override
    protected void endGame(AbstractGameState gs) {
        if (gs.getCoreGameParameters().verbose) {
            System.out.println(Arrays.toString(gs.getPlayerResults()));
        }
    }
}


