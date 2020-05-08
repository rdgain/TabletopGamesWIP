package games.explodingkittens;

import actions.IAction;
import components.Deck;
import components.IDeck;
import components.PartialObservableDeck;
import core.AbstractGameState;
import gamestates.PlayerResult;
import games.explodingkittens.cards.ExplodingKittenCard;
import games.explodingkittens.actions.*;
import observations.Observation;
import players.AbstractPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ExplodingKittensGameState extends AbstractGameState {

    public List<Deck<ExplodingKittenCard>> playerHandCards;
    public PartialObservableDeck<ExplodingKittenCard> drawPile;
    public Deck<ExplodingKittenCard> discardPile;
    public int playerGettingAFavor = -1;

    public boolean[] isPlayerAlive;
    public int nPlayersActive;

    public ExplodingKittensGamePhase gamePhase = ExplodingKittensGamePhase.PlayerMove;

    public ExplodingKittensGameState(ExplodingKittenParameters gameParameters) {
        super(gameParameters);
        setComponents(gameParameters);
    }

    public void killPlayer(int playerID){
        isPlayerAlive[playerID] = false;
        nPlayersActive -= 1;
        if (nPlayersActive == 1) {
            endGame();
        }
    }

    public void setComponents(ExplodingKittenParameters gameParameters) {
        isPlayerAlive = new boolean[getNPlayers()];
        for (int i = 0; i < getNPlayers(); i++) isPlayerAlive[i] = true;
        nPlayersActive = getNPlayers();

        boolean[] visibility = new boolean[getNPlayers()];
        Arrays.fill(visibility, true);
        drawPile = new PartialObservableDeck<>(visibility);
        // add all cards and distribute 7 random cards to each player
        for (HashMap.Entry<ExplodingKittenCard.CardType, Integer> entry : gameParameters.cardCounts.entrySet()) {
            if (entry.getKey() == ExplodingKittenCard.CardType.DEFUSE || entry.getKey() == ExplodingKittenCard.CardType.EXPLODING_KITTEN)
                continue;
            for (int i = 0; i < entry.getValue(); i++) {
                ExplodingKittenCard card = new ExplodingKittenCard(entry.getKey());
                drawPile.add(card);
            }
        }
        drawPile.shuffle();

        // For each player, initialize their own areas: they get a player hand and a player card
        // give each player a defuse card and seven random cards from the deck
        playerHandCards = new ArrayList<>(getNPlayers());
        for (int i = 0; i < getNPlayers(); i++) {
            String deckname = "Player" + i + "HandCards";
            Deck<ExplodingKittenCard> playerCards = new Deck<>(deckname);
            playerHandCards.add(playerCards);

            //add defuse card
            ExplodingKittenCard defuse =  new ExplodingKittenCard(ExplodingKittenCard.CardType.DEFUSE);
            playerCards.add(defuse);

            // add 7 random cards from the deck
            for (int j = 0; j < 7; j++)
                playerCards.add(drawPile.draw());
        }

        // add remaining defuse cards and exploding kitten cards to the deck and shuffle again
        for (int i = getNPlayers(); i < 6; i++){
            ExplodingKittenCard defuse =  new ExplodingKittenCard(ExplodingKittenCard.CardType.DEFUSE);
            drawPile.add(defuse);
        }
        for (int i = 0; i < getNPlayers()-1; i++){
            ExplodingKittenCard explodingKitten = new ExplodingKittenCard(ExplodingKittenCard.CardType.EXPLODING_KITTEN);
            drawPile.add(explodingKitten);
        }
        drawPile.shuffle();

        // setup discardPile area
        discardPile = new Deck<>();
    }

    private ArrayList<IAction> defuseActions(int playerID){
        ArrayList<IAction> actions = new ArrayList<>();
        Deck<ExplodingKittenCard> playerDeck = playerHandCards.get(playerID);
        ExplodingKittenCard kitten = playerDeck.peek();
        for (int i = 0; i <= drawPile.getCards().size(); i++){
            actions.add(new PlaceExplodingKittenAction<>(kitten, playerDeck, drawPile, i));
        }
        return actions;
    }

    private ArrayList<IAction> nopeActions(int playerID){
        ArrayList<IAction> actions = new ArrayList<>();
        Deck<ExplodingKittenCard> playerDeck = playerHandCards.get(playerID);
        for (ExplodingKittenCard card : playerDeck.getCards()) {
            if (card.cardType == ExplodingKittenCard.CardType.NOPE) {
                actions.add(new NopeAction<>(card, playerDeck, discardPile, playerID));
            }
            break;
        }
        actions.add(new PassAction(playerID));
        return actions;
    }

    private ArrayList<IAction> favorActions(int playerID){
        ArrayList<IAction> actions = new ArrayList<>();
        Deck<ExplodingKittenCard> playerDeck = playerHandCards.get(playerID);
        Deck<ExplodingKittenCard> receiverDeck = playerHandCards.get(playerGettingAFavor);
        for (ExplodingKittenCard card : playerDeck.getCards()) {
            actions.add(new GiveCardAction(card, playerDeck, receiverDeck));
        }
        return actions;
    }

    private ArrayList<IAction> playerActions(int playerID){
        ArrayList<IAction> actions = new ArrayList<>();
        IDeck<ExplodingKittenCard> playerDeck = playerHandCards.get(playerID);

        // todo: only add unique actions
        for (ExplodingKittenCard card : playerDeck.getCards()) {
            switch (card.cardType) {
                case DEFUSE:
                case MELONCAT:
                case RAINBOWCAT:
                case FURRYCAT:
                case BEARDCAT:
                case TACOCAT:
                case NOPE:
                case EXPLODING_KITTEN:
                    break;
                case SKIP:
                    actions.add(new SkipAction<>(card, playerDeck, discardPile));
                    break;
                case FAVOR:
                    for (int player = 0; player < getNPlayers(); player++) {
                        if (player == playerID)
                            continue;
                        if (playerHandCards.get(player).getCards().size() > 0)
                            actions.add(new FavorAction<>(card, playerDeck, discardPile, player, playerID));
                    }
                    break;
                case ATTACK:
                    for (int targetPlayer = 0; targetPlayer < getNPlayers(); targetPlayer++) {

                        if (targetPlayer == playerID || !isPlayerAlive[targetPlayer])
                            continue;

                        actions.add(new AttackAction<>(card, playerDeck, discardPile, targetPlayer));
                    }
                    break;
                case SHUFFLE:
                    actions.add(new ShuffleAction<>(card, playerDeck, discardPile, drawPile));
                    break;
                case SEETHEFUTURE:
                    actions.add(new SeeTheFutureAction<>(card, playerDeck, discardPile, playerID, drawPile));
                    break;
                default:
                    System.out.println("No actions known for cardtype: " + card.cardType.toString());
            }
        }
        /* todo add special combos
        // can take any card from anyone
        for (int i = 0; i < nPlayers; i++){
            if (i != activePlayer){
                Deck otherDeck = (Deck)this.areas.get(activePlayer).getComponent(playerHandHash);
                for (Card card: otherDeck.getCards()){
                    actions.add(new TakeCard(card, i));
                }
            }
        }*/

        // add end turn by drawing a card
        actions.add(new DrawExplodingKittenCard(playerID, drawPile, playerDeck));
        return actions;
    }

    public void print(ExplodingKittenTurnOrder turnOrder) {
        System.out.println("Exploding Kittens Game-State");
        System.out.println("============================");

        int currentPlayer = turnOrder.getCurrentPlayerIndex(this);

        for (int i = 0; i < getNPlayers(); i++){
            if (currentPlayer == i)
                System.out.print(">>> Player " + i + ":");
            else
                System.out.print("Player " + i + ":");
            printDeck(playerHandCards.get(i));
        }

        System.out.print("DrawPile" + ":");
        printDeck(drawPile);

        System.out.print("DiscardPile" + ":");
        printDeck(discardPile);

        System.out.println("Current GamePhase: " + gamePhase);
        System.out.println("Missing Draws: " + turnOrder.requiredDraws);
    }

    public void printDeck(IDeck<ExplodingKittenCard> deck){
        StringBuilder sb = new StringBuilder();
        for (ExplodingKittenCard card : deck.getCards()){
            sb.append(card.cardType.toString());
            sb.append(",");
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length()-1);
        System.out.println(sb.toString());
        //System.out.println();
    }

    public boolean isGameOver(){
        return terminalState;
    }

    @Override
    public Observation getObservation(AbstractPlayer player) {
        return null;
    }

    @Override
    public List<IAction> getActions(AbstractPlayer player) {
        ArrayList<IAction> actions;
        // todo the actions per player do not change a lot in between two turns
        // i would strongly recommend to update an existing list instead of generating a new list everytime we query this function
        switch (gamePhase){
            case PlayerMove:
                actions = playerActions(player.playerID);
                break;
            case DefusePhase:
                actions = defuseActions(player.playerID);
                break;
            case NopePhase:
                actions = nopeActions(player.playerID);
                break;
            case FavorPhase:
                actions = favorActions(player.playerID);
                break;
            default:
                actions = new ArrayList<>();
                break;
        }

        return actions;
    }

    @Override
    public void endGame() {
        this.terminalState = true;
        for (int i = 0; i < getNPlayers(); i++){
            playerResults[i] = isPlayerAlive[i] ? PlayerResult.Winner : PlayerResult.Loser;
        }
    }
}