package games.marblesmultiverse.components;

import core.components.Card;

public class MMCard extends Card {
    Cards type;

    protected MMCard(Cards type){
        super(type.name());
        this.type=type;
    }

    CardType getCardType(){
        return type.type;
    }
}
