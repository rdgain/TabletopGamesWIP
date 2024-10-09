package games.marblesmultiverse.components;

public class MMTypes {
    public enum MarbleType {
        BLUE,
        GREEN,
        RED,
        YELLOW,
        PURPLE,
        NEUTRAL,
    }

    public enum CardType{
        Setup,
        Victory,
        Movement,
        Push,
        PushRequirement,
        PushOut
    }

    public enum SpotType {
        NORMAL,
        VORTEX,
        BLOCKAGE,
        VICTORY,
        VOID
    }
}
