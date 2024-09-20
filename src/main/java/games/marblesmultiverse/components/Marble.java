package games.marblesmultiverse.components;

import core.components.Component;
import core.components.Token;

import java.util.Objects;

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

    @Override
    public Token copy(int playerId) {
        return super.copy(playerId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Marble marble = (Marble) o;
        return color == marble.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), color);
    }
}
