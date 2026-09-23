package it.polimi.ingsw.Network.Server.Connections;

import it.polimi.ingsw.Client.Controller.MessagesToServer.MessageToServer;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;
import it.polimi.ingsw.Network.Server.LoggerUtility;

import java.util.logging.Logger;

/**
 * Interface defining methods for handling server-side connections with clients.
 * Implementing classes handle socket or RMI connections.
 */
public interface ServerConnectionHandler extends Runnable{

    /**
     * Logger instance for logging connection events and messages.
     */
    Logger logger = LoggerUtility.getLogger();
    /**
     * Establishes the connection with the client.
     */
    void connect();
    /**
     * Disconnects the client from the server.
     */
    void disconnect();
    /**
     * Sends a message to the connected client.
     *
     * @param message The MessageToClient object to send.
     */
    void sendMessage(MessageToClient message);
    /**
     * Receives a message from the connected client.
     *
     * @return The received MessageToServer object.
     */
    MessageToServer receiveMessage();
    /**
     * Runs the connection handling logic in a separate thread.
     * Implementations should handle client interactions within this method.
     */
    //public abstract MessageToClient sendMessage();

    void run();

}

