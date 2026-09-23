package it.polimi.ingsw.Client.Controller.Excteptions;

/**
 * Exception thrown when the nickname field is empty.
 */
public class EmptyNicknameFileldException extends Exception {

    /**
     * Constructs a new EmptyNicknameFileldException with the default message.
     */
    public EmptyNicknameFileldException() {
        super("Nickname field is empty");
    }
}
