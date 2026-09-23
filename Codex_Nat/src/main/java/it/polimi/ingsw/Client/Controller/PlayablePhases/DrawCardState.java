package it.polimi.ingsw.Client.Controller.PlayablePhases;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.GameCommand;
import it.polimi.ingsw.Client.View.CLI.Printer;
import it.polimi.ingsw.Network.Client.ClientConnectionHandler;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MTCtype;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;

/**
 * Represents the state where the client draws a card during gameplay.
 * It handles the process of drawing a card, updates the client's state,
 * and waits for confirmation from the server that the action was successful.
 * This state utilizes {@link ClientController#update()} to synchronize client state,
 * {@link Printer#update()} to update the CLI view, and {@link GameCommand#HandleInput()} to
 * potentially handle user input during the card drawing process.
 * If the server indicates an error with {@link MTCtype#NOTOKAY}, it retries the card drawing process.
 */
public class DrawCardState extends PlayableState {


    /**
     * Executes the behavior associated with the DrawCardState.
     * It synchronizes client state, updates the CLI view, and handles user input.
     * If the card drawing process encounters an error (NOTOKAY message from server),
     * it retries the drawing process until successful.
     */
    public static void execute() {
        ClientConnectionHandler clientConnectionHandler = ClientController.getInstance().getClientConnectionHandler();

        ClientController cli = ClientController.getInstance();
        Printer view = Printer.getInstance();

        cli.update();
        view.update();

        GameCommand.HandleInput();

        MessageToClient confirmMessage = clientConnectionHandler.GetMessageToClient();

        if (confirmMessage.getType().equals(MTCtype.NOTOKAY)) {
            System.out.println("Something went wrong");
            execute();
        }

        cli.update();

        System.out.println("ending DrawCard phase");
    }
}
