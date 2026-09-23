package it.polimi.ingsw.Server.Model.Exceptions;

public class NegativeResourcesException extends RuntimeException{
    public NegativeResourcesException() {
        super("Removing this amount of resources will result in a negative amount of resources");
    }
}
