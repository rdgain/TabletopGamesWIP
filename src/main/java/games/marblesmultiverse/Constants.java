package games.marblesmultiverse;

import utilities.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static Vector2D[][] neighbor_directions = new Vector2D[][] {
            {
                new Vector2D(1, 0), new Vector2D(0, -1),
                new Vector2D(-1, -1), new Vector2D(-1, 0),
                new Vector2D(-1, 1), new Vector2D(0, 1)
            },
            {
                new Vector2D(1, 0), new Vector2D(1, -1),
                new Vector2D(0, -1), new Vector2D(-1, 0),
                new Vector2D(0, 1), new Vector2D(1, 1)
            }};

    public static List<Vector2D> getNeighbours(Vector2D cell) {
        ArrayList<Vector2D> neighbors = new ArrayList<>();
        int parity = Math.abs(cell.getY() % 2);
        for (Vector2D v: neighbor_directions[parity]) {
            neighbors.add(cell.add(v));
        }
        return neighbors;
    }

    public static int direction(Vector2D a, Vector2D b) {
        // Calculate the difference between point a and point b
        int dx = b.getX() - a.getX();
        int dy = b.getY() - a.getY();

        // Determine which row offset we are in: odd or even
        int rowParity = a.getY() & 1; // a.y % 2 (0 for even rows, 1 for odd rows)

        // Compare the difference with each direction in the neighbor_directions array
        for (int dir = 0; dir < 6; dir++) {
            Vector2D direction = neighbor_directions[rowParity][dir];
            if (dx == direction.getX() && dy == direction.getY()) {
                return dir; // Return the direction index if it matches
            }
        }

        // Return -1 if no valid direction is found (this shouldn't happen if a and b are neighbors)
        return -1;
    }

    public static Vector2D add_direction(Vector2D a, int dir) {
        int parity = Math.abs(a.getY() % 2);
        return a.add(neighbor_directions[parity][dir]);
    }

    public static Vector2D subtract_direction(Vector2D a, int dir) {
        int parity = Math.abs(a.getY() % 2);
        return a.subtract(neighbor_directions[parity][dir]);
    }

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
