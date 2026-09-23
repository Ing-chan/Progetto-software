package it.polimi.ingsw.Client.Controller.Executor;

import it.polimi.ingsw.Network.Virtuals.VirtualView;
import it.polimi.ingsw.Server.Controller.MessagesToClient.ViewUtilMTC;

/**
 * Executor for handling ViewUtilMTC messages to update the VirtualView.
 */
public class UpdateVirtualViewExecutor implements MTCExecutor {

    /**
     * Executes the given ViewUtilMTC message by updating the VirtualView.
     *
     * @param viewUtilMTC the ViewUtilMTC message to be executed
     */
    public static void execute(ViewUtilMTC viewUtilMTC) {

        VirtualView virtualView = VirtualView.getInstance();

        virtualView.setPlayers(viewUtilMTC.getPlayers());
        virtualView.setVirtualDrawableCards(viewUtilMTC.getDrawableCards());
        virtualView.setVirtualScoreBoard(viewUtilMTC.getVirtualScoreBoard());
        virtualView.setVirtualPlayer(viewUtilMTC.getPlayer());
        virtualView.setActivePlayer(viewUtilMTC.getActivePlayer());
        virtualView.setLastTurnsSet(viewUtilMTC.isLastTurnsSet());
        virtualView.setLastTurn(viewUtilMTC.getLastTurn());
        virtualView.setMessages(viewUtilMTC.getMessages());
        virtualView.setTurnPhase(viewUtilMTC.getTurnPhase());
        virtualView.notifyObservers();
    }
}
