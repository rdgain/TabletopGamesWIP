package games.marblesmultiverse.gui;

import core.components.Card;
import core.components.Component;
import gui.views.CardView;
import gui.views.ComponentView;

import javax.swing.*;
import java.awt.*;

// TODO extends CardView when MMCard is available
public class MMCardView extends ComponentView {
    final static int cardWidth = 150;
    final static int cardHeight = 200;
    public MMCardView(Component c) {
        super(c, cardWidth, cardHeight);
        this.setLayout(new FlowLayout());
        JTextArea desc = new JTextArea("Lorem ipsum dolor sit amet. Sed quia quia nam assumenda quasi ea enim facilis ut sapiente nisi et");
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setFont(desc.getFont().deriveFont(13f));
        add(desc);
        //super(c);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int fontSize = g.getFont().getSize();
        Rectangle rect = new Rectangle(5, 5, width-5, height-5);
//        g.drawString("Lorem ipsum dolor sit amet. Sed quia quia nam assumenda quasi ea enim facilis ut sapiente nisi et nulla beatae quo sequi quae ex praesentium animi. Et vitae neque ea consequatur odio et voluptate exercitationem est nihil quos.", 5, 5);
        g.drawRect(5, 5, cardWidth-5, cardHeight-5);
        CardView.drawCard(g2d, null, rect);
//        g.drawString("Lorem ipsum dolor sit amet. Sed quia quia nam assumenda quasi ea enim facilis ut sapiente nisi et ", 2, (int) (20 + fontSize * 1.5));
    }

}
