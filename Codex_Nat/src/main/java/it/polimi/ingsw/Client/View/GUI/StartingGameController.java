package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.MessagesToServer.ChooseSecretObjectiveMTS;
import it.polimi.ingsw.Network.Client.ClientConnectionHandler;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MTCtype;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class StartingGameController extends Gui implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startGame();
    }

    public void startGame() {
        new Thread(() -> {
            String selectedObj = SecretObjController.getSelectedObjective();

            MessageToClient msg;
            do {
                ClientConnectionHandler clientConnectionHandler = ClientController.getInstance().getClientConnectionHandler();
                ClientController gui = ClientController.getInstance();
                clientConnectionHandler.SendMessageToSever(new ChooseSecretObjectiveMTS(gui.getGameID(), gui.getUsername(), selectedObj));
                LoggerUtility.getLogger().info("The objective has been chosen");
                msg = clientConnectionHandler.GetMessageToClient();

                if (!msg.getType().equals(MTCtype.OKAY)) {
                    System.out.println("Something went wrong");
                }

            } while(!msg.getType().equals(MTCtype.OKAY));

            do {
                ClientConnectionHandler clientConnectionHandler = ClientController.getInstance().getClientConnectionHandler();
                LoggerUtility.getLogger().info("Waiting for my turn...");
                msg = clientConnectionHandler.GetMessageToClient();
                ClientController.getInstance().update();
            } while(!msg.getType().equals(MTCtype.ISYOURTURN));

            StageController.changePrimaryScene("fxml/game-board.fxml", "Play");
        }).start();
    }
}
