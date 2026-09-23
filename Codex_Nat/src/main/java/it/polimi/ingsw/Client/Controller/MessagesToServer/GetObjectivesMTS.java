package it.polimi.ingsw.Client.Controller.MessagesToServer;

import it.polimi.ingsw.Server.Controller.Executor.GetObjectivesMTSExecutor;

/**
 * Represents a message sent from the client to request objectives for a specific game.
 * Extends {@link MessageToServer}.
 */
public class GetObjectivesMTS extends MessageToServer {
    public GetObjectivesMTS(int gameID, String username) {
        this.gameID=gameID;
        this.username =username;
    }

    /**
     * Executes the update by calling {@link GetObjectivesMTSExecutor#execute(GetObjectivesMTS)}.
     * This method is called when the message needs to be processed.
     */
    @Override
    public void update() {
        GetObjectivesMTSExecutor.execute(this);
    }
}
