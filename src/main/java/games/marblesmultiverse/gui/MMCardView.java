package games.marblesmultiverse.gui;

import games.marblesmultiverse.components.Card;
import gui.views.CardView;
import gui.views.ComponentView;

import javax.swing.*;
import java.awt.*;

// TODO extends CardView when MMCard is available
public class MMCardView extends ComponentView {
    final static int cardWidth = 150;
    final static int cardHeight = 200;

    public MMCardView(Card c) {
        super(null, cardWidth, cardHeight);
        this.setLayout(null);
        JTextArea desc = new JTextArea(c.type + "\n\n" + c.description);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setFont(desc.getFont().deriveFont(13f));
        desc.setEditable(false);
        desc.setFocusable(false);
        desc.setBounds(10,10,cardWidth-20,cardHeight-20);
        add(desc);
    }

    @Override
    protected void paintComponent(Graphics gg) {
        Graphics2D g = (Graphics2D) gg;
        g.setColor(Color.white);
        g.fillRect(5, 5, cardWidth-5, cardHeight-5);
        g.setColor(Color.black);
        g.drawRect(5, 5, cardWidth-5, cardHeight-5);
//        CardView.drawCard(g2d, null, rect);
    }

}
