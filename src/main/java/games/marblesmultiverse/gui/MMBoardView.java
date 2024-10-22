package games.marblesmultiverse.gui;

import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.components.BoardSpot;
import games.marblesmultiverse.components.MMTypes;
import gui.IScreenHighlight;
import gui.views.ComponentView;
import org.w3c.dom.css.Rect;
import utilities.Vector2D;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;
import java.util.LinkedList;

import static games.marblesmultiverse.components.MMTypes.MarbleType.*;
import static games.marblesmultiverse.components.MMTypes.SpotType.*;
import static gui.AbstractGUIManager.defaultItemSize;

public class MMBoardView extends ComponentView implements IScreenHighlight {

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
        put(VICTORY, new Color(245, 248, 250));
        put(VORTEX, new Color(98, 101, 89));
        put(BLOCKAGE, new Color(51, 35, 37));
        put(VOID, new Color(38, 38, 38));
    }};
    private Map<Rectangle, String> rects;
    private LinkedList<Rectangle> highlights;
    private int nMaxHighlights = 2;
    private Color highlightToColor = new Color(10, 178, 89);
    private Color highlightFromColor = new Color(144, 176, 0);

    public MMBoardView(MMGameState gs) {
        super(gs.getBoard(), 500, 500);
        this.gs = gs;
        rects = new HashMap<>();
        highlights = new LinkedList<>();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    for (Map.Entry<Rectangle, String> entry : rects.entrySet()) {
                        if (entry.getKey().contains(e.getPoint())) {
                            highlights.add(entry.getKey());
                            if (highlights.size() > nMaxHighlights) {
                                highlights.removeFirst();
                            }
                            break;
                        }
                    }
                } else {
                    highlights.clear();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        drawHexGrid((Graphics2D) g);
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
        g.setColor(spotTypeColorMap.get(type));
        g.fillPolygon(h);
        if (type == VICTORY) {
            g.setColor(marbleTypeColorMap.get(victoryOwner));
            g.setStroke(new BasicStroke(5));
        } else {
            g.setColor(Color.black);
        }
        g.drawPolygon(h);
        g.setStroke(s);

        if (occupant != null) {
            drawSphere(g, x, y, marbleTypeColorMap.get(occupant));
        }

        if (isHighlighted(new Vector2D(spot.x, spot.y), 0)) {
            g.setColor(highlightFromColor);
            g.drawPolygon(h);
        } else if (isHighlighted(new Vector2D(spot.x, spot.y), 1)) {
            g.setColor(highlightToColor);
            g.drawPolygon(h);
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

    protected void drawHexGrid(Graphics2D g) {
        int offsetY = defaultItemSize/2;

        for (int i = 0; i < gs.getBoard().getHeight(); i++) {
            for (int j = 0; j < gs.getBoard().getWidth(); j++) {

                int offsetX = defaultItemSize/2;
                if (i % 2 == 1) {
                    offsetX += defaultItemSize/2;
                }

                int xC = 10 + offsetX + j * defaultItemSize;
                int yC = 10 + offsetY + i * defaultItemSize;
                drawCell(g, xC, yC, i + ", " + j, gs.getBoard().getElement(j, i));

                // Save rect where cell is drawn
                rects.put(new Rectangle(xC - defaultItemSize/2, yC - defaultItemSize/2, defaultItemSize, defaultItemSize), "grid-" + j + "-" + i);
            }
        }
    }

    @Override
    public void clearHighlights() {
        highlights.clear();
    }

    public List<Rectangle> getHighlights() {
        return highlights;
    }

    public boolean isHighlighted(Vector2D pos) {
        for (Rectangle r : highlights) {
            if (rects.get(r).equals("grid-" + pos.getX() + "-" + pos.getY())) {
                return true;
            }
        }
        return false;
    }

    public boolean isHighlighted(Vector2D pos, int idx) {
        if (highlights.size() <= idx) return false;
        Rectangle r = highlights.get(idx);
        return rects.get(r).equals("grid-" + pos.getX() + "-" + pos.getY());
    }
}
