package games.marblesmultiverse.gui;

import core.AbstractGameState;
import core.AbstractPlayer;
import core.CoreConstants;
import core.Game;
import core.actions.AbstractAction;
import games.marblesmultiverse.MMGameState;
import games.marblesmultiverse.actions.DirectionalAction;
import games.marblesmultiverse.components.Card;
import gui.AbstractGUIManager;
import gui.GamePanel;
import gui.IScreenHighlight;
import players.human.ActionController;
import utilities.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.util.List;
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

    MMBoardView boardView;

    public MMGUIManager(GamePanel parent, Game game, ActionController ac, Set<Integer> human) {
        super(parent, game, ac, human);
        MMGameState gameState = (MMGameState) game.getGameState();

        // Main Game area
        JPanel mainGameArea = new JPanel(new FlowLayout());

        boardView = new MMBoardView(gameState);

        // Rule Panel to display current rules
        JPanel rulePanel = new JPanel(new GridLayout(3, 2));
        for (Card card: gameState.getRulesInPlay().values()) {
            //JButton rule = new JButton("Rule " + i);
            MMCardView rule = new MMCardView(card);
            rulePanel.add(rule);
        }

        mainGameArea.add(boardView);
        mainGameArea.add(rulePanel);

        width = boardView.getPreferredSize().width + MMCardView.cardWidth*2;

        JPanel infoPanel = createGameStateInfoPanel("Multiverse Marbles", gameState, width, defaultInfoPanelHeight);
        JComponent actionPanel = createActionPanel(new IScreenHighlight[]{boardView}, width, defaultActionPanelHeight/2, false, true, null, null, null);

        parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
        parent.add(infoPanel);
        parent.add(mainGameArea);
        parent.add(actionPanel);
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
        return 10000;
    }

    @Override
    protected void updateActionButtons(AbstractPlayer player, AbstractGameState gameState) {
        if (gameState.getGameStatus() == CoreConstants.GameResult.GAME_ONGOING && !(actionButtons == null)) {
            List<AbstractAction> actions = player.getForwardModel().computeAvailableActions(gameState, gameState.getCoreGameParameters().actionSpace);
            for (int i = 0; i < actions.size() && i < maxActionSpace; i++) {
                DirectionalAction action = (DirectionalAction) actions.get(i);
                Vector2D from = action.from();
                Vector2D to = action.to();
                boolean show = boardView.getHighlights().isEmpty() ||
                        boardView.getHighlights().size() == 1 && boardView.isHighlighted(from, 0) ||
                        boardView.getHighlights().size() == 2 && boardView.isHighlighted(from, 0) && boardView.isHighlighted(to, 1);
                if (show) {
                    actionButtons[i].setVisible(true);
                    actionButtons[i].setButtonAction(actions.get(i), gameState);
                    actionButtons[i].setBackground(Color.white);
                } else {
                    actionButtons[i].setVisible(false);
                    actionButtons[i].setButtonAction(null, "");
                }
            }
            for (int i = actions.size(); i < actionButtons.length; i++) {
                actionButtons[i].setVisible(false);
                actionButtons[i].setButtonAction(null, "");
            }
        }
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
