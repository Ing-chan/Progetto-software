package it.polimi.ingsw.Server.Controller.Executor;

import it.polimi.ingsw.Client.Controller.MessagesToServer.EndTurnMTS;
import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.GamesManager;
import it.polimi.ingsw.Server.Controller.MessagesToClient.okayMTC;
import it.polimi.ingsw.Server.Controller.TurnHandler;
import it.polimi.ingsw.Server.Model.Enums.TurnPhase;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Server;

import java.util.logging.Logger;

public class EndTurnMTSExecutor implements MTSExecutor {

    public static void execute(EndTurnMTS endTurnMTS) {
        final Logger logger = LoggerUtility.getLogger();

        String player = endTurnMTS.getNickname();
        TurnHandler turnHandler = GamesManager.getInstance().getGameController(endTurnMTS.getGameID()).getTurnHandler();

        //we double-check, for robustness of the software
        if (turnHandler.getActivePlayer().equals(player) && turnHandler.getTurnPhase() == TurnPhase.hasToEndTurn) {
            //we log only if the message is valid
            logger.finer(endTurnMTS.getNickname() + " ends his turn");

            GameController gameController = GamesManager.getInstance().getGameController(endTurnMTS.getGameID());

            Server.getHandle(endTurnMTS.getNickname()).sendMessage(new okayMTC());

            //checks whether the conditions to go in the final game phase are met.
            gameController.update();


        } else {
            logger.severe(endTurnMTS.getNickname() + " tried to end his turn but it wasn't his turn");
        }

    }
}
