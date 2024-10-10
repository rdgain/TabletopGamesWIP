package games.marblesmultiverse.gui;

import core.components.Component;
import gui.views.ComponentView;

import java.awt.*;
import java.util.ArrayList;

import static gui.AbstractGUIManager.defaultItemSize;

public class MMBoardView extends ComponentView {

    private int offsetX = 10;

    private int tempGridHeight = 9;
    private int tempGridWidth = 5;
    ArrayList<Integer> widths;


    public MMBoardView(Component c) {
        super(c, 600, 600);
        widths = new ArrayList<>();

        //Generate width per row for the Hex Grid
        int w = tempGridWidth;
        for (int i = 0; i < tempGridHeight; i++) {
            widths.add(w);
            if (i < tempGridHeight/2) {
                w++;
            }
            else {
                w--;
            }
        }
//        System.out.println(widths);
    }

    @Override
    protected void paintComponent(Graphics g) {
        drawHexGrid((Graphics2D) g, 10 + 4 * defaultItemSize + 10, defaultItemSize);
    }

    // TODO: add arguments for color and if have to draw a piece on top
    protected void drawCell(Graphics2D g, int x, int y, String text) {
        // Create hexagon
        Polygon h = new Polygon();
        for (int i = 0; i < 6; i++) {
            h.addPoint((int) (x + defaultItemSize/2 * Math.cos(Math.PI/2 + i * 2 * Math.PI / 6)),
                    (int) (y + defaultItemSize/2 * Math.sin(Math.PI/2 + i * 2 * Math.PI / 6)));
        }
//        g.setColor(new Color(183, 55, 55));
//        g.fillPolygon(h);
        g.drawPolygon(h);
        g.drawString(text, x-offsetX, y);
    }

    protected void drawHexGrid(Graphics2D g, int x, int y) {
        int offsetY = defaultItemSize/2;

        // TODO: Color the goal hexes with data from Board (component)
        // TODO: Also use if there's pieces on top of a grid

        for (int i = 0; i < tempGridHeight; i++) {
            int w = widths.get(i);
            for (int j = 0; j < w; j++) {
                int offsetX = - ((w - tempGridWidth) * defaultItemSize / 2);
//                if (i % 2 == 1) {
//                    offsetX += defaultItemSize / 2;
//                }
                int xC = x + offsetX + j * defaultItemSize;
                int yC = y + offsetY + i * defaultItemSize;
                drawCell(g, xC, yC, i + ", " + j);
            }
        }
    }
}
