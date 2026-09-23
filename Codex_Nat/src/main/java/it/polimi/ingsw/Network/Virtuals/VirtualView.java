package it.polimi.ingsw.Network.Virtuals;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.MessagesToServer.ViewUtilitiesMTS;
import it.polimi.ingsw.Server.Controller.GameObservers.Observer;
import it.polimi.ingsw.Server.Model.Enums.TurnPhase;

import java.io.Serializable;
import java.util.ArrayList;

public class VirtualView implements Serializable {
    static String view; //to indicate if CLI or GUI is active
    ArrayList<Observer> observers;
    static VirtualView virtualViewInstance;
    VirtualSecretObj virtualObj;
    VirtualDrawableCards virtualDrawableCards;
    VirtualScoreBoard virtualScoreBoard;
    VirtualPlayer virtualPlayer;
    boolean isLastTurnsSet;
    int lastTurn;
    ArrayList<String> messages;
    ArrayList<String> players;

    String activePlayer;
    TurnPhase turnPhase;


    public VirtualView() {
        turnPhase = TurnPhase.hasToPlaceCard;
        messages = new ArrayList<>();
        observers = new ArrayList<>();
        virtualViewInstance = this;
    }

    public static VirtualView getInstance() {
        return virtualViewInstance;
    }

    public static VirtualView getInstance(String args) {
        if (virtualViewInstance == null) {
            virtualViewInstance = new VirtualView();
            view = args;
        }
        return virtualViewInstance;
    }



    public void subscribeToThis(Observer obs) {
        observers.add(obs);
        ClientController cli = ClientController.getInstance();
        cli.getClientConnectionHandler().SendMessageToSever(new ViewUtilitiesMTS(cli.getGameID(), cli.getUsername()));
    }

    public void notifyObservers() {
        for (Observer obs : observers) {
            obs.update();
        }
    }

    public String getView() {
        return view;
    }

    public VirtualSecretObj getVirtualObj() {
        return virtualObj;
    }

    public void setVirtualDrawableCards(VirtualDrawableCards virtualDrawableCards) {
        this.virtualDrawableCards = virtualDrawableCards;
    }

    public VirtualDrawableCards getVirtualDrawableCards() {
        return virtualDrawableCards;
    }

    public void setVirtualScoreBoard(VirtualScoreBoard virtualScoreBoard) {
        this.virtualScoreBoard = virtualScoreBoard;
    }

    public VirtualScoreBoard getVirtualScoreBoard() {
        return virtualScoreBoard;
    }

    public void setVirtualPlayer(VirtualPlayer virtualPlayer) {
        this.virtualPlayer = virtualPlayer;
    }

    public VirtualPlayer getVirtualPlayer() {
        return virtualPlayer;
    }

    public boolean isLastTurnsSet() {
        return isLastTurnsSet;
    }

    public void setLastTurnsSet(boolean lastTurnsSet) {
        isLastTurnsSet = lastTurnsSet;
    }

    public int getLastTurn() {
        return lastTurn;
    }

    public void setLastTurn(int lastTurn) {
        this.lastTurn = lastTurn;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public String getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }

    public void setView(String view) {
        this.view = view;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }
}
