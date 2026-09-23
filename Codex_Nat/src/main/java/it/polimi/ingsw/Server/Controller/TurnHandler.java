package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Network.Server.ObserverPackage.Subject.EndOfTurnSubject;
import it.polimi.ingsw.Server.Controller.GameObservers.EndOfTurnObserver;
import it.polimi.ingsw.Server.Model.Enums.TurnPhase;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurnHandler implements EndOfTurnSubject {

    ArrayList<String> players;
    String activePlayer;
    TurnPhase turnPhase;
    int numTurn;
    int lastTurn;

    final Logger logger = LoggerUtility.getLogger();


    public TurnHandler() {
        this.players = new ArrayList<>();
        activePlayer = null;
        this.numTurn = 0;
        turnPhase = TurnPhase.hasToPlaceCard;
        lastTurn = -1;
    }

    public void changeTurn() {
              //goes to the next active player
            int nextIndex = (players.indexOf(activePlayer) + 1) % players.size();
            setActivePlayer(players.get(nextIndex));
            //increases turn number when we get back to the first
            if (activePlayer.equals(players.getFirst())) {
                numTurn++;
            }

    }


    public void nextTurnPhase() {
        int nextIndex = (turnPhase.ordinal() + 1) % TurnPhase.values().length;
        turnPhase = TurnPhase.values()[nextIndex];
    }

    public void changeHisMind() {
        turnPhase = TurnPhase.hasToPlaceCard;
    }//todo delete

    public int getNumTurn() {
        return numTurn;
    }

    public int getLastTurn() {
        return lastTurn;
    }

    //todo non so cosa faccia
    @Override
    public void attachEndOfTurn(EndOfTurnObserver observer) {

    }

    public void notifyObservers() {

    }

    public void addPlayer(String player) {
        players.add(player);
        if (activePlayer == null) {
            activePlayer = player;
        }
    }

    void setActivePlayer(String newActivePlayer) {
        activePlayer = newActivePlayer;
    }//todo probabilmente vola


    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public String getActivePlayer() {
        return activePlayer;
    }

    public void setLastTurn() {
        //TODO rimnettere a 3
        if (lastTurn == -1) {
            lastTurn = numTurn + 1;
            logger.log(Level.INFO, "\u001b[35m" + "LAST 3 TURNS" + "\u001B[31m");
        }
    }

    public boolean lastTurnIsSet() {
        return lastTurn != -1;
    }

    public boolean isLastTurn() {
        return (numTurn == lastTurn);
    }
}
