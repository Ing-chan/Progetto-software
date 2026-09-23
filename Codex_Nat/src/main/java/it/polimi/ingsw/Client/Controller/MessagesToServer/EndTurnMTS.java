package it.polimi.ingsw.Client.Controller.MessagesToServer;

import it.polimi.ingsw.Server.Controller.Executor.EndTurnMTSExecutor;

/**
 * Retrieves the ID of the card being drawn.
 *
 * @return The ID of the card being drawn.
 */
public class EndTurnMTS extends MessageToServer {

    /**
     * Constructs an instance of EndTurnMTS with the specified game ID and player nickname.
     *
     * @param gameID   The ID of the game where the turn ends.
     * @param username The nickname of the player ending their turn.
     */
    public EndTurnMTS(int gameID,String username){
        this.username = username;
        this.gameID = gameID;
    };

    /**
     * Executes the update by calling {@link EndTurnMTSExecutor#execute(EndTurnMTS)}.
     * This method is called when the message needs to be processed.
     */
    public void update(){
        EndTurnMTSExecutor.execute(this);
    }

}
