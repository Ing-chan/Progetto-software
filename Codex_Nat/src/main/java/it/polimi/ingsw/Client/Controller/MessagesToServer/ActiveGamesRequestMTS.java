package it.polimi.ingsw.Client.Controller.MessagesToServer;

import it.polimi.ingsw.Server.Controller.Executor.ActiveGamesRequestMTSExecutor;

/**
 * Represents a message sent from the client to request active games from the server.
 * Extends {@link MessageToServer}.
 */
public class ActiveGamesRequestMTS extends MessageToServer {

    int playerNumber; // Number of players requested for the game

    /**
     * Constructs an instance of ActiveGamesRequestMTS with the specified username and player number.
     *
     * @param username     The username of the client requesting active games.
     * @param playerNumber The number of players requested for the game.
     */
    public ActiveGamesRequestMTS(String username,int playerNumber) {
        this.username = username;
        this.playerNumber=playerNumber;
    }

    /**
     * Executes the update by calling {@link ActiveGamesRequestMTSExecutor#execute(ActiveGamesRequestMTS)}.
     * This method is typically called when the message needs to be processed.
     */
    @Override
    public void update() {
        ActiveGamesRequestMTSExecutor.execute(this);
    }

    /**
     * Retrieves the number of players requested for the game.
     *
     * @return The number of players requested.
     */
    public int getPlayerNumber()
    {
        return playerNumber;
    }
}
