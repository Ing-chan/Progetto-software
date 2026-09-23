package it.polimi.ingsw.Network.Client.RMI;

import it.polimi.ingsw.Client.Controller.MessagesToServer.MessageToServer;

import java.rmi.Remote;
import java.rmi.RemoteException;




/**
 * This interface defines the methods for RMI client connections.
 */
public interface RMIClientConnection extends Remote {

    /**
     * Receives a message sent to the client.
     *
     *  message the message to be received.
     * @throws RemoteException in case of problems with communication with the client.
     */
    MessageToServer RMIGetMessageToServer() throws RemoteException;

    void ConnectToRMIConnection (String connectionSession) throws  RemoteException;

    void close() throws RemoteException;

}

