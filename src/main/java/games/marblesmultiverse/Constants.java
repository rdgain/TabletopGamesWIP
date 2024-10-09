package games.marblesmultiverse;

import utilities.Vector2D;

public class Constants {
    public static Vector2D[][] neighbor_directions = new Vector2D[][] {{new Vector2D(1, 0), new Vector2D(0, -1),
            new Vector2D(-1, -1), new Vector2D(-1, 0),
            new Vector2D(-1, 1), new Vector2D(0, 1)},
            {new Vector2D(1, 0), new Vector2D(1, -1),
                    new Vector2D(0, -1), new Vector2D(-1, 0),
                    new Vector2D(0, 1), new Vector2D(1, 1)}};

    public static int grid_distance( Vector2D a, Vector2D b ){
        int x0 = (int) (a.getX()-Math.floor((double) b.getX() /2));
        int y0 = b.getX();
        int x1 = (int) (a.getY()-Math.floor((double) b.getY() /2));
        int y1 = b.getY();
        int dx = x1 - x0;
        int dy = y1 - y0;
        int dist = Math.max(Math.abs(dx), Math.abs(dy));
        dist = Math.max(dist, Math.abs(dx+dy));
        return dist;
    }
}
