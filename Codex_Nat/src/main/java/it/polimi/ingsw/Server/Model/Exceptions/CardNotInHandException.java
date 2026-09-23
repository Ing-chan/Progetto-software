package it.polimi.ingsw.Server.Model.Exceptions;

public class CardNotInHandException extends RuntimeException {
    public CardNotInHandException() {
        super("Error! You don't have the card in your hand");
    }
}
