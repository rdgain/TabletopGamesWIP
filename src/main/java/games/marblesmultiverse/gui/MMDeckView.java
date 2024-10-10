package games.marblesmultiverse.gui;

import core.components.Card;
import core.components.Deck;
import gui.views.CardView;
import gui.views.DeckView;

import java.awt.*;

public class MMDeckView extends DeckView<Card> {

    public MMDeckView(int player, Deck<Card> d, boolean visible, int componentWidth, int componentHeight) {
        super(player, d, visible, componentWidth, componentHeight);
    }

    @Override
    public void drawComponent(Graphics2D g, Rectangle rect, Card card, boolean front) {
        int fontSize = g.getFont().getSize();

    }
}
