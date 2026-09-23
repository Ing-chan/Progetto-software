package it.polimi.ingsw.Server.Controller.GameObservers;

import it.polimi.ingsw.Network.Server.Connections.ServerConnectionHandler;
import it.polimi.ingsw.Server.Controller.MessagesToClient.ChatMTC;
import it.polimi.ingsw.Server.Controller.TurnHandler;
import it.polimi.ingsw.Server.Model.Chat.Chat;
import it.polimi.ingsw.Server.Server;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatObserver implements Observer{

    final private TurnHandler turnHandler;
    final private Chat chat;
    ArrayList<String> players;

    HashMap<String, ServerConnectionHandler> connections;

    public ChatObserver(TurnHandler turnHandler, ArrayList<String> players, Chat chat) {
        this.turnHandler = turnHandler;
        this.players = players;
        this.chat = chat;
        connections = new HashMap<>();
        for(String player: players){
            connections.put(player, Server.getHandle(player));
        }
    }

    @Override
    public void update() {
        for(String player: players){
            synchronized (turnHandler) {
                if (!player.equals(turnHandler.getActivePlayer())) {
                    connections.get(player).sendMessage(new ChatMTC(chat.downloadMessages(player)));
                }
            }
        }
    }
}
