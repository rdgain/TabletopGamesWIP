package games.marblesmultiverse.components;

public enum Cards{
    YOUR_COLOR("Occupy all goals of your color.", CardType.Victory),
    ONE_OF_EACH("Occupy 1 goal of each color.", CardType.Victory),
    DIAGONAL("Occupy 2 opposing goals and the center (if it exists).", CardType.Victory),
    ANY_THREE("Occupy 3 non-neighbouring goals.", CardType.Victory),
    BORDERLANDS("Put 1 marble on each side of the board. Corners do not count.", CardType.Victory),
    PUSH_OUT("Push out 3 opposing marbles.", CardType.Victory),
    MOVE_1("Move 1 marble by 1 space in any direction.", CardType.Movement),
    MOVE_EXACTLY_2("Move 1 marble exactly 2 spaces in any direction.", CardType.Movement),
    MOVE_2("Move 1 marble by up to 3 spaces in any direction in a straight line.", CardType.Movement),
    SPLIT_MOVE("Move 1 or 2 marbles by 1 space in any direction.", CardType.Movement),
    SIDESTEP("Move a column of marbles orthogonally by 1 space or move 1 marble by 1 space in any direction.", CardType.Movement),
    LEAPFROG("Jump over 1 marble or blocker or move a marble by 1 space in any direction", CardType.Movement),
    CHAIN_FROG("Repeatedly jump over 1 marble or blocker or move a marble by 1 space in any direction", CardType.Movement),
    PUSH_1("Push a column of marbles by 1 space in any direction", CardType.Push),
    PUSH_2("Push a column of marbles by up to 2 spaces in any direction.", CardType.Push),
    SPLIT_PUSH("Push a column of marbles by up to 2 spaces in any direction.", CardType.Push),
    MUST_PUSH_OPPONENT("You must push a column of opposing marbles by 1 space, if you can.", CardType.Push),
    MUST_PUSH_ANY("You must push a column of marbles by 1 space, if you can. Including your own.", CardType.Push),
    MOMENTUM("Push all the way in any direction until your first marble reaches the edge or you cannot push anymore.", CardType.Push),
    MORE("You can push opposing marbles if you use more marbles.", CardType.PushRequirement),
    MORE_OR_EQUAL("You can push opposing marbles if you use more or equal marbles. Do not split your column while pushing the opponent.", CardType.PushRequirement),
    EQUAL("You can push opposing marbles if you use equal marbles. Do not split your column while pushing the opponent.", CardType.PushRequirement),
    FEWER_OR_MORE("You can push opposing marbles if you use fewer or more marbles.", CardType.PushRequirement),
    FEWER_OR_EQUAL("You can push opposing marbles if you use fewer or equal marbles. Do not split your column while pushing the opponent.", CardType.PushRequirement),
    MAJORITY("You can push opposing marbles if you have more marbles anywhere in the line.", CardType.PushRequirement),
    MINORITY("You can push opposing marbles if you have fewer marbles anywhere in the line.", CardType.PushRequirement),
    UNEVEN("You can push your opposing marbles if you use an uneven number of marbles (1, 3 or 5). Do not split your column while pushing the opponent.", CardType.PushRequirement),
    EVEN("You can push opposing marbles if you use an even number of marbles (2, 4 or 6). Do not split your column while pushing the opponent.", CardType.PushRequirement),
    OUT_IS_GONE("ushed out marbles are removed from play", CardType.PushOut),
    CENTER_IF_FREE("Pushed out marbles return to the center, if it is free. Else they are removed.", CardType.PushOut),
    CENTER_REPLACE("Pushed out marbles return to the center. Remove any pre-existing marble.", CardType.PushOut),
    THE_RETURN("The last pushed out marble returns to a free space of the next opponent's choice.", CardType.PushOut),
    REPLACEMENT("The last pushed out marble replaces an opponent's marble of their choice. You cannot push out your own marbles.", CardType.PushOut),
    WARP_AROUND("Instead of being pushed out, marbles \"wrap around\" to the other end of the line.", CardType.PushOut),
    TELEPORT_IF_FREE("Pushed out marbles appear on the other end of the line, if that space is free. Else they are removed.", CardType.PushOut),
    TELEPORT_REPLACE("Pushed out marbles appear on the other end of the line. Remove any pre-existing marble. You cannot push out your own marbles.", CardType.PushOut);

    public final String description;
    public final CardType type;

    Cards(String description, CardType type){
        this.description =description;
        this.type=type;
    }
}