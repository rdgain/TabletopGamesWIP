package games.marblesmultiverse.components;

public class MMTypes {
    public enum MarbleType {
        BLUE,
        GREEN,
        RED,
        YELLOW,
        PURPLE,
        NEUTRAL;

        public static MarbleType player(int playerID) {
            return MarbleType.values()[playerID];
        }
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
