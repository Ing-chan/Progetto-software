package it.polimi.ingsw.Client.Controller.PlayablePhases;


import it.polimi.ingsw.Client.Controller.GameCommand;
import it.polimi.ingsw.Client.View.CLI.Printer;
import it.polimi.ingsw.Network.Server.LoggerUtility;

import java.util.logging.Logger;

/**
 * Represents the state where the client acknowledges a player disconnection.
 * It logs the disconnection event using a {@link Logger} instance obtained
 * from {@link LoggerUtility#getLogger()}.
 */
public class DisconnectionState {

    private static final Logger logger = LoggerUtility.getLogger();

    /**
     * Executes the behavior associated with the DisconnectionState.
     * It logs the disconnection event using a logger, indicating that
     * a player has disconnected and the game has ended.
     */
    public static void execute() {
        logger.warning("A player has disconnected! Game ended!");
        GameCommand.killReader();
        System.exit(0);
    }
}

