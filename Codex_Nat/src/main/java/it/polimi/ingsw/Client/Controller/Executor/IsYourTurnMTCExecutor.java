package it.polimi.ingsw.Client.Controller.Executor;

import it.polimi.ingsw.Client.Controller.PlayablePhases.DrawCardState;
import it.polimi.ingsw.Client.Controller.PlayablePhases.PlaceCardState;
import it.polimi.ingsw.Network.Virtuals.VirtualView;

/**
 * Executor for handling the transition to the player's turn.
 * This class is responsible for executing the actions related to placing and drawing cards when it is the player's turn.
 */
public class IsYourTurnMTCExecutor implements MTCExecutor{

    /**
     * Executes the actions for the player's turn.
     * This method transitions the game to the PlaceCardState and then to the DrawCardState.
     */
    public static void execute()
    {
        if(VirtualView.getInstance().getView().equals("-cli")) {
            PlaceCardState.execute();
            DrawCardState.execute();
        }
    }
}
