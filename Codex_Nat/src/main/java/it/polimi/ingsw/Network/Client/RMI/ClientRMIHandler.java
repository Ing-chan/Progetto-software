package it.polimi.ingsw.Network.Client.RMI;

import it.polimi.ingsw.Client.Controller.MessagesToServer.MessageToServer;
import it.polimi.ingsw.Network.Client.ClientConnectionHandler;
import it.polimi.ingsw.Network.Server.Connections.RMIServerConnection;
import it.polimi.ingsw.Network.Server.Connections.RMIServerInterface;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MTCtype;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class ClientRMIHandler extends UnicastRemoteObject implements RMIClientConnection, ClientConnectionHandler, Serializable {

    RMIServerConnection RMIServerStud;
    Registry registry;
    private final ArrayList<MessageToServer> MTSQueue;

    // RMI client's constructor
    public ClientRMIHandler(String address, int port) throws RemoteException {
        super();
        MTSQueue = new ArrayList<>();

        try {
            // Connect to the RMI registry
            //gets server address
            String rmiSession = "rmi://" + address + ":" + port + "/CodexNaturalis";
//            System.out.println("trying to connect at " + rmiSession);
//            RMIServerInterface rmiServer = (RMIServerInterface) Naming.lookup(rmiSession);
            registry = LocateRegistry.getRegistry(address, port);
            RMIServerInterface rmiServer = (RMIServerInterface) registry.lookup("CodexNaturalis");
            rmiServer.ConnectToClient(this);

//            System.out.println("Connected to the RMI server at " + rmiSession);
        } catch (NotBoundException e) {
            LoggerUtility.getLogger().severe("Failed to connect to RMI server: " + e.getMessage());
            e.printStackTrace();
        }

    }


    @Override
    public MessageToClient GetMessageToClient() {
        MessageToClient msg = null;
        while (msg == null) {
            try {

                msg = RMIServerStud.RMIGetMessageToClient();

                if (msg.getType() != null && msg.getType().equals(MTCtype.DISCONNECTED)) {
                    msg.update();
                }

                return msg;
            } catch (RemoteException e) {
                LoggerUtility.getLogger().severe(e.getMessage());
            }
        }
        return msg;
    }


    @Override
    public void SendMessageToSever(MessageToServer message) {
        synchronized (MTSQueue) {
            MTSQueue.add(message);
            MTSQueue.notify();
        }
    }


    @Override
    public MessageToServer RMIGetMessageToServer() throws RemoteException {
        MessageToServer msg;
        synchronized (MTSQueue) {
            while (MTSQueue.isEmpty()) {
                try {
                    MTSQueue.wait();
                } catch (InterruptedException e) {
                    LoggerUtility.getLogger().severe(e.getMessage());
                }
            }
            msg = MTSQueue.getFirst();
            MTSQueue.removeFirst();
        }
        return msg;
    }

    @Override
    public void WaitForMessage(MTCtype type) {
        MessageToClient msg;

        do {
            msg = GetMessageToClient();
            msg.update();
        } while (!msg.getType().equals(type));
    }

    @Override
    public void ConnectToRMIConnection(String connectionSession) {
        synchronized (this) {
            try {
                RMIServerStud = (RMIServerConnection) registry.lookup(connectionSession);
            } catch (NotBoundException | RemoteException e) {
                LoggerUtility.getLogger().severe("Failed to connect to RMI connection " + e.getMessage());
            }
        }
    }

    @Override
    public void close() throws RemoteException {
        RMIServerStud = null;
    }


}