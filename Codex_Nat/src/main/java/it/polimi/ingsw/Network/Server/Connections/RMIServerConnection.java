package it.polimi.ingsw.Network.Server.Connections;

import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Retrieves a message from the client for RMI communication.
 *
 * @return The message received from the client.
 * @throws RemoteException  If there is an RMI communication error.
 */
public interface RMIServerConnection extends Remote {

    /**
     * Retrieves a message intended for a client from the RMI server.
     *
     * @return The {@link MessageToClient} object containing the message to be sent to the client.
     * @throws RemoteException If an RMI communication error occurs.
     */
    MessageToClient RMIGetMessageToClient() throws RemoteException;

}