package it.polimi.ingsw.Server.Model.Exceptions;

public class NotEnoughResourcesException extends RuntimeException{
    public NotEnoughResourcesException(){
        super("You don't have enough resources to place this gold card");
    }
}
