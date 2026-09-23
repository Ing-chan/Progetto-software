package it.polimi.ingsw.Client.Controller.Executor;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Server.Controller.MessagesToClient.ChatMTC;

/**
 * Executor for handling ChatMTC messages.
 */
public class ChatMTCExecutor implements MTCExecutor{

    /**
     * Executes the given ChatMTC message by updating the ClientController.
     *
     * @param chatMTC the ChatMTC message to be executed
     */
    public static void execute(ChatMTC chatMTC){
        ClientController.getInstance().update();
    }
}
