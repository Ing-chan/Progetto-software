package it.polimi.ingsw.Server.Controller.GameObservers;

import it.polimi.ingsw.Server.Controller.MessagesToClient.GameIsStartedMTC;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;
import it.polimi.ingsw.Server.Server;

import java.util.ArrayList;

public class GameIsStarted implements Observer {

    ArrayList<String> usernames;
    int gameID;

    public GameIsStarted(int gameID) {
        usernames = new ArrayList<>();
        this.gameID=gameID;
    }

    public void add(String username) {
        usernames.add(username);

    }

    public void update() {
        MessageToClient msg = new GameIsStartedMTC(gameID);

        for (String nick : usernames) {
            Server.getHandle(nick).sendMessage(msg);
        }
    }
}
