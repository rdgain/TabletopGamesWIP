package games.marblesmultiverse.components;

import core.components.Token;

public class Marble extends Token {

    public enum MarbleColor{
        BLUE,
        GREEN,
        RED,
        YELLOW,
        PURPLE
    }

    private final MarbleColor color;

    public Marble(MarbleColor color,String name, int ID) {
        super(name, ID);
        this.color=color;
    }

    public MarbleColor getColor() {return  color;};
}
