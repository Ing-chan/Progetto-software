package it.polimi.ingsw.Client.Controller.MessagesToServer;

import it.polimi.ingsw.Server.Controller.Executor.DrawCardMTSExecutor;

/**
 * Retrieves the ID of the secret objective card being chosen.
 *
 * @return The ID of the secret objective card.
 */
public class DrawCardMTS extends MessageToServer {
    String cardID;

    /**
     * Constructs an instance of DrawCardMTS with the specified game ID, player nickname, and card ID.
     *
     * @param gameid The ID of the game in which the card is drawn.
     * @param player The nickname of the player drawing the card.
     * @param cardID The ID of the card being drawn.
     */
    public DrawCardMTS(int gameid, String player, String cardID) {
        this.gameID = gameid;
        this.username = player;
        this.cardID = cardID;
    }

    /**
     * Executes the update by calling {@link DrawCardMTSExecutor#execute(DrawCardMTS)}.
     * This method is called when the message needs to be processed.
     */
    public void update() {
        DrawCardMTSExecutor.execute(this);
    }

    /**
     * Retrieves the ID of the card being drawn.
     *
     * @return The ID of the card being drawn.
     */
    public String getCardID() {
        return cardID;
    }
}
