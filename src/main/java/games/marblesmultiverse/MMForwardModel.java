package games.marblesmultiverse;

import core.AbstractGameState;
import core.CoreConstants;
import core.StandardForwardModel;
import core.actions.AbstractAction;
import core.components.Counter;
import core.components.GridBoard;
import games.marblesmultiverse.components.*;
import utilities.Vector2D;

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
        // create deck of all cards
        List<Card> deck = new ArrayList<>();
        Collections.addAll(deck, Card.values());

        // select first rules
        Map<MMTypes.CardType, Card> initialSetup = new HashMap<>();
        initialSetup.put(MMTypes.CardType.Setup, Card.TWO_SIDES);
        initialSetup.put(MMTypes.CardType.Victory, Card.YOUR_COLOR);
        initialSetup.put(MMTypes.CardType.Movement, Card.MOVE_1);
        initialSetup.put(MMTypes.CardType.Push, Card.PUSH_1);
        initialSetup.put(MMTypes.CardType.PushRequirement, Card.MORE);
        initialSetup.put(MMTypes.CardType.PushOut, Card.OUT_IS_GONE);

        for (Card card : initialSetup.values()) {
            deck.remove(card);
        }

        state.rulesInPlay = initialSetup;
        state.deckOfRules = deck;
        state.board = new GridBoard<>(params.gridSize, params.gridSize);
        int nMarblesPerPlayer = state.rulesInPlay.get(MMTypes.CardType.Setup).parseSetup(state.board);

        for (int i = 0; i < state.getNPlayers(); i++) {
            state.playerMarblesOnBoard.add(new Counter(nMarblesPerPlayer, 0, nMarblesPerPlayer, "Marbles on board p" + i));
            state.playerMarblesPushedOut.add(new Counter());
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

        // Check default game end: 1 marble left for a player todo

        // Check victory rules active
        int winner = ((MMGameState) currentState).rulesInPlay.get(MMTypes.CardType.Victory).checkVictory((MMGameState) currentState);
        if (winner != -1) {
            currentState.setGameStatus(CoreConstants.GameResult.GAME_END);
            for (int i = 0; i < currentState.getNPlayers(); i++) {
                if (i == winner) currentState.setPlayerResult(CoreConstants.GameResult.WIN_GAME, i);
                else currentState.setPlayerResult(CoreConstants.GameResult.LOSE_GAME, i);
            }
            return;
        }

        endPlayerTurn(currentState);
    }

    @Override
    protected void endGame(AbstractGameState gs) {
        if (gs.getCoreGameParameters().verbose) {
            System.out.println(Arrays.toString(gs.getPlayerResults()));
        }
    }

    GridBoard<BoardSpot> setupGameBoard(GridBoard<BoardSpot> board, MMParameters params) {
        int centerCoord = (int) Math.floor((double) params.gridSize / 2);

        List<Vector2D> stack = new ArrayList<>();
        // for now only do basic setup sets all as normal and defines null spots
        board.setElement(centerCoord, centerCoord, new BoardSpot(centerCoord, centerCoord, MMTypes.SpotType.NORMAL));
        Vector2D center = new Vector2D(centerCoord, centerCoord);
        int parity = Math.abs(center.getY() % 2);
        for (Vector2D v : Constants.neighbor_directions[parity]) {
            stack.add(center.add(v));
        }
        while (!stack.isEmpty()) {
            Vector2D lookingAt = stack.remove(0);
            if (Constants.grid_distance(lookingAt, center) < 5) {
                // if within range become normal
                board.setElement(lookingAt.getX(), lookingAt.getY(), new BoardSpot(lookingAt.getX(), lookingAt.getY(), MMTypes.SpotType.NORMAL));
                // then add neighbours to list
                int newParity = Math.abs(lookingAt.getY() % 2);
                for (Vector2D v : Constants.neighbor_directions[newParity]) {
                    stack.add(lookingAt.add(v));
                }
            }
        }

        // run code to specific setup switch, maybe Jsonify at the end

        // Define victory spots

        // get top spots
        // top left is index 0 top right is index 5
        Vector2D topLeft = center.add(Constants.neighbor_directions[parity][0].mult(centerCoord));
        Vector2D topRight = center.add(Constants.neighbor_directions[parity][5].mult(centerCoord));
        board.setElement(topLeft.getX(), topLeft.getY(), new BoardSpot(topLeft.getX(), topLeft.getY(), MMTypes.SpotType.VICTORY, MMTypes.MarbleType.BLUE));
        board.setElement(topRight.getX(), topRight.getY(), new BoardSpot(topRight.getX(), topRight.getY(), MMTypes.SpotType.VICTORY, MMTypes.MarbleType.BLUE));


        // get bottom spots
        // bottom left is index 2 bottom right is index 3
        Vector2D bottomLeft = center.add(Constants.neighbor_directions[parity][2].mult(centerCoord));
        Vector2D bottomRight = center.add(Constants.neighbor_directions[parity][3].mult(centerCoord));
        board.setElement(bottomLeft.getX(), bottomLeft.getY(), new BoardSpot(bottomLeft.getX(), bottomLeft.getY(), MMTypes.SpotType.VICTORY, MMTypes.MarbleType.GREEN));
        board.setElement(bottomRight.getX(), bottomRight.getY(), new BoardSpot(bottomRight.getX(), bottomRight.getY(), MMTypes.SpotType.VICTORY, MMTypes.MarbleType.GREEN));

        // Define starting marble spaces
        // GREEN
        Vector2D topMiddle = new Vector2D(centerCoord, 0);
        board.getElement(topMiddle).addMarble(MMTypes.MarbleType.GREEN);

        Vector2D bottomLeftMiddle = topMiddle.add(Constants.neighbor_directions[0][2]);
        board.getElement(bottomLeftMiddle).addMarble(MMTypes.MarbleType.GREEN);

        Vector2D bottomRightMiddle = topMiddle.add(Constants.neighbor_directions[0][3]);
        board.getElement(bottomRightMiddle).addMarble(MMTypes.MarbleType.GREEN);

        Vector2D leftBottomLeftMiddle = bottomLeftMiddle.add(Constants.neighbor_directions[1][1]);
        board.getElement(leftBottomLeftMiddle).addMarble(MMTypes.MarbleType.GREEN);

        Vector2D rightBottomRightMiddle = bottomRightMiddle.add(Constants.neighbor_directions[1][4]);
        board.getElement(rightBottomRightMiddle).addMarble(MMTypes.MarbleType.GREEN);

        Vector2D leftBottomLeftBottomLeftMiddle = bottomLeftMiddle.add(Constants.neighbor_directions[1][2]);
        board.getElement(leftBottomLeftBottomLeftMiddle).addMarble(MMTypes.MarbleType.GREEN);

        Vector2D nextStep = leftBottomLeftBottomLeftMiddle.add(Constants.neighbor_directions[0][4]);
        for (int i = 0; i < centerCoord; i++) {
            board.getElement(nextStep).addMarble(MMTypes.MarbleType.GREEN);
            nextStep = nextStep.add(Constants.neighbor_directions[0][4]);
        }

        // BLUE
        Vector2D bottomMiddle = new Vector2D(centerCoord, board.getHeight() - 1);
        board.getElement(bottomMiddle).addMarble(MMTypes.MarbleType.BLUE);

        Vector2D topLeftMiddle = topMiddle.add(Constants.neighbor_directions[0][0]);
        board.getElement(topLeftMiddle).addMarble(MMTypes.MarbleType.BLUE);

        Vector2D topRightMiddle = topMiddle.add(Constants.neighbor_directions[0][5]);
        board.getElement(topRightMiddle).addMarble(MMTypes.MarbleType.BLUE);

        Vector2D leftTopLeftMiddle = bottomLeftMiddle.add(Constants.neighbor_directions[1][1]);
        board.getElement(leftTopLeftMiddle).addMarble(MMTypes.MarbleType.BLUE);

        Vector2D rightTopRightMiddle = bottomRightMiddle.add(Constants.neighbor_directions[1][4]);
        board.getElement(rightTopRightMiddle).addMarble(MMTypes.MarbleType.BLUE);

        Vector2D leftTopLeftTopLeftMiddle = bottomLeftMiddle.add(Constants.neighbor_directions[1][0]);
        board.getElement(leftTopLeftTopLeftMiddle).addMarble(MMTypes.MarbleType.GREEN);

        nextStep = leftBottomLeftBottomLeftMiddle.add(Constants.neighbor_directions[0][4]);
        for (int i = 0; i < centerCoord; i++) {
            board.getElement(nextStep).addMarble(MMTypes.MarbleType.GREEN);
            nextStep = nextStep.add(Constants.neighbor_directions[0][4]);
        }
        return board;
    }
}


