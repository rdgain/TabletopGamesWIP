package games.marblesmultiverse.components;

import core.components.BoardNode;

import java.util.Objects;

public class BoardSpot extends BoardNode {

    // position on grid, r-odd (shift)
    public final int x;
    public final int y;

    MMTypes.SpotType type; // type of node
    MMTypes.MarbleType occupant; // who is in this spot
    MMTypes.MarbleType victoryOwner; // who is this victory from

    public BoardSpot(int x, int y, MMTypes.SpotType type) {
        super();
        this.type = type;
        this.x = x;
        this.y = y;
        occupant = null;
        victoryOwner= null;
    }
    public BoardSpot(int x, int y, MMTypes.SpotType type, MMTypes.MarbleType victoryOwner) {
        super();
        this.type = type;
        this.x = x;
        this.y = y;
        this.occupant = null;
        this.victoryOwner= victoryOwner;
    }

    public BoardSpot(int x, int y, MMTypes.SpotType type, int ID) {
        super(-1, "create name with X and Y", ID);
        this.type = type;
        this.x = x;
        this.y = y;
        occupant = null;
        victoryOwner= null;
    }

    public boolean addMarble(MMTypes.MarbleType newOccupant){
        if(type == MMTypes.SpotType.VORTEX || type == MMTypes.SpotType.BLOCKAGE){
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

    public MMTypes.MarbleType getOccupant() {
        return occupant;
    }

    public MMTypes.MarbleType getVictoryOwner() {
        return victoryOwner;
    }

    public MMTypes.SpotType getSpotType() {
        return type;
    }

    @Override
    public BoardSpot copy() {
        BoardSpot copy = new BoardSpot(x, y, type, componentID);
        copy.occupant = occupant;
        copy.victoryOwner = victoryOwner;
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BoardSpot boardSpot = (BoardSpot) o;
        return x == boardSpot.x && y == boardSpot.y && type == boardSpot.type && occupant == boardSpot.occupant && victoryOwner == boardSpot.victoryOwner;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, x, y, occupant, victoryOwner);
    }
}
