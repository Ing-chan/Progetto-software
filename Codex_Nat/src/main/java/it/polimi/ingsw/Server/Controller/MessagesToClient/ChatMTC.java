package it.polimi.ingsw.Server.Controller.MessagesToClient;


import it.polimi.ingsw.Client.Controller.Executor.ChatMTCExecutor;

import java.util.ArrayList;

public class ChatMTC extends MessageToClient{
    ArrayList<String> messages;
    public ChatMTC(ArrayList<String> messages) {
type = MTCtype.CHAT;

        this.messages = messages;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    @Override
    public void update() {
        ChatMTCExecutor.execute(this);
    }
}
