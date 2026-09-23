package it.polimi.ingsw.Server.Model.Exceptions;

public class NotDrawableCardException extends Exception {
    public NotDrawableCardException(){
        super("this card is not drawable");
    }

}