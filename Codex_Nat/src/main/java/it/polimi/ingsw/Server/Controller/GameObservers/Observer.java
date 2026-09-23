package it.polimi.ingsw.Server.Controller.GameObservers;

import it.polimi.ingsw.Network.Server.LoggerUtility;

import java.util.logging.Logger;

public interface Observer {

    final Logger logger = LoggerUtility.getLogger();
    void update();
}