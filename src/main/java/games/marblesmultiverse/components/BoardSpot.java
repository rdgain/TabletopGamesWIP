package games.marblesmultiverse.components;

import com.univocity.parsers.annotations.Copy;
import core.components.BoardNode;
import scala.collection.parallel.mutable.UnrolledParArrayCombiner;
import utilities.Vector2D;

import java.util.Objects;

public class BoardSpot extends BoardNode {
    // use this for r(odd) shift


    public enum SpotType {
        NORMAL,
        VORTEX,
        BLOCKAGE,
        VICTORY,
        VOID;
    }
    // type of node
    SpotType type;
    // position on grid
    public final int x;
    public final int y;
    // who is in this spot
    Marble occupant;

    // who is this victory from
    MarbleTypes victoryOwner;
    public BoardSpot(int x, int y, SpotType type) {
        super();
        this.type = type;
        this.x = x;
        this.y = y;
        occupant = null;
        victoryOwner= null;
    }
    public BoardSpot(int x, int y, SpotType type, MarbleTypes victoryOwner) {
        super();
        this.type = type;
        this.x = x;
        this.y = y;
        this.occupant = null;
        this.victoryOwner= victoryOwner;
    }

    public BoardSpot(int x, int y, SpotType type, int ID) {
        super(-1, "create name with X and Y", ID);
        this.type = type;
        this.x = x;
        this.y = y;
        occupant = null;
        victoryOwner= null;
    }



    // dunno if it is going to be necessary but just in case
    public boolean addMarble(Marble newOccupant){
        if(type == SpotType.VORTEX || type == SpotType.BLOCKAGE){
            return false;
        }
        if(occupant == null){
            return false;
        }
        else {
            occupant = newOccupant;
            return true;
        }
    }
    public boolean removeMarble(){
        if(occupant == null){
            return false;
        }
        else {
            occupant = null;
            return true;
        }
    }

    @Override
    public BoardSpot copy() {
        BoardSpot = new BoardSpot(); // add stuff
        return super.copy();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BoardSpot boardSpot = (BoardSpot) o;
        return x == boardSpot.x && y == boardSpot.y && type == boardSpot.type && Objects.equals(occupant, boardSpot.occupant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, x, y, occupant);
    }
}
