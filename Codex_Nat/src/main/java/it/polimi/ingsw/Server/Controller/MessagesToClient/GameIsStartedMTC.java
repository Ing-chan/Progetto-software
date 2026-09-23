package it.polimi.ingsw.Server.Controller.MessagesToClient;

public class GameIsStartedMTC extends MessageToClient {
    public GameIsStartedMTC(int gameID) {
        this.gameID=gameID;
        this.type=MTCtype.GAMESTARTED;
    }

    @Override
    public void update() {

    }
}
