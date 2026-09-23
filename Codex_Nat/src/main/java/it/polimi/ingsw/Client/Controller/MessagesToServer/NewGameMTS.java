package it.polimi.ingsw.Client.Controller.MessagesToServer;

import it.polimi.ingsw.Server.Controller.Executor.NewGameMTSExecutor;

/**
 * Message sent from the client to the server to initiate a new game.
 * This message contains the player's nickname and the number of players for the new game.
 */
public class NewGameMTS extends MessageToServer{
    int playerNumber;

    /**
     * Constructs a new game message to send to the server.
     *
     * @param player The nickname of the player sending the message.
     * @param playerNumber The number of players for the new game.
     */
    public NewGameMTS(String player, int playerNumber)
    {
        this.username =player;
        this.playerNumber=playerNumber;
    }

    /**
     * Updates the server with the new game request by executing the corresponding executor.
     */
    public void update(){
        NewGameMTSExecutor.execute(this);
    }

    /**
     * Gets the number of players for the new game.
     *
     * @return The number of players.
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Gets the nickname of the player sending the message.
     *
     * @return The player's nickname.
     */
    public String getUsername() {
        return username;
    }
}
