package games.marblesmultiverse;

import core.AbstractGameState;
import core.AbstractParameters;
import core.components.*;
import games.GameType;
import games.marblesmultiverse.components.BoardSpot;
import games.marblesmultiverse.components.Card;
import games.marblesmultiverse.components.MMTypes;

import java.util.*;

/**
 * <p>The game state encapsulates all game information. It is a data-only class, with game functionality present
 * in the Forward Model or actions modifying the state of the game.</p>
 * <p>Most variables held here should be {@link Component} subclasses as much as possible.</p>
 * <p>No initialisation or game logic should be included here (not in the constructor either). This is all handled externally.</p>
 * <p>Computation may be included in functions here for ease of access, but only if this is querying the game state information.
 * Functions on the game state should never <b>change</b> the state of the game.</p>
 */
public class MMGameState extends AbstractGameState {

    List<Card> deckOfRules;
    Map<MMTypes.CardType, Card> rulesInPlay = new HashMap<>();
    GridBoard<BoardSpot> board;

    /**
     * @param gameParameters - game parameters.
     * @param nPlayers       - number of players in the game
     */
    public MMGameState(AbstractParameters gameParameters, int nPlayers) {
        super(gameParameters, nPlayers);
    }

    public GridBoard<BoardSpot> getBoard() {
        return board;
    }

    public List<Card> getDeckOfRules() {
        return deckOfRules;
    }

    public Map<MMTypes.CardType, Card> getRulesInPlay() {
        return rulesInPlay;
    }

    /**
     * @return the enum value corresponding to this game, declared in {@link GameType}.
     */
    @Override
    protected GameType _getGameType() {
        return GameType.MultiverseMarbles;
    }

    /**
     * Returns all Components used in the game and referred to by componentId from actions or rules.
     * This method is called after initialising the game state, so all components will be initialised already.
     *
     * @return - List of Components in the game.
     */
    @Override
    protected List<Component> _getAllComponents() {
        return new ArrayList<>() {{
            add(board);
        }};
    }

    /**
     * <p>Create a deep copy of the game state containing only those components the given player can observe.</p>
     * <p>If the playerID is NOT -1 and If any components are not visible to the given player (e.g. cards in the hands
     * of other players or a face-down deck), then these components should instead be randomized (in the previous examples,
     * the cards in other players' hands would be combined with the face-down deck, shuffled together, and then new cards drawn
     * for the other players). This process is also called 'redeterminisation'.</p>
     * <p>There are some utilities to assist with this in utilities.DeterminisationUtilities. One firm is guideline is
     * that the standard random number generator from getRnd() should not be used in this method. A separate Random is provided
     * for this purpose - redeterminisationRnd.
     *  This is to avoid this RNG stream being distorted by the number of player actions taken (where those actions are not themselves inherently random)</p>
     * <p>If the playerID passed is -1, then full observability is assumed and the state should be faithfully deep-copied.</p>
     *
     * <p>Make sure the return type matches the class type, and is not AbstractGameState.</p>
     *
     *
     * @param playerId - player observing this game state.
     */
    @Override
    protected MMGameState _copy(int playerId) {
        MMGameState copy = new MMGameState(gameParameters, getNPlayers());
        copy.deckOfRules = new ArrayList<>(deckOfRules);
        copy.rulesInPlay = new HashMap<>(rulesInPlay);
        copy.board = board.emptyCopy();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                BoardSpot spot = board.getElement(j, i);
                if (spot != null)
                    copy.board.setElement(j, i, spot.copy());
            }
        }

        return copy;
    }

    /**
     * @param playerId - player observing the state.
     * @return a score for the given player approximating how well they are doing (e.g. how close they are to winning
     * the game); a value between 0 and 1 is preferred, where 0 means the game was lost, and 1 means the game was won.
     */
    @Override
    protected double _getHeuristicScore(int playerId) {
        if (isNotTerminal()) {
            // TODO calculate an approximate value
            return getGameScore(playerId);
        } else {
            // The game finished, we can instead return the actual result of the game for the given player.
            return getPlayerResults()[playerId].value;
        }
    }

    /**
     * @param playerId - player observing the state.
     * @return the true score for the player, according to the game rules. May be 0 if there is no score in the game.
     */
    @Override
    public double getGameScore(int playerId) {
        return 0;  // no scoring
    }

    @Override
    public boolean _equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MMGameState that = (MMGameState) o;
        return Objects.equals(deckOfRules, that.deckOfRules) && Objects.equals(rulesInPlay, that.rulesInPlay) && Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), deckOfRules, rulesInPlay, board);
    }
}
