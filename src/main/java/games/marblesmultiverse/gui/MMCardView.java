package games.marblesmultiverse.gui;

import games.marblesmultiverse.components.Card;
import gui.views.CardView;
import gui.views.ComponentView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

// TODO extends CardView when MMCard is available
public class MMCardView extends JScrollPane {
    final static int cardWidth = 150;
    final static int cardHeight = 180;
    final static Border border = BorderFactory.createLineBorder(Color.black, 2);

    public MMCardView(Card c) {
        this.setLayout(null);
        JTextArea desc = new JTextArea(c.type + "\n\n" + c.description);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setFont(desc.getFont().deriveFont(13f));
        desc.setEditable(false);
        desc.setFocusable(false);
        desc.setBounds(10,10,cardWidth-20,cardHeight-20);
        desc.setPreferredSize(new Dimension(cardWidth, cardHeight*2));
        setBackground(Color.white);
//        setBorder(border);
        add(desc);
    }

//    @Override
//    protected void paintComponent(Graphics gg) {
//        super.paintComponent(gg);
//        Graphics2D g = (Graphics2D) gg;
////        CardView.drawCard(g, null, rect);
//    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(cardWidth, cardHeight);
    }
}
