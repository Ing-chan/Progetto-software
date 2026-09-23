package it.polimi.ingsw.Network.Server.ObserverPackage.Subject;

import it.polimi.ingsw.Server.Controller.GameObservers.EndOfTurnObserver;

public interface EndOfTurnSubject {
    void attachEndOfTurn(EndOfTurnObserver observer);

    void notifyObservers();
}
