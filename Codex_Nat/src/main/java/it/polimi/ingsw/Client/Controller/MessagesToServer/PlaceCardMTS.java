package it.polimi.ingsw.Client.Controller.MessagesToServer;

import it.polimi.ingsw.Server.Controller.Executor.PlaceCardMTSExecutor;

import java.awt.*;

/**
 * Message sent from the client to the server to place a card on the board.
 * This message contains the game ID, the player's nickname, the card ID, the coordinates where the card is to be placed, and the face of the card to be used.
 */
public class PlaceCardMTS extends MessageToServer {

    String cardID;
    Point coords;
    boolean selectedFace;

    /**
     * Constructs a place card message to send to the server.
     *
     * @param gameid The ID of the game.
     * @param player The nickname of the player sending the message.
     * @param cardID The ID of the card to be placed.
     * @param coords The coordinates where the card is to be placed.
     * @param selectedFace The face of the card to be used.
     */
    public PlaceCardMTS(int gameid, String player, String cardID,Point coords, boolean selectedFace)
    {
      this.gameID=gameid;
      this.username =player;
      this.cardID=cardID;
      this.coords=coords;
      this.selectedFace=selectedFace;
    }

    /**
     * Updates the server with the place card request.
     */
    public void update(){
      PlaceCardMTSExecutor.execute(this);
    }

    /**
     * Updates the server with the place card request.
     */
    public String getCardID() {
        return cardID;
    }

    /**
     * Gets the coordinates where the card is to be placed.
     *
     * @return The coordinates.
     */
    public Point getCoords() {
        return coords;
    }

    /**
     * Gets the face of the card to be used.
     *
     * @return The face of the card.
     */
    public boolean getSelectedFace() {
        return selectedFace;
    }
}
