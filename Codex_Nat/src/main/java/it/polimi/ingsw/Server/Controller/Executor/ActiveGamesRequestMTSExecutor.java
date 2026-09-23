package it.polimi.ingsw.Server.Controller.Executor;

import it.polimi.ingsw.Client.Controller.MessagesToServer.ActiveGamesRequestMTS;
import it.polimi.ingsw.Server.Controller.GamesManager;

public class ActiveGamesRequestMTSExecutor implements MTSExecutor{

    public static void execute(ActiveGamesRequestMTS activeGamesRequestMTS) {

        GamesManager.getInstance().notifyGames(activeGamesRequestMTS.getNickname(), activeGamesRequestMTS.getPlayerNumber());

    }


}
