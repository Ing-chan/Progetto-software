package it.polimi.ingsw.Client.Controller.MessagesToServer;

/**
 * Represents a handshake message sent from the client to the server to establish a connection.
 * Extends {@link MessageToServer}.
 */
public class HandShakeMTS extends MessageToServer{

    /**
     * Constructs an instance of HandShakeMTS with the specified username.
     *
     * @param username The username of the client initiating the handshake.
     */
    public HandShakeMTS(String username){
        this.username = username;
    }

    /**
     * Updates the server with the handshake message. This method currently does nothing
     * as handshake messages does not require processing.
     */
    @Override
    public void update() {
    }

    /**
     * Gets the username associated with this handshake message.
     *
     * @return The username of the client initiating the handshake.
     */
    public String getUsername()
    {
        return username;
    }
}
