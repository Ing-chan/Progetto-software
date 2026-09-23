package it.polimi.ingsw.Client.Controller.PlayablePhases;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.GameCommand;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MTCtype;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;
import it.polimi.ingsw.Network.Client.ClientConnectionHandler;
import it.polimi.ingsw.Client.View.CLI.Printer;
import it.polimi.ingsw.Client.View.CLI.Reader;

public class SetupState extends PlayableState {

    public static void execute() {

        //crea e avvia il thread reader per gestire l'input dei comandi
        Reader reader = Reader.getInstance();
        new Thread(reader).start();

        ClientConnectionHandler clientConnectionHandler = ClientController.getInstance().getClientConnectionHandler();

        GameCommand.ChooseSecretObjective.TUICommand();

        MessageToClient msg;

        //waits until it's the player's turn
        do {
            msg = clientConnectionHandler.GetMessageToClient();
            ClientController.getInstance().update();
            Printer.getInstance().update();
        } while (!msg.getType().equals(MTCtype.ISYOURTURN));

        GameCommand.HandleInput();

        System.out.println("Ending SetupState");
        }


}

