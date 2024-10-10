package games.marblesmultiverse.gui;

import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.components.BoardSpot;
import games.marblesmultiverse.components.MMTypes;
import gui.views.ComponentView;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import static games.marblesmultiverse.components.MMTypes.MarbleType.*;
import static games.marblesmultiverse.components.MMTypes.SpotType.*;
import static gui.AbstractGUIManager.defaultItemSize;

public class MMBoardView extends ComponentView {

    MMGameState gs;

    Map<MMTypes.MarbleType, Color> marbleTypeColorMap = new HashMap<>() {{
       put(BLUE, new Color(37, 126, 171));
       put(GREEN, new Color(119, 154, 39));
       put(RED, new Color(176, 21, 47));
       put(YELLOW, new Color(182, 147, 5));
       put(PURPLE, new Color(104, 33, 190));
       put(NEUTRAL, new Color(130, 146, 173));
    }};

    Map<MMTypes.SpotType, Color> spotTypeColorMap = new HashMap<>() {{
        put(NORMAL, new Color(245, 248, 250));
        put(VORTEX, new Color(98, 101, 89));
        put(BLOCKAGE, new Color(51, 35, 37));
        put(VOID, new Color(38, 38, 38));
    }};

    public MMBoardView(MMGameState gs) {
        super(gs.getBoard(), 600, 600);
        this.gs = gs;
    }

    @Override
    protected void paintComponent(Graphics g) {
        drawHexGrid((Graphics2D) g, 0, 0);
    }

    protected void drawCell(Graphics2D g, int x, int y, String text, BoardSpot spot) {
        if (spot == null) return;

        // Create hexagon
        Polygon h = new Polygon();
        for (int i = 0; i < 6; i++) {
            h.addPoint((int) (x + defaultItemSize/2 * Math.cos(Math.PI/2 + i * 2 * Math.PI / 6)),
                    (int) (y + defaultItemSize/2 * Math.sin(Math.PI/2 + i * 2 * Math.PI / 6)));
        }

        MMTypes.SpotType type = spot.getSpotType();
        MMTypes.MarbleType occupant = spot.getOccupant();
        MMTypes.MarbleType victoryOwner = spot.getVictoryOwner();

        Stroke s = g.getStroke();
        if (type == VICTORY) {
            g.setColor(Color.white);
            g.fillPolygon(h);
            g.setColor(marbleTypeColorMap.get(victoryOwner));
            g.setStroke(new BasicStroke(5));
        } else {
            g.setColor(spotTypeColorMap.get(type));
            g.fillPolygon(h);
            g.setColor(Color.black);
        }
        g.drawPolygon(h);
        g.setStroke(s);

        if (occupant != null) {
            drawSphere(g, x, y, marbleTypeColorMap.get(occupant));
        }

//        g.drawString(text, x-offsetX, y);
    }

    public void drawSphere(Graphics2D g2d, int centerX, int centerY, Color color) {
        int diameter = (int) (defaultItemSize * 0.6f);

        // Gradient to simulate light hitting the sphere
        Point2D center = new Point2D.Float(centerX, centerY);
        float radius = diameter / 2f;

        // Create a radial gradient that goes from the sphere color to a darker version
        RadialGradientPaint gradientPaint = new RadialGradientPaint(
                center,
                radius,
                new float[]{0f, 1f}, // Distribution of the gradient (inner/outer)
                new Color[]{color.brighter(), color.darker()} // Colors at inner/outer edges
        );

        g2d.setPaint(gradientPaint);
        g2d.fillOval(centerX - diameter / 2, centerY - diameter / 2, diameter, diameter);
    }

    protected void drawHexGrid(Graphics2D g, int x, int y) {
        int offsetY = defaultItemSize/2;

        for (int i = 0; i < gs.getBoard().getHeight(); i++) {
            for (int j = 0; j < gs.getBoard().getWidth(); j++) {

                int offsetX = defaultItemSize/2;
                if (i % 2 == 1) {
                    offsetX += defaultItemSize/2;
                }

                int xC = x + offsetX + j * defaultItemSize;
                int yC = y + offsetY + i * defaultItemSize;
                drawCell(g, xC, yC, i + ", " + j, gs.getBoard().getElement(j, i));

                // Save rect where cell is drawn
//                rects.put(new Rectangle(xC - defaultItemSize/2, yC - defaultItemSize/2, defaultItemSize, defaultItemSize), "grid-" + j + "-" + i);
            }
        }
    }
}
