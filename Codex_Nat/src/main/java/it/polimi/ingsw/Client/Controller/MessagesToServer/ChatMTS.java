package it.polimi.ingsw.Client.Controller.MessagesToServer;


import it.polimi.ingsw.Server.Controller.Executor.ChatMTSExecutor;

/**
 * Represents a chat message to be sent to the server.
 * This message can either be sent to a specific receiver or broadcasted to all players.
 */
public class ChatMTS extends MessageToServer{

    /** The receiver of the message. */
    String receiver;

    /** Flag indicating if the message is a broadcast. */
    boolean broadcast;

    /** The chat message content. */
    private final String message;

    /**
     * Constructs a new ChatMTS instance.
     *
     * @param gameID the ID of the game
     * @param player the username of the player sending the message
     * @param chatMessage the content of the chat message
     * @param receiver the receiver of the message
     * @param broadcast flag indicating if the message is a broadcast
     */
    public ChatMTS(int gameID, String player, String chatMessage, String receiver, boolean broadcast) {
        this.gameID = gameID;
        this.username = player;
        this.message = chatMessage;
        this.broadcast = broadcast;
        this.receiver = receiver;
    }

    /**
     * Gets the chat message content.
     *
     * @return the chat message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the chat message content.
     *
     * @return the chat message
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Checks if the message is a broadcast.
     *
     * @return true if the message is a broadcast, false otherwise
     */
    public boolean isBroadcast() {
        return broadcast;
    }

    /**
     * Executes the chat message using the ChatMTSExecutor.
     */
    @Override
    public void update() {
        ChatMTSExecutor.execute(this);
    }
}