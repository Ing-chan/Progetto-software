package it.polimi.ingsw.Client.Controller.MessagesToServer;

import it.polimi.ingsw.Server.Controller.Executor.ViewUtilitiesMTSExecutor;

/**
 * Message sent from the client to the server to request view-related utilities.
 * This message contains the game ID and the user's nickname.
 */
public class ViewUtilitiesMTS extends MessageToServer {

    /**
     * Constructs a view utilities message to send to the server.
     *
     * @param gameID The ID of the game.
     * @param username The nickname of the user sending the message.
     */
    public ViewUtilitiesMTS(int gameID, String username) {
        this.gameID = gameID;
        this.username = username;
    }

    /**
     * Updates the server with the request to provide view-related utilities.
     */
    @Override
    public void update() {
        ViewUtilitiesMTSExecutor.execute(this);
    }
}