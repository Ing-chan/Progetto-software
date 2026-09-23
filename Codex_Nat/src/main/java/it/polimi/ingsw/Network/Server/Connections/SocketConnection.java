package it.polimi.ingsw.Network.Server.Connections;

import it.polimi.ingsw.Client.Controller.MessagesToServer.MessageToServer;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;
import it.polimi.ingsw.Server.Controller.MessagesToClient.notOkayMTC;
import it.polimi.ingsw.Server.Controller.MessagesToClient.okayMTC;
import it.polimi.ingsw.Server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;
/**
 * Handles socket-based communication with a client on the server side.
 */
public class SocketConnection implements ServerConnectionHandler, Runnable {

    Socket socket;
    ObjectOutputStream outStream;
    ObjectInputStream inStream;
    String nickname;
    int gameID;
    private final Logger logger = LoggerUtility.getLogger();
    boolean connected = false;
    /**
     * Constructor to initialize the SocketConnection with a socket.
     *
     * @param socket The Socket object representing the connection with the client.
     */
    public SocketConnection(Socket socket) {
        this.socket = socket;
        try {
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Starts the connection handling process in a new thread.
     */
    public void connect() {
        //first sent message MUST be handshake MTS
        new Thread(this).start();
        connected = true;
        logger.info("Connection established with " + socket.getInetAddress());
    }
    /**
     * Disconnects the client from the server and closes resources.
     */
    public void disconnect() {
        try {
            Server.logout(nickname, this);
            logger.info("Client " + nickname + " disconnected");

            if (socket != null) {
                socket.close();
                logger.info("Socket connection closed with " + socket.getInetAddress());
            }
        } catch (IOException e) {
            logger.severe("Socket server failed to start: " + e.getMessage());
        }
    }

    /**
     * Sends a message to the client.
     *
     * @param message The MessageToClient object to be sent.
     */
    public void sendMessage(MessageToClient message) {
        try {
            if (connected) {
                outStream.reset();
                outStream.writeObject(message);
                outStream.flush();
            }
        } catch (IOException e) {
            logger.severe("Socket server failed to start: " + e.getMessage());
        }
    }

    
    @Override
    public void run() {

        //makes sure there is only one client with the same username
        while (!Server.login(receiveMessage().getNickname(), this)) {
            sendMessage(new notOkayMTC("Nickname already taken"));
        }
        sendMessage(new okayMTC());

        while (connected) {
            MessageToServer message = receiveMessage();

            if (message == null){
                disconnect();
                connected = false;
            }
            else{
                nickname = message.getNickname();
                message.update();
            }
        }
    }

    /**
     * Receives a message from the client.
     *
     * @return The received MessageToServer object.
     */
    //TODO FARE IMPLEMENTAZIONE
    public MessageToServer receiveMessage() {
        try {
            return (MessageToServer) inStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public String getNickname() {
        return nickname;
    }

    public int getGameID() {
        return gameID;
    }
}

