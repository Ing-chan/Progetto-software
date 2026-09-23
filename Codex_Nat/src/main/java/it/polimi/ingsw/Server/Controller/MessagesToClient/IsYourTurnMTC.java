package it.polimi.ingsw.Server.Controller.MessagesToClient;

import it.polimi.ingsw.Client.Controller.Executor.IsYourTurnMTCExecutor;

public class IsYourTurnMTC extends MessageToClient{

    public IsYourTurnMTC(String player){
        this.username = player;
        this.type=MTCtype.ISYOURTURN;
    }

    public void update(){
        IsYourTurnMTCExecutor.execute();
    }


}