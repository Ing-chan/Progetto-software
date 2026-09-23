package it.polimi.ingsw.Network.Virtuals;

import it.polimi.ingsw.Server.Model.Enums.CardColour;
import it.polimi.ingsw.Server.Model.Enums.CardType;

import java.io.Serializable;
import java.util.ArrayList;

public class VirtualCard implements Serializable {
    String ID;
    String descriptionFront;
    String descriptionBack;
    Boolean face;
    ArrayList<String> printFront;
    ArrayList<String> printBack;
    CardColour cardColor;
    CardType cardType;

    public VirtualCard(String ID, Boolean face, CardColour cardColor, CardType cardType, ArrayList<String> printFront, ArrayList<String> printBack) {
        this.ID = ID;
        if (printFront == null || printFront.size() <= 1) {
            this.printFront = printFront;
        } else {
            this.printFront = new ArrayList<>(printFront.subList(0, printFront.size() - 1));
        }

        if (printBack == null || printBack.size() <= 1) {
            this.printBack = printBack;
        } else {
            this.printBack = new ArrayList<>(printBack.subList(0, printBack.size() - 1));
        }

        this.face = face;
        this.descriptionFront = printFront.getLast();
        this.descriptionBack = printBack.getLast();
        this.cardColor = cardColor;
        this.cardType = cardType;
    }

    public String getID() {
        return ID;
    }

    public CardColour getCardColor() {
        return cardColor;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getDescription() {
        if (face) {
            return descriptionFront;
        }
        return descriptionBack;
    }

    public Boolean getFace() {
        return face;
    }

    public ArrayList<String> getPrint() {
        if (face) {
            return printFront;
        }
        return printBack;
    }

    public void flip() {
        face = !face;
    }
}
