package it.polimi.ingsw.Client.Controller.PlayablePhases;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.GameCommand;
import it.polimi.ingsw.Client.View.CLI.Printer;

import java.util.Scanner;

/**
 * Represents the idle state where the client waits for its turn in the game.
 * This state continuously checks for updates from the server until the game is closing.
 * After the game ends, it waits for user input to close the game and then terminates the reader thread.
 */
public class IdleState extends PlayableState {

    /**
     * Executes the behavior associated with the IdleState.
     * This method continuously checks for updates from the server until the game is closing.
     * After the game ends, it waits for user input to close the game and then terminates the reader thread.
     */
    public static void execute() {
        ClientController cli = ClientController.getInstance();

        while (!cli.IsGameClosing()) {
            ClientController.getInstance().getClientConnectionHandler().GetMessageToClient().update();
            Printer.getInstance().update();
        }

        System.out.println("_Press any Enter to close the game_");

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        GameCommand.killReader();
    }
}
