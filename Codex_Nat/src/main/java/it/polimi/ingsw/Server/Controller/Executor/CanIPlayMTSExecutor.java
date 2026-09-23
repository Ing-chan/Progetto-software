package it.polimi.ingsw.Server.Controller.Executor;

import it.polimi.ingsw.Client.Controller.MessagesToServer.CanIPlayMTS;
import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.GamesManager;
import it.polimi.ingsw.Server.Controller.MessagesToClient.notOkayMTC;
import it.polimi.ingsw.Server.Controller.MessagesToClient.okayMTC;
import it.polimi.ingsw.Server.Server;

public class CanIPlayMTSExecutor implements MTSExecutor {


    public static void execute(CanIPlayMTS canIPlayMTS) {
        //if gameID is 0 then the user picked random. This is handled inside the Controller

            GameController gameController = GamesManager.getInstance().getGameController(canIPlayMTS.getGameID());
            String player = canIPlayMTS.getUsername();

            if (!gameController.gameIsFull()) {
                Server.getHandle(canIPlayMTS.getUsername()).sendMessage(new okayMTC());
                gameController.addPlayer(player);
                GamesManager.getInstance().registerClientRoom(canIPlayMTS.getGameID(),player);
            }else{
                Server.getHandle(canIPlayMTS.getUsername()).sendMessage(new notOkayMTC("Game room is full!"));
            }
        }
    }

