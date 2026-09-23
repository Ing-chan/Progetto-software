package it.polimi.ingsw.Server.Controller.GameObservers;

import it.polimi.ingsw.Server.Controller.MessagesToClient.CurrentGamesMTC;
import it.polimi.ingsw.Server.Server;

import java.util.ArrayList;

public class ActiveGames implements Observer{

    String username;
    ArrayList<String> currentGames;

    public ActiveGames(String username, ArrayList<String> currentGames){
        this.username = username;
        this.currentGames = currentGames;
    }


    public void update(){
        Server.getHandle(username).sendMessage(new CurrentGamesMTC(currentGames));
    }
}
