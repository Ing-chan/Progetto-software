package it.polimi.ingsw.Server.Controller.MessagesToClient;

import it.polimi.ingsw.Network.Virtuals.VirtualPlayer;
import it.polimi.ingsw.Server.Model.GameModel;

import java.io.Serializable;

public abstract class MessageToClient implements Serializable {

    MTCtype type;

    int gameID;
    GameModel game;
    String username;
    VirtualPlayer player;

    public abstract void update();

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setGame(GameModel game) {
        this.game = game;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGameID() {
        return gameID;
    }



    public String getNickname() {
        return username;
    }

    public VirtualPlayer getPlayer() {
        return player;
    }

    public MTCtype getType() {
        return type;
    }
}
