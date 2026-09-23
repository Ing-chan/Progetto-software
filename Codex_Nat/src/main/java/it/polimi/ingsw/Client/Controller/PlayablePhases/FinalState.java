package it.polimi.ingsw.Client.Controller.PlayablePhases;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.GameCommand;
import it.polimi.ingsw.Client.View.CLI.Printer;

/**
 * Represents the final state of the client's playable phases in the game.
 * In this state, the game is considered ended, and the final score is displayed.
 * It waits for user input to potentially handle any final user actions.
 */
public class FinalState extends PlayableState{

    /**
     * Executes the behavior associated with the FinalState.
     * In this state, the game is considered ended, and the final score is displayed.
     * It waits for user input to potentially handle any final user actions.
     */
    public static void execute() {

        ClientController cli = ClientController.getInstance();
        Printer view = Printer.getInstance();

        cli.gameIsEnded();
        cli.update();
        view.finalScore();

        System.out.println("ending final phase");

        GameCommand.HandleInput();

    }
}
