package it.polimi.ingsw.Network.Server.Connections;

import it.polimi.ingsw.Network.Client.RMI.RMIClientConnection;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Interface that defines methods for an RMI server to connect to clients.
 * Extends {@link Remote} to indicate that these methods can be invoked remotely.
 */
public interface RMIServerInterface extends Remote {
    /**
     * Connects to a client identified by the provided RMI service name.
     * This method is invoked remotely by the client to establish the connection.
     *
     * @param clientServiceName The RMI service name of the client.
     * @throws RemoteException If an RMI communication error occurs.
     */
    void ConnectToClient(RMIClientConnection clientConnection) throws RemoteException;

    String GetClientIP() throws RemoteException;

}
