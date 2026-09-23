package it.polimi.ingsw.Server.Controller.Executor;

import it.polimi.ingsw.Client.Controller.MessagesToServer.GetObjectivesMTS;
import it.polimi.ingsw.Server.Controller.GamesManager;
import it.polimi.ingsw.Server.Controller.MessagesToClient.ObjectiveMTC;
import it.polimi.ingsw.Server.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.Server.Server;

import java.util.ArrayList;

public class GetObjectivesMTSExecutor implements MTSExecutor {
    public static void execute(GetObjectivesMTS getObjectivesMTS) {

        ArrayList<ObjectiveCard> objectives = GamesManager.getInstance().getGameController(getObjectivesMTS.getGameID()).getGameModel().getTwoObjectives();

        Server.getHandle(getObjectivesMTS.getNickname()).sendMessage(new ObjectiveMTC(objectives));
    }
}
