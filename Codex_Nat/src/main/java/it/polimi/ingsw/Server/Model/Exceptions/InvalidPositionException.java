package it.polimi.ingsw.Server.Model.Exceptions;

public class InvalidPositionException extends RuntimeException{
    public InvalidPositionException() {
        super("Invalid Position");
    }
}
