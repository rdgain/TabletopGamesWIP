package games.marblesmultiverse.components;

import core.components.Component;
import core.components.Token;

import java.util.Objects;

public class Marble extends Token {


    private final MarbleTypes color;

    public Marble(MarbleTypes color,String name) {
        super(name);
        this.color=color;
    }

    public MarbleTypes getColor() {return  color;};

    @Override
    public Token copy(int playerId) {
        return (Token) super.copy(playerId);
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
