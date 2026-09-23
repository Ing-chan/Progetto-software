package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Client.Controller.MessagesToServer.ViewUtilitiesMTS;
import it.polimi.ingsw.Network.Client.ClientConnectionHandler;
import it.polimi.ingsw.Network.Client.RMI.ClientRMIHandler;
import it.polimi.ingsw.Network.Client.Socket.ClientSocketHandler;
import it.polimi.ingsw.Network.Virtuals.VirtualCard;
import it.polimi.ingsw.Network.Virtuals.VirtualView;
import it.polimi.ingsw.Server.Server;

import java.rmi.RemoteException;

public class ClientController {

    private int gameID;
    private String username;
    private int playersInMatch;
    private ClientConnectionHandler clientConnectionHandler;

    private boolean gameEnded;
    private boolean gameIsClosing;
    static ClientController instance;
    VirtualView virtualView;


    public ClientController(String connectionType, String address) {

        if (address.equals("localhost") || address.isEmpty()) {
            address = "127.0.0.1";
        }
        if (connectionType != null) {
            if (connectionType.equals("Socket")) {
                clientConnectionHandler = new ClientSocketHandler(address, Server.getSocketPort());
            } else {
                try {
                    clientConnectionHandler = new ClientRMIHandler(address, Server.getRMIPort());
                } catch (RemoteException e) {
                    System.out.println("Error starting RMI");
                    e.printStackTrace();
                }
            }
        }
        gameEnded = false;
        gameIsClosing = false;
        instance = this;
        virtualView = VirtualView.getInstance();
    }


    public void update() {
        //download the info from the server
        clientConnectionHandler.SendMessageToSever(new ViewUtilitiesMTS(gameID, username));
        //updates the virtualView with the new attributes
        clientConnectionHandler.GetMessageToClient().update();
    }

    public boolean flip(int num) {
        if (num >= 0 && num < virtualView.getVirtualPlayer().getPlayerCards().size()) {
            virtualView.getVirtualPlayer().getPlayerCards().get(num).flip();
            return true;
        }
        return false;
    }

    public boolean getSide(String cardID) {
        for (VirtualCard card : virtualView.getVirtualPlayer().getPlayerCards()) {
            if (card.getID().equals(cardID)) {
                return card.getFace();
            }
        }
        return false;
    }

    public ClientConnectionHandler getClientConnectionHandler() {
        return clientConnectionHandler;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static ClientController getInstance() {
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setPlayersInMatch(int playersInMatch) {
        this.playersInMatch = playersInMatch;
    }

    public int getPlayersInMatch() {
        return playersInMatch;
    }

    public boolean playerIsActive(){
        return VirtualView.getInstance().getActivePlayer().equals(username);
    }

    public void gameIsEnded() { gameEnded = true;}

    public boolean isGameEnded() {return gameEnded;}

    public void gameIsClosing() {
        gameIsClosing = true;
    }

    public boolean IsGameClosing() {
        return gameIsClosing;
    }
}
