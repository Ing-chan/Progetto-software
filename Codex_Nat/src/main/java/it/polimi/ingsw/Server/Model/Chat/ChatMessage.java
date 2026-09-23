package it.polimi.ingsw.Server.Model.Chat;


/**
 * @param broadcast true (message is for for everyone)
 */
public record ChatMessage(String msg, String sender, String receiver, boolean broadcast) {

}