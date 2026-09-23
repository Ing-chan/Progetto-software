package it.polimi.ingsw.Server.Controller.MessagesToClient;


import it.polimi.ingsw.Client.Controller.Executor.StopGameMTCExecutor;

public class StopGameMTC extends MessageToClient{

    public StopGameMTC() {
        this.type=MTCtype.DISCONNECTED;
    }

    @Override
    public void update() { StopGameMTCExecutor.execute(this); }
}
