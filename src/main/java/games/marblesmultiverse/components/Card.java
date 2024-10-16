package games.marblesmultiverse.components;

import core.actions.AbstractAction;
import core.components.GridBoard;
import games.marblesmultiverse.Constants;
import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.actions.Move;
import games.marblesmultiverse.actions.Push;
import utilities.Vector2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Card {
    YOUR_COLOR("Occupy all goals of your color.", MMTypes.CardType.Victory),
    ONE_OF_EACH("Occupy 1 goal of each color.", MMTypes.CardType.Victory),
    DIAGONAL("Occupy 2 opposing goals and the center (if it exists).", MMTypes.CardType.Victory),
    ANY_THREE("Occupy 3 non-neighbouring goals.", MMTypes.CardType.Victory),
    BORDERLANDS("Put 1 marble on each side of the board. Corners do not count.", MMTypes.CardType.Victory),
    PUSH_OUT("Push out 3 opposing marbles.", MMTypes.CardType.Victory),

    MOVE_1("Move 1 marble by 1 space in any direction.", MMTypes.CardType.Movement),
    MOVE_EXACTLY_2("Move 1 marble exactly 2 spaces in any direction.", MMTypes.CardType.Movement),
    MOVE_2("Move 1 marble by up to 3 spaces in any direction in a straight line.", MMTypes.CardType.Movement),
    SPLIT_MOVE("Move 1 or 2 marbles by 1 space in any direction.", MMTypes.CardType.Movement),
    SIDESTEP("Move a column of marbles orthogonally by 1 space or move 1 marble by 1 space in any direction.", MMTypes.CardType.Movement),
    LEAPFROG("Jump over 1 marble or blocker or move a marble by 1 space in any direction", MMTypes.CardType.Movement),
    CHAIN_FROG("Repeatedly jump over 1 marble or blocker or move a marble by 1 space in any direction", MMTypes.CardType.Movement),

    PUSH_1("Push a column of marbles by 1 space in any direction", MMTypes.CardType.Push),
    PUSH_2("Push a column of marbles by up to 2 spaces in any direction.", MMTypes.CardType.Push),
    SPLIT_PUSH("Push a column of marbles by up to 2 spaces in any direction.", MMTypes.CardType.Push),
    MUST_PUSH_OPPONENT("You must push a column of opposing marbles by 1 space, if you can.", MMTypes.CardType.Push),
    MUST_PUSH_ANY("You must push a column of marbles by 1 space, if you can. Including your own.", MMTypes.CardType.Push),
    MOMENTUM("Push all the way in any direction until your first marble reaches the edge or you cannot push anymore.", MMTypes.CardType.Push),

    MORE("You can push opposing marbles if you use more marbles.", MMTypes.CardType.PushRequirement),
    MORE_OR_EQUAL("You can push opposing marbles if you use more or equal marbles. Do not split your column while pushing the opponent.", MMTypes.CardType.PushRequirement),
    EQUAL("You can push opposing marbles if you use equal marbles. Do not split your column while pushing the opponent.", MMTypes.CardType.PushRequirement),
    FEWER_OR_MORE("You can push opposing marbles if you use fewer or more marbles.", MMTypes.CardType.PushRequirement),
    FEWER_OR_EQUAL("You can push opposing marbles if you use fewer or equal marbles. Do not split your column while pushing the opponent.", MMTypes.CardType.PushRequirement),
    MAJORITY("You can push opposing marbles if you have more marbles anywhere in the line.", MMTypes.CardType.PushRequirement),
    MINORITY("You can push opposing marbles if you have fewer marbles anywhere in the line.", MMTypes.CardType.PushRequirement),
    UNEVEN("You can push your opposing marbles if you use an uneven number of marbles (1, 3 or 5). Do not split your column while pushing the opponent.", MMTypes.CardType.PushRequirement),
    EVEN("You can push opposing marbles if you use an even number of marbles (2, 4 or 6). Do not split your column while pushing the opponent.", MMTypes.CardType.PushRequirement),

    OUT_IS_GONE("Pushed out marbles are removed from play", MMTypes.CardType.PushOut),
    CENTER_IF_FREE("Pushed out marbles return to the center, if it is free. Else they are removed.", MMTypes.CardType.PushOut),
    CENTER_REPLACE("Pushed out marbles return to the center. Remove any pre-existing marble.", MMTypes.CardType.PushOut),
    THE_RETURN("The last pushed out marble returns to a free space of the next opponent's choice.", MMTypes.CardType.PushOut),
    REPLACEMENT("The last pushed out marble replaces an opponent's marble of their choice. You cannot push out your own marbles.", MMTypes.CardType.PushOut),
    WARP_AROUND("Instead of being pushed out, marbles \"wrap around\" to the other end of the line.", MMTypes.CardType.PushOut),
    TELEPORT_IF_FREE("Pushed out marbles appear on the other end of the line, if that space is free. Else they are removed.", MMTypes.CardType.PushOut),
    TELEPORT_REPLACE("Pushed out marbles appear on the other end of the line. Remove any pre-existing marble. You cannot push out your own marbles.", MMTypes.CardType.PushOut),

    TWO_SIDES("TwoSides.txt", MMTypes.CardType.Setup);

    public final String description;
    public final MMTypes.CardType type;

    Card(String description, MMTypes.CardType type){
        this.description =description;
        this.type=type;
    }

    public List<AbstractAction> generateMoveActions(MMGameState gs, int playerID) {
        List<AbstractAction> actions = new ArrayList<>();
        if (this.type != MMTypes.CardType.Movement) return actions;

        switch (this) {
            case MOVE_1:
                // Check all neighbours distance 1
                for (int i = 0; i < gs.getBoard().getHeight(); i++) {
                    for (int j = 0; j < gs.getBoard().getWidth(); j++) {
                        BoardSpot boardSpot = gs.getBoard().getElement(j, i);
                        if (boardSpot != null && boardSpot.occupant == MMTypes.MarbleType.player(playerID)) {
                            Vector2D from = new Vector2D(j, i);
                            for (Vector2D to: Constants.getNeighbours(from)) {
                                // 1 space away
                                BoardSpot spot = gs.getBoard().getElement(to.getX(), to.getY());
                                if (spot != null) {
                                    if (spot.occupant == null) {
                                        // Move is always to an empty spot
                                        actions.add(new Move(playerID, from, to));
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default: return actions;
        }
        return actions;
    }

    public List<AbstractAction> generatePushActions(MMGameState gs, int playerID) {
        List<AbstractAction> actions = new ArrayList<>();
        if (this.type != MMTypes.CardType.Push) return actions;

        switch (this) {
            case PUSH_1:
                // Check all neighbours distance 1
                for (int i = 0; i < gs.getBoard().getHeight(); i++) {
                    for (int j = 0; j < gs.getBoard().getWidth(); j++) {
                        BoardSpot boardSpot = gs.getBoard().getElement(j, i);
                        if (boardSpot != null && boardSpot.occupant == MMTypes.MarbleType.player(playerID)) {
                            Vector2D from = new Vector2D(j, i);
                            for (Vector2D to: Constants.getNeighbours(from)) {
                                // 1 space away
                                BoardSpot spot = gs.getBoard().getElement(to.getX(), to.getY());
                                if (spot != null) {
                                    // todo some rules don't allow to split columns
                                    if (spot.occupant == MMTypes.MarbleType.player(playerID)) {
                                        // Check push requirements
                                        AbstractAction action = gs.getRulesInPlay().get(MMTypes.CardType.Push).canPush(gs, from, to, playerID, 1);
                                        if (action != null) {
                                            actions.add(action);  // todo ncolumns
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default: return actions;
        }
        return actions;
    }

    public AbstractAction canPush(MMGameState gs, Vector2D from, Vector2D to, int playerPushing, int nCols) {
        if (this.type != MMTypes.CardType.Push) return null;
        switch(this) {
            case PUSH_1:
                // find direction from -> to
                int direction = Constants.direction(from, to);
                // go from 'from' in the direction, stopping when we find an empty space or an opponent marble. count marbles in our column
                Vector2D last = calculateColumn(gs.getBoard(), from, direction, playerPushing);
                int count = last.subtract(from).magnitude();
                // if current occupant is not empty, keep going until we find an empty space or a different player's marble
                Vector2D current = Constants.add_direction(last, direction);
                Vector2D oppStart = current.copy();
                int oppCount = 0;
                if (gs.getBoard().isInBounds(current.getX(), current.getY())
                        && gs.getBoard().getElement(current) != null
                        && gs.getBoard().getElement(current).occupant != null) {
                    int opponent = gs.getBoard().getElement(current).occupant.ordinal();
                    Vector2D oppLast = calculateColumn(gs.getBoard(), current, direction, opponent);
                    oppCount = oppLast.subtract(oppStart).magnitude();
                    if (gs.getBoard().isInBounds(current.getX(), current.getY())
                            && gs.getBoard().getElement(current) != null
                            && gs.getBoard().getElement(current).occupant != null) {
                        // Chain pushes
                        if (gs.getRulesInPlay().get(MMTypes.CardType.PushRequirement).pushReq(count, oppCount))
                            return canPush(gs, oppStart, Constants.add_direction(oppStart, direction), opponent, nCols+1);
                    }
                }
                if (gs.getRulesInPlay().get(MMTypes.CardType.PushRequirement).pushReq(count, oppCount)) {
                    return new Push(playerPushing, from, to, nCols);
                } else return null;
            default:
                return null;
        }
    }

    public void pushOut(MMGameState state, MMTypes.MarbleType player) {
        if (this.type != MMTypes.CardType.PushOut) return;
        switch (this) {
            case OUT_IS_GONE:
                // Nothing to do, it's just gone
                break;
            default:
                break;
        }
    }

    // Returns the last spot in the column
    public static Vector2D calculateColumn(GridBoard<BoardSpot> board, Vector2D from, int direction, int playerID) {
        Vector2D current = from;
        while (board.isInBounds(current.getX(), current.getY())
                && board.getElement(current) != null
                && board.getElement(current).occupant == MMTypes.MarbleType.player(playerID)) {
            current = Constants.add_direction(current, direction);
        }
        return Constants.subtract_direction(current, direction);
    }

    private boolean pushReq(int count, int oppCount) {
        if (this.type != MMTypes.CardType.PushRequirement) return false;
        return switch (this) {
            case MORE -> count > oppCount;
            case MORE_OR_EQUAL -> count >= oppCount;
            case EQUAL -> count == oppCount;
            case FEWER_OR_MORE -> count != oppCount;
            case FEWER_OR_EQUAL -> count <= oppCount;
            case MAJORITY -> count > oppCount;  // todo, this is different...
            case MINORITY -> count < oppCount;  // todo, this is different...
            case UNEVEN -> count % 2 == 1;
            case EVEN -> count % 2 == 0;
            default -> false;
        };
    }

    public void parseSetup(GridBoard<BoardSpot> board) {
        if (this.type != MMTypes.CardType.Setup) return;

        try (BufferedReader reader = new BufferedReader(new FileReader("data/marbles/setup/" + description))) {
            String line;
            int y = 0;
            boolean marbleSection = false;

            // First parse the board spots and create BoardSpot objects
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    // Switch to marble section when an empty line is encountered
                    marbleSection = true;
                    y = 0; // Reset y for marble placement
                    continue;
                }

                String[] tokens = line.split(",");
                for (int x = 0; x < tokens.length; x++) {
                    if (!marbleSection) {
                        // Parsing the first part: board spot types
                        BoardSpot spot = null;
                        switch (tokens[x]) {
                            case "-1": // No board spot here
                                break;
                            case "0":
                                spot = new BoardSpot(x, y, MMTypes.SpotType.NORMAL, null);
                                break;
                            default:
                                spot = new BoardSpot(x, y, MMTypes.SpotType.VICTORY, MMTypes.MarbleType.valueOf(tokens[x]));
                                break;
                        }
                        board.setElement(x, y, spot);
                    } else {
                        // Parsing the second part: marble placements
                        BoardSpot spot = board.getElement(x, y);
                        if (spot != null && !tokens[x].equals("0")) {
                            spot.addMarble(MMTypes.MarbleType.valueOf(tokens[x]));
                        }
                    }
                }
                y++; // Move to the next row
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int checkVictory(MMGameState gs) {
        if (this.type != MMTypes.CardType.Victory) return -1;

        switch(this) {
            case YOUR_COLOR:
                boolean[] win = new boolean[gs.getNPlayers()];
                Arrays.fill(win, true);
                for (int i = 0; i < gs.getBoard().getHeight(); i++) {
                    for (int j = 0; j < gs.getBoard().getWidth(); j++) {
                        BoardSpot boardSpot = gs.getBoard().getElement(j, i);
                        if (boardSpot != null && boardSpot.victoryOwner != null &&
                                boardSpot.victoryOwner.ordinal() < gs.getNPlayers() &&
                                boardSpot.occupant != boardSpot.victoryOwner)
                            win[boardSpot.victoryOwner.ordinal()] = false;
                    }
                }
                for (int i = 0; i < win.length; i++) {
                    if (win[i]) return i;
                }
                return -1;
            default: return -1;
        }
    }
}