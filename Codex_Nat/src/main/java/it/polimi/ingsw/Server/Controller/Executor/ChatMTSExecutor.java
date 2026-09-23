package it.polimi.ingsw.Server.Controller.Executor;


import it.polimi.ingsw.Client.Controller.MessagesToServer.ChatMTS;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.GamesManager;
import it.polimi.ingsw.Server.Model.Chat.ChatMessage;

import java.util.logging.Logger;

public class ChatMTSExecutor implements MTSExecutor {

    public static void execute(ChatMTS chatMTS) {

        Logger logger = LoggerUtility.getLogger();
        GameController cli = GamesManager.getInstance().getGameController(chatMTS.getGameID());

        String message = chatMTS.getMessage();
        String sender = chatMTS.getNickname();
        String receiver = chatMTS.getReceiver();
        boolean broadcast = chatMTS.isBroadcast();


        cli.addMessage(new ChatMessage(message, sender, receiver, broadcast));
        logger.info("added message " + message + " from " + sender + " to " + (broadcast?"everyone":receiver));
    }
}
