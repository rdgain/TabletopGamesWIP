package games.marblesmultiverse;

import core.AbstractGameState;
import core.StandardForwardModel;
import core.actions.AbstractAction;
import core.components.GridBoard;
import games.catan.CatanParameters;
import games.marblesmultiverse.components.*;
import gametemplate.MMParameters;
import gametemplate.actions.GTAction;
import tech.tablesaw.plotly.components.Grid;
import utilities.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static games.terraformingmars.TMTypes.neighbor_directions;

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
        state.board = SetupGameBoard(null);

    }

    public static Vector2D[][] neighbor_directions = new Vector2D[][] {{new Vector2D(1, 0), new Vector2D(0, -1),
            new Vector2D(-1, -1), new Vector2D(-1, 0),
            new Vector2D(-1, 1), new Vector2D(0, 1)},
            {new Vector2D(1, 0), new Vector2D(1, -1),
                    new Vector2D(0, -1), new Vector2D(-1, 0),
                    new Vector2D(0, 1), new Vector2D(1, 1)}};
    public static int grid_distance( Vector2D a, Vector2D b ){
        int x0 = (int) (a.getX()-Math.floor((double) b.getX() /2));
        int y0 = b.getX();
        int x1 = (int) (a.getY()-Math.floor((double) b.getY() /2));
        int y1 = b.getY();
        int dx = x1 - x0;
        int dy = y1 - y0;
        int dist = Math.max(Math.abs(dx), Math.abs(dy));
        dist = Math.max(dist, Math.abs(dx+dy));
        return dist;
    }


    GridBoard<BoardSpot> SetupGameBoard(Cards setup) {
        //setup empty board
        GridBoard<BoardSpot> board= new GridBoard<BoardSpot>(9,9);

        int centerCoord = (int) Math.floor((double) board.getHeight() /2);

        List<Vector2D> stack = new ArrayList<Vector2D>();
        // for now only do basic setup sets all as normal and defines null spots
        board.setElement(centerCoord,centerCoord,new BoardSpot(centerCoord, centerCoord,BoardSpot.SpotType.NORMAL));
        Vector2D center = new Vector2D(centerCoord,centerCoord);
        int parity = Math.abs(center.getY() % 2);
        for (Vector2D v: neighbor_directions[parity]) {
            stack.add(center.add(v));
        }
        while (!stack.isEmpty()){
            Vector2D lookingAt = stack.remove(0);
            if (grid_distance(lookingAt,center)<5){
                // if within range become normal
                board.setElement(lookingAt.getX(), lookingAt.getY(), new BoardSpot(lookingAt.getX(), lookingAt.getY(),BoardSpot.SpotType.NORMAL));
                // then add neighbours to list
                int newParity = Math.abs(lookingAt.getY() % 2);
                for (Vector2D v: neighbor_directions[newParity]) {
                    stack.add(lookingAt.add(v));
                }
            }
        }

        // run code to specific setup switch, maybe Jsonify at the end

        // Define victory spots

        // get top spots
        // top left is index 0 top right is index 5
        Vector2D topLeft = center.add(neighbor_directions[parity][0].mult(4));
        Vector2D topRight = center.add(neighbor_directions[parity][5].mult(4));
        board.setElement(topLeft.getX(), topLeft.getY(), new BoardSpot(topLeft.getX(),topLeft.getY(), BoardSpot.SpotType.VICTORY, MarbleTypes.BLUE));
        board.setElement(topRight.getX(), topRight.getY(), new BoardSpot(topRight.getX(),topRight.getY(), BoardSpot.SpotType.VICTORY, MarbleTypes.BLUE));


        // get bottom spots
        // bottom left is index 2 bottom right is index 3
        Vector2D bottomLeft = center.add(neighbor_directions[parity][2].mult(4));
        Vector2D bottomRight = center.add(neighbor_directions[parity][3].mult(4));
        board.setElement(bottomLeft.getX(), bottomLeft.getY(), new BoardSpot(bottomLeft.getX(),bottomLeft.getY(), BoardSpot.SpotType.VICTORY, MarbleTypes.GREEN));
        board.setElement(bottomRight.getX(), bottomRight.getY(), new BoardSpot(bottomRight.getX(),bottomRight.getY(), BoardSpot.SpotType.VICTORY, MarbleTypes.GREEN));

        // Define starting marble spaces
        // GREEN
        Vector2D topMiddle = new Vector2D(centerCoord,0);
        board.getElement(topMiddle).addMarble(new Marble(MarbleTypes.GREEN,"marble"));

        Vector2D bottomLeftMiddle = topMiddle.add(neighbor_directions[0][2]);
        board.getElement(bottomLeftMiddle).addMarble(new Marble(MarbleTypes.GREEN,"marble"));

        Vector2D bottomRightMiddle = topMiddle.add(neighbor_directions[0][3]);
        board.getElement(bottomRightMiddle).addMarble(new Marble(MarbleTypes.GREEN,"marble"));

        Vector2D leftBottomLeftMiddle = bottomLeftMiddle.add(neighbor_directions[1][1]);
        board.getElement(leftBottomLeftMiddle).addMarble(new Marble(MarbleTypes.GREEN,"marble"));

        Vector2D rightBottomRightMiddle = bottomRightMiddle.add(neighbor_directions[1][4]);
        board.getElement(rightBottomRightMiddle).addMarble(new Marble(MarbleTypes.GREEN,"marble"));

        Vector2D leftBottomLeftBottomLeftMiddle = bottomLeftMiddle.add(neighbor_directions[1][2]);
        board.getElement(leftBottomLeftBottomLeftMiddle).addMarble(new Marble(MarbleTypes.GREEN,"marble"));

        Vector2D nextStep = leftBottomLeftBottomLeftMiddle.add(neighbor_directions[0][4]);
        for(int i = 0; i>4;i++){
            board.getElement(nextStep).addMarble(new Marble(MarbleTypes.GREEN,"marble"));
            nextStep =nextStep.add(neighbor_directions[0][4]);
        }

        // BLUE
        Vector2D bottomMiddle = new Vector2D(centerCoord, board.getHeight()-1);
        board.getElement(bottomMiddle).addMarble(new Marble(MarbleTypes.BLUE,"marble"));

        Vector2D topLeftMiddle = topMiddle.add(neighbor_directions[0][0]);
        board.getElement(topLeftMiddle).addMarble(new Marble(MarbleTypes.BLUE,"marble"));

        Vector2D topRightMiddle = topMiddle.add(neighbor_directions[0][5]);
        board.getElement(topRightMiddle).addMarble(new Marble(MarbleTypes.BLUE,"marble"));

        Vector2D leftTopLeftMiddle = bottomLeftMiddle.add(neighbor_directions[1][1]);
        board.getElement(leftTopLeftMiddle).addMarble(new Marble(MarbleTypes.BLUE,"marble"));

        Vector2D rightTopRightMiddle = bottomRightMiddle.add(neighbor_directions[1][4]);
        board.getElement(rightTopRightMiddle).addMarble(new Marble(MarbleTypes.BLUE,"marble"));

        Vector2D leftTopLeftTopLeftMiddle = bottomLeftMiddle.add(neighbor_directions[1][0]);
        board.getElement(leftTopLeftTopLeftMiddle).addMarble(new Marble(MarbleTypes.GREEN,"marble"));

        nextStep = leftBottomLeftBottomLeftMiddle.add(neighbor_directions[0][4]);
        for(int i = 0; i>4;i++){
            board.getElement(nextStep).addMarble(new Marble(MarbleTypes.GREEN,"marble"));
            nextStep =nextStep.add(neighbor_directions[0][4]);
        }
        return board;
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
