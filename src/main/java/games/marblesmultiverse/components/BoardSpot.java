package games.marblesmultiverse.components;

import core.components.BoardNode;

public class BoardSpot extends BoardNode {


    public enum SpotType {
        NORMAL,
        VORTEX,
        BLOCKAGE,
        VICTORY;
    }
    // type of node
    SpotType type;
    // position on grid
    public final int x;
    public final int y;
    // who is in this spot
    Marble occupant;
    public BoardSpot(int x, int y, SpotType type) {
        super();
        this.type = type;
        this.x = x;
        this.y = y;
        occupant = null;
    }


    // dunno if it is going to be necessary but just in case
    public boolean AddMarble(Marble newOccupant){
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
    public boolean RemoveMarble(){
        if(occupant == null){
            return false;
        }
        else {
            occupant = null;
            return true;
        }
    }
}
