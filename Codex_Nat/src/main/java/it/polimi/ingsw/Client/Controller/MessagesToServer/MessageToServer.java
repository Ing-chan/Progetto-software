package it.polimi.ingsw.Client.Controller.MessagesToServer;

import java.io.Serializable;

/**
 * Abstract class representing a message sent from the client to the server.
 * Implements {@link Serializable} to allow objects to be serialized.
 * Subclasses must implement the {@link #update()} method.
 */
public abstract class MessageToServer implements Serializable {
    int gameID;
    String username;

    /**
     * Updates the server with the specific message. Subclasses must provide an implementation.
     */
    public abstract void update();

    /**
     * Sets the game ID for this message.
     *
     * @param gameID The game ID to set.
     */
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    /**
     * Sets the nickname for this message.
     *
     * @param username The nickname to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the game ID associated with this message.
     *
     * @return The game ID.
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * Gets the nickname associated with this message.
     *
     * @return The nickname.
     */
    public String getNickname() {
        return username;
    }
}
