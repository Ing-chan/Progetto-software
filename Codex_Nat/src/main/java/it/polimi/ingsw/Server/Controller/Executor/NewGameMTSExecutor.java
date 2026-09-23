package it.polimi.ingsw.Server.Controller.Executor;

import it.polimi.ingsw.Client.Controller.MessagesToServer.NewGameMTS;
import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.GamesManager;


public class NewGameMTSExecutor implements MTSExecutor {

    public static void execute(NewGameMTS newGameMTS) {
        GameController gameController = new GameController(newGameMTS.getPlayerNumber());
        gameController.addPlayer(newGameMTS.getUsername());
        GamesManager.getInstance().putGame(gameController.getID(), gameController);
        GamesManager.getInstance().registerClientRoom(gameController.getID(),newGameMTS.getUsername());
    }
}


