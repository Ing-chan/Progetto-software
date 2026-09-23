package it.polimi.ingsw.Network.Virtuals;

import java.io.Serializable;
import java.util.ArrayList;

public class VirtualDrawableCards implements Serializable {
    ArrayList<VirtualCard> drawableCards;
    int remainingCards;



    public VirtualDrawableCards(ArrayList<VirtualCard> drawableCards,int remainingCards) {
        this.drawableCards = drawableCards;
        this.remainingCards = remainingCards;
    }

    public ArrayList<VirtualCard> getDrawablaCards() {
        return drawableCards;
    }

    public void setDrawableCards(ArrayList<VirtualCard> drawableCards) {
        this.drawableCards = drawableCards;
    }

    public void setRemainingCards(int remainingCards) {
        this.remainingCards = remainingCards;
    }

    public int getRemainingCards() {
        return remainingCards;
    }
}
