package it.polimi.ingsw.Client.Controller.PlayablePhases;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.GameCommand;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MTCtype;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;
import it.polimi.ingsw.Server.Controller.MessagesToClient.notOkayMTC;
import it.polimi.ingsw.Network.Client.ClientConnectionHandler;
import it.polimi.ingsw.Client.View.CLI.Printer;

public class PlaceCardState extends PlayableState {

    //this states places a card, enter here if its your turn
    public static void execute() {
        ClientConnectionHandler clientConnectionHandler = ClientController.getInstance().getClientConnectionHandler();

        ClientController cli = ClientController.getInstance();
        Printer view = Printer.getInstance();

        cli.update();
        view.update();

        GameCommand.HandleInput();

        MessageToClient confirmMessage = clientConnectionHandler.GetMessageToClient();

        if (confirmMessage.getType().equals(MTCtype.NOTOKAY)) {
            System.out.println(((notOkayMTC)confirmMessage).getMessage());
            execute();
        }

        System.out.println("Ending PlaceCard State");
    }

}