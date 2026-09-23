package it.polimi.ingsw.Client.Controller.MessagesToServer;

import it.polimi.ingsw.Server.Controller.Executor.CanIPlayMTSExecutor;

/**
 * Represents a message sent from the client to check if a player can join a game.
 * Extends {@link MessageToServer}.
 */
public class CanIPlayMTS extends MessageToServer {

    /**
     * Represents a message sent from the client to check if a player can join a game.
     * Extends {@link MessageToServer}.
     */
    public CanIPlayMTS(int gameID, String player)
    {
        this.gameID=gameID;
        this.username =player;
    }

    /**
     * Executes the update by calling {@link CanIPlayMTSExecutor#execute(CanIPlayMTS)}.
     * This method is typically called when the message needs to be processed.
     */
    public void update(){
        CanIPlayMTSExecutor.execute(this);
    }

    /**
     * Retrieves the nickname of the player requesting to join the game.
     *
     * @return The nickname of the player.
     */
    public String getUsername() {
        return username;
    }
}
