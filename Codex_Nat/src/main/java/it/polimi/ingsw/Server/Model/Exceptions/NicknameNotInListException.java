package it.polimi.ingsw.Server.Model.Exceptions;

public class NicknameNotInListException extends RuntimeException {
    public NicknameNotInListException(String name) {
        super("There is no nickname that matches " + name + "in the list of players");
    }
}
