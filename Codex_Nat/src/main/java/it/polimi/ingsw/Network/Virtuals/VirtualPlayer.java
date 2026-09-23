package it.polimi.ingsw.Network.Virtuals;

import java.io.Serializable;
import java.util.ArrayList;

public class VirtualPlayer implements Serializable {
    String username;
    ArrayList<VirtualCard> playerCards;
    ArrayList<VirtualObjective> playerObject;
    VirtualBoard virtualBoard;
    VirtualInventory virtualInventory;

    public VirtualPlayer(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public VirtualBoard getVirtualBoard() {
        return virtualBoard;
    }

    public VirtualInventory getVirtualInventory() {
        return virtualInventory;
    }

    public void setVirtualBoard(VirtualBoard virtualBoard) {
        this.virtualBoard = virtualBoard;
    }

    public void setVirtualInventory(VirtualInventory virtualInventory) {
        this.virtualInventory = virtualInventory;
    }

    public void setPlayerCards(ArrayList<VirtualCard> playerCards){
        this.playerCards=playerCards;
    }

    public ArrayList<VirtualObjective> getPlayerObject() {
        return playerObject;
    }

    public void setPlayerObject(ArrayList<VirtualObjective> virtualCards) {
        this.playerObject = virtualCards;
    }

    public ArrayList<VirtualCard> getPlayerCards()
    {
        return playerCards;
    };

    public VirtualCard findCard(String cardID) {
        for(VirtualCard card: playerCards){
            if(card.getID().equals(cardID))
            {
                return card;
            }
        }
            //TODO togliere e migliorare
            return null;

    }
}
