package games.marblesmultiverse;

import core.AbstractGameState;
import core.StandardForwardModel;
import core.actions.AbstractAction;
import games.catan.CatanParameters;
import games.marblesmultiverse.components.BoardSpot;
import games.marblesmultiverse.components.CardType;
import games.marblesmultiverse.components.Cards;
import games.marblesmultiverse.components.MMCard;
import gametemplate.MMParameters;
import gametemplate.actions.GTAction;

import java.util.ArrayList;
import java.util.List;

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
        // create deck of all cards
        Cards possibleCards[] = Cards.values();
        ArrayList<Cards> deck = new ArrayList<Cards>();
        for(Cards card : possibleCards){
            deck.add(card);
        }
        // select first rules
        ArrayList<Cards> initialSetup = new ArrayList<Cards>();
        initialSetup.add(Cards.YOUR_COLOR);
        initialSetup.add(Cards.MOVE_1);
        initialSetup.add(Cards.PUSH_1);
        initialSetup.add(Cards.MORE);
        initialSetup.add(Cards.OUT_IS_GONE);

        for(Cards card : initialSetup){
            deck.remove(card);
        }

        state.cardsInPlay = initialSetup;
        state.deckOfRules = deck;

        // create game board
        state.board = SetupGameBoard();






    }

    BoardSpot[][] SetupGameBoard() {
        //setup empty board
        BoardSpot[][] emptyBoard = new BoardSpot[9][9];
        int midX = emptyBoard.length / 2;
        int midY = emptyBoard[0].length / 2;


    }
    /**
     * Calculates the list of currently available actions, possibly depending on the game phase.
     * @return - List of AbstractAction objects.
     */
    @Override
    protected List<AbstractAction> _computeAvailableActions(AbstractGameState gameState) {
        List<AbstractAction> actions = new ArrayList<>();
        // TODO: create action classes for the current player in the given game state and add them to the list. Below just an example that does nothing, remove.
        actions.add(new GTAction());
        return actions;
    }
}
