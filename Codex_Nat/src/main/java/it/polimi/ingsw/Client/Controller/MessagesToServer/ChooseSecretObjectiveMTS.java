package it.polimi.ingsw.Client.Controller.MessagesToServer;

import it.polimi.ingsw.Server.Controller.Executor.ChooseSecretObjectiveMTSExecutor;

/**
 * Represents a message sent from the client to choose a secret objective card.
 * Extends {@link MessageToServer}.
 */
public class ChooseSecretObjectiveMTS extends MessageToServer {

    String cardID;

    /**
     * Constructs an instance of ChooseSecretObjectiveMTS with the specified game ID, player nickname, and card ID.
     *
     * @param gameID The ID of the game in which the secret objective is chosen.
     * @param player The nickname of the player choosing the secret objective card.
     * @param cardID The ID of the secret objective card being chosen.
     */
    public ChooseSecretObjectiveMTS(int gameID, String player, String cardID)
    {
        this.gameID=gameID;
        this.username =player;
        this.cardID = cardID;
    }

    /**
     * Executes the update by calling {@link ChooseSecretObjectiveMTSExecutor#execute(ChooseSecretObjectiveMTS)}.
     * This method is typically called when the message needs to be processed.
     */
    public void update(){
        ChooseSecretObjectiveMTSExecutor.execute(this);
    }

    /**
     * Retrieves the ID of the secret objective card being chosen.
     *
     * @return The ID of the secret objective card.
     */
    public String getCardID() {
       return cardID;
     }
}
