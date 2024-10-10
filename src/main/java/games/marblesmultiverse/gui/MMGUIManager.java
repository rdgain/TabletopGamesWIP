package games.marblesmultiverse.gui;

import core.AbstractGameState;
import core.AbstractPlayer;
import core.Game;
import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.components.Card;
import gui.AbstractGUIManager;
import gui.GamePanel;
import gui.IScreenHighlight;
import players.human.ActionController;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * <p>This class allows the visualisation of the game. The game components (accessible through {@link Game#getGameState()}
 * should be added into {@link javax.swing.JComponent} subclasses (e.g. {@link javax.swing.JLabel},
 * {@link javax.swing.JPanel}, {@link javax.swing.JScrollPane}; or custom subclasses such as those in {@link gui} package).
 * These JComponents should then be added to the <code>`parent`</code> object received in the class constructor.</p>
 *
 * <p>An appropriate layout should be set for the parent GamePanel as well, e.g. {@link javax.swing.BoxLayout} or
 * {@link java.awt.BorderLayout} or {@link java.awt.GridBagLayout}.</p>
 *
 * <p>Check the super class for methods that can be overwritten for a more custom look, or
 * {@link games.terraformingmars.gui.TMGUI} for an advanced game visualisation example.</p>
 *
 * <p>A simple implementation example can be found in {@link games.tictactoe.gui.TicTacToeGUIManager}.</p>
 */
public class MMGUIManager extends AbstractGUIManager {

    private int cardWidth = 50;
    private int cardHeight = 100;

    public MMGUIManager(GamePanel parent, Game game, ActionController ac, Set<Integer> human) {
        super(parent, game, ac, human);
        MMGameState gameState = (MMGameState) game.getGameState();

        // Main Game area
        JPanel mainGameArea = new JPanel(new FlowLayout());

//        JButton boardPlaceHolder = new JButton("HEX GRID");
        MMBoardView boardPlaceHolder = new MMBoardView(gameState);

        // Rule Panel to display current rules
        JPanel rulePanel = new JPanel(new GridLayout(3, 2));
        for (Card card: gameState.getRulesInPlay().values()) {
            //JButton rule = new JButton("Rule " + i);
            MMCardView rule = new MMCardView(card);
            rulePanel.add(rule);
        }
        // Panel to display draw pile and discard pile
        JPanel drawDiscardPanel = new JPanel(new FlowLayout());
        JButton drawPile = new JButton("Draw Pile");
        JButton discardPile = new JButton("DiscardPile");
        drawDiscardPanel.add(drawPile);
        drawDiscardPanel.add(discardPile);

        mainGameArea.add(boardPlaceHolder);
        mainGameArea.add(rulePanel);
        mainGameArea.add(drawDiscardPanel);

//        JPanel infoPanel = createGameStateInfoPanel("Multiverse Marbles", gameState, width, defaultInfoPanelHeight);
        JComponent actionPanel = createActionPanel(new IScreenHighlight[0], width, defaultActionPanelHeight, false, true, null, null, null);

        parent.setLayout(new BorderLayout());
        parent.add(mainGameArea, BorderLayout.CENTER);
//        parent.add(infoPanel, BorderLayout.NORTH);
        parent.add(actionPanel, BorderLayout.SOUTH);
        parent.revalidate();
        parent.setVisible(true);
        parent.repaint();
    }

    /**
     * Defines how many action button objects will be created and cached for usage if needed. Less is better, but
     * should not be smaller than the number of actions available to players in any game state.
     *
     * @return maximum size of the action space (maximum actions available to a player for any decision point in the game)
     */
    @Override
    public int getMaxActionSpace() {
        // TODO
        return 10;
    }

    /**
     * Updates all GUI elements given current game state and player that is currently acting.
     *
     * @param player    - current player acting.
     * @param gameState - current game state to be used in updating visuals.
     */
    @Override
    protected void _update(AbstractPlayer player, AbstractGameState gameState) {
        // TODO
    }
}
