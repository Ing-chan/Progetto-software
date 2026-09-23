package it.polimi.ingsw.Network.Server.Connections;

import it.polimi.ingsw.Client.Controller.MessagesToServer.MessageToServer;
import it.polimi.ingsw.Network.Client.RMI.RMIClientConnection;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;
import it.polimi.ingsw.Server.Controller.MessagesToClient.notOkayMTC;
import it.polimi.ingsw.Server.Controller.MessagesToClient.okayMTC;
import it.polimi.ingsw.Server.Server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Handles the RMI connection with a client, implementing {@link RMIServerConnection}
 * and {@link ServerConnectionHandler}.
 */
public class RMIConnectionHandler extends UnicastRemoteObject implements RMIServerConnection, ServerConnectionHandler {

    String nickname;
    Logger logger = LoggerUtility.getLogger();
    private final ArrayList<MessageToClient> MTCQueue;
    RMIClientConnection ClientStud;
    String session;
    boolean connected;

    /**
     * Constructs an RMIConnectionHandler object.
     *
     * @param ClientStud         RMIClientConnection representing the client connection.
     * @param RMI_PORT           The RMI port used for the connection.
     * @param RMIServerSession   The session identifier for the RMI server session.
     * @throws RemoteException   If there is an RMI communication error.
     */
    protected RMIConnectionHandler(RMIClientConnection ClientStud, int RMI_PORT, String RMIServerSession) throws RemoteException {
        super();
        connected = false;
        this.ClientStud = ClientStud;
        System.out.println("inizializzando handlerRMI clientStud=" + ClientStud);
        MTCQueue = new ArrayList<>();
        initRegistry(RMI_PORT, RMIServerSession);
    }

    /**
     * Initializes the RMI registry and binds the RMI connection handler.
     *
     * @param RMI_PORT           The RMI port used for the connection.
     * @param RMIServerSession   The session identifier for the RMI server session.
     * @throws RemoteException   If there is an RMI communication error.
     */
    void initRegistry(int RMI_PORT, String RMIServerSession) throws RemoteException {
        int randomNumber = Math.abs(new Random().nextInt());
        session = RMIServerSession + "_" + randomNumber;
        String service = "CodexNaturalis_" + randomNumber;
        logger.info("\u001B[32m\"" + "Publishing connection at port " + RMI_PORT + " at address " + session + "\u001B[31m");
        Registry registry = LocateRegistry.getRegistry(RMI_PORT);
        registry.rebind(service, this);
        ClientStud.ConnectToRMIConnection(service);
        connected = true;
    }

    /**
     * Starts the connection handling process in a separate thread.
     */
    @Override
    public void connect() {
        new Thread(this).start();
        connected = true;
        logger.info("Thread started");
    }

    /**
     * Disconnects the RMI connection.
     */
    @Override
    public void disconnect() {
        try {
            logger.info("Client " + nickname + " disconnected");
            Server.logout(nickname, this);
            connected = false;
            if (ClientStud != null) {
                ClientStud.close();
                logger.info("Connection closed with " + session);
            }
        } catch (IOException e) {
            logger.severe("Socket server failed to disconnect: " + e.getMessage());
        }
    }

    /**
     * Sends a message to the client.
     *
     * @param message   The message to send.
     */
    @Override
    public void sendMessage(MessageToClient message) {
        synchronized (MTCQueue) {
            MTCQueue.add(message);
            MTCQueue.notify();
        }
    }

    /**
     * Receives a message from the client.
     *
     * @return The received message.
     */
    @Override
    public MessageToServer receiveMessage() {
        MessageToServer msg = null;
            try {
                msg = ClientStud.RMIGetMessageToServer();
            } catch (RemoteException e) {
                connected = false;
                LoggerUtility.getLogger().severe(e.getMessage());
                disconnect();

        }
        return msg;
    }

    /**
     * Runs the connection handling logic, including client login and message processing.
     */
    @Override
    public void run() {

        nickname = receiveMessage().getNickname();
        while (!Server.login(nickname, this)) {

            nickname = receiveMessage().getNickname();
            sendMessage(new notOkayMTC("Username already taken"));
        }
        sendMessage(new okayMTC());

        while (connected) {
            MessageToServer message = receiveMessage();
            message.update();
        }
        Server.logout(nickname, this);
        logger.info("Client" + nickname + " Disconnected");
    }


    /**
     * Retrieves a message from the client for RMI communication.
     *
     * @return The message received from the client.
     * @throws RemoteException  If there is an RMI communication error.
     */
        @Override
        public MessageToClient RMIGetMessageToClient () throws RemoteException {
            MessageToClient msg;
            synchronized (MTCQueue) {
                while (MTCQueue.isEmpty()) {
                    try {
                        MTCQueue.wait();
                    } catch (InterruptedException e) {
                        LoggerUtility.getLogger().severe(e.getMessage());
                    }
                }
                msg = MTCQueue.getFirst();
                MTCQueue.removeFirst();
            }
            return msg;
        }

    }
