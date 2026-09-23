package it.polimi.ingsw.Network.Server.Connections;


import it.polimi.ingsw.Network.Client.RMI.RMIClientConnection;
import it.polimi.ingsw.Network.Server.LoggerUtility;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

/**
 * Runs the connection handling logic in a separate thread.
 * Implementations should handle client interactions within this method.
 */
public class ServerRMI extends UnicastRemoteObject implements RMIServerInterface {

    private final Logger logger = LoggerUtility.getLogger();

    final String serverIP;
    final int RMI_PORT;
    RMIClientConnection ClientStud;

    /**
     * Constructs the RMI server with the specified IP address and port.
     *
     * @param serverIP The IP address of the server.
     * @param RMI_PORT The port number for RMI communication.
     * @throws RemoteException If an RMI communication error occurs.
     */
    public ServerRMI(String serverIP, int RMI_PORT) throws RemoteException {
        super();
        this.serverIP = serverIP;
        this.RMI_PORT = RMI_PORT;
        initRegistry();
        ClientStud = null;
    }

    /**
     * Waits for a client connection request, then establishes a connection using RMI.
     * Once connected, it creates an {@link RMIConnectionHandler} to manage the client connection.
     */
    public void accept() {
        synchronized (this) {
            while (ClientStud == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    LoggerUtility.getLogger().severe(e.getMessage());
                }
            }
            String rmiSession = "rmi://" + serverIP + ":" + RMI_PORT + "/CodexNaturalis";

            try {
                RMIConnectionHandler rmiConnectionHandler = new RMIConnectionHandler(ClientStud, RMI_PORT, rmiSession);
                rmiConnectionHandler.connect();
            } catch (RemoteException e) {
                LoggerUtility.getLogger().severe(e.getMessage());
            }
            ClientStud = null;
            notify(); //prob inutile
        }
    }


    /**
     * Initializes the RMI registry and binds the server object to a specified session name.
     * Logs successful server start with the session details.
     */
    void initRegistry() {
        try {
            String rmiSession = "rmi://" + serverIP + ":" + RMI_PORT + "/CodexNaturalis";
            Registry registry = LocateRegistry.createRegistry(RMI_PORT);
            registry.bind("CodexNaturalis", this);
            logger.info("RMI server is online with naming: " + rmiSession);
        } catch (RemoteException | AlreadyBoundException e) {
            LoggerUtility.getLogger().severe("Banana" + e.getMessage());

    }
    }





    //Function belonging to the interface
    @Override
    public void ConnectToClient(RMIClientConnection stud) throws RemoteException {
        //first sent message MUST be handshake MTS
        synchronized (this) {
            while (ClientStud == null) {
                LoggerUtility.getLogger().info("trying to connect");
                    ClientStud = stud;
                    LoggerUtility.getLogger().info("connected" + ClientStud.toString());

            }
            this.notify();
        }
    }

    @Override
    public String GetClientIP() throws RemoteException {
        try {
            return RemoteServer.getClientHost();
        } catch (ServerNotActiveException e) {
            throw new RemoteException("Cannot get client host", e);
        }
    }
}
