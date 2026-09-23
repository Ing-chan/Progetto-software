package it.polimi.ingsw.Server.Controller.MessagesToClient;

import it.polimi.ingsw.Client.Controller.Executor.CurrentGamesMTCExecutor;

import java.util.ArrayList;

public class CurrentGamesMTC extends MessageToClient{

    ArrayList<String> CurrentGames;

    public CurrentGamesMTC(ArrayList<String> CurrentGames){
        this.type = MTCtype.CURRENTGAMES;
        this.CurrentGames = CurrentGames;
    }

    public void update(){
        CurrentGamesMTCExecutor.execute(this);
    }

    public void getPrint(){
        for(String string: CurrentGames){
            System.out.println(string);
        }
    }

    public ArrayList<String> getCurrentGames() {
        return CurrentGames;
    }

}
