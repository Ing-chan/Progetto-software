package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MTCtype;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WaitingRoomSceneController extends Gui implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startProgress();
    }

    private void startProgress() {
        new Thread(() -> {

            MessageToClient msg;
            do {
                msg = ClientController.getInstance().getClientConnectionHandler().GetMessageToClient();
                // Ensure the message is not null
                if (msg == null) {
                    System.out.println("Received null message, retrying...");
                }

            } while (!Objects.requireNonNull(msg).getType().equals(MTCtype.GAMESTARTED));

            LoggerUtility.getLogger().fine("The Game Begins!");
            ClientController.getInstance().setGameID(msg.getGameID());

            if (ClientController.getInstance() == null) {
                System.out.println("Error in ClientController");
            }
            StageController.changePrimaryScene("fxml/secret-objective.fxml", "Set a secret objective card");

        }).start();
    }

}