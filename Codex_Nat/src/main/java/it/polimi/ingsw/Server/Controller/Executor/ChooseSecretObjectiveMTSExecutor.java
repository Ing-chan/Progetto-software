package it.polimi.ingsw.Server.Controller.Executor;

import it.polimi.ingsw.Client.Controller.MessagesToServer.ChooseSecretObjectiveMTS;
import it.polimi.ingsw.Server.Controller.GamesManager;
import it.polimi.ingsw.Server.Controller.MessagesToClient.okayMTC;
import it.polimi.ingsw.Server.Model.GameModel;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Server;

import java.util.logging.Logger;

public class ChooseSecretObjectiveMTSExecutor implements MTSExecutor {

    public static void execute(ChooseSecretObjectiveMTS chooseSecretObjectiveMTS) {
        Logger logger = LoggerUtility.getLogger();
        GameModel game = GamesManager.getInstance().getGameController(chooseSecretObjectiveMTS.getGameID()).getGameModel();
        String player = chooseSecretObjectiveMTS.getNickname();
        String chosenCard = chooseSecretObjectiveMTS.getCardID();

        logger.info( "Game: "+chooseSecretObjectiveMTS.getGameID() +" Player " + chooseSecretObjectiveMTS.getNickname() + " chose objective " + chooseSecretObjectiveMTS.getCardID());
        Server.getHandle(player).sendMessage(new okayMTC());
        game.setSecretObjectives(player,chosenCard);
    }

}
