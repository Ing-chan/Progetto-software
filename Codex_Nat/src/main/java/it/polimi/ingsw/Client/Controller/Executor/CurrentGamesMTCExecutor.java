package it.polimi.ingsw.Client.Controller.Executor;

import it.polimi.ingsw.Server.Controller.MessagesToClient.CurrentGamesMTC;

/**
 * Executor class for handling the execution of CurrentGamesMTC messages.
 * This class is responsible for executing actions based on the CurrentGamesMTC message received.
 */
public class CurrentGamesMTCExecutor implements MTCExecutor{

    /**
     * Executes the CurrentGamesMTC message.
     * This method invokes the getPrint() method on the CurrentGamesMTC instance to print current games.
     *
     * @param currentGamesMTC The CurrentGamesMTC message instance to execute.
     */
    public static void execute(CurrentGamesMTC currentGamesMTC) {
       currentGamesMTC.getPrint();
    }
}
