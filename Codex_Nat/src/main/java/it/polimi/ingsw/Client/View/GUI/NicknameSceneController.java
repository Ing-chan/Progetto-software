package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.MessagesToServer.HandShakeMTS;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MTCtype;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;
import it.polimi.ingsw.Network.Client.ClientConnectionHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class  NicknameSceneController extends Gui implements Initializable {
    @FXML
    public TextField nicknameField;
    @FXML
    public Button backButton;
    @FXML
    public Button continueButton;
    public ImageView errore1;
    public ImageView errore2;
    public Text errore3;

    @FXML
    public void onBackButton(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                StageController.switchScene(event, "fxml/pick-connection.fxml", "Connection Type");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void onContinueButton(ActionEvent event){
        ClientConnectionHandler clientConnectionHandler = ClientController.getInstance().getClientConnectionHandler();
        MessageToClient msg;
        clientConnectionHandler.SendMessageToSever(new HandShakeMTS(nicknameField.getText()));
        msg = clientConnectionHandler.GetMessageToClient();
        boolean isOkay = msg.getType().equals(MTCtype.OKAY);
        if(isOkay){
            ClientController.getInstance().setUsername(nicknameField.getText());
            Platform.runLater(() -> {
                try {
                    StageController.switchScene(event, "fxml/game-settings.fxml", "Set game preferences");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            errore1.setVisible(true);
            errore2.setVisible(true);
            errore3.setVisible(true);
            nicknameField.clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errore1.setVisible(false);
        errore2.setVisible(false);
        errore3.setVisible(false);
    }
}