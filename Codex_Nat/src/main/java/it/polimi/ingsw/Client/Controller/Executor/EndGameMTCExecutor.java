package it.polimi.ingsw.Client.Controller.Executor;

import it.polimi.ingsw.Client.Controller.PlayablePhases.FinalState;
import it.polimi.ingsw.Network.Virtuals.VirtualView;
import it.polimi.ingsw.Server.Controller.MessagesToClient.EndGameMTC;

/**
 * Executor for handling the EndGameMTC message.
 * This class is responsible for executing the final state transition when the end game message is received.
 */
public class EndGameMTCExecutor implements MTCExecutor{

    /**
     * Executes the final state transition upon receiving the EndGameMTC message.
     *
     * @param endGameMTC The message indicating the end of the game.
     */
    public static void execute(EndGameMTC endGameMTC) {

        if(VirtualView.getInstance().getView().equals("-cli")) {
            FinalState.execute();
        }else{
            //GUI
        }
    }
}
