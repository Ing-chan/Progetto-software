package it.polimi.ingsw.Server.Controller.MessagesToClient;

import it.polimi.ingsw.Client.Controller.Executor.EndGameMTCExecutor;

public class EndGameMTC extends MessageToClient {

    public EndGameMTC() {
        this.type=MTCtype.ENDGAME;
    }

    @Override
    public void update() {
        EndGameMTCExecutor.execute(this);
    }
}
