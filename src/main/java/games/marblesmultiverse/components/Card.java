package games.marblesmultiverse.components;

import core.actions.AbstractAction;
import core.components.GridBoard;
import games.marblesmultiverse.Constants;
import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.actions.Move;
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
    OUT_IS_GONE("ushed out marbles are removed from play", MMTypes.CardType.PushOut),
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
                                BoardSpot spot = gs.getBoard().getElement(to.getX(), to.getY());
                                if (spot != null) {
                                    if (spot.occupant == null) {
                                        // It's a legal move!
                                        actions.add(new Move(playerID, from, to));
                                    } else if (spot.occupant == MMTypes.MarbleType.player(playerID)) {
                                        // Check push requirements
                                        if (gs.getRulesInPlay().get(MMTypes.CardType.Push).canPush(gs, from, to)) {
                                            actions.add(new Move(playerID, from, to));
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

    public boolean canPush(MMGameState gs, Vector2D from, Vector2D to) {
        if (this.type != MMTypes.CardType.Push) return false;
        switch(this) {
            case PUSH_1:  // todo
                return false;
            default:
                return false;
        }
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
                            boardSpot.occupant != boardSpot.victoryOwner) win[boardSpot.victoryOwner.ordinal()] = false;
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