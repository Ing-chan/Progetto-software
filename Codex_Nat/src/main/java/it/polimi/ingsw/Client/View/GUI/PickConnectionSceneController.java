package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.Controller.ClientController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PickConnectionSceneController extends Gui implements Initializable {
    @FXML
    public RadioButton rmiButton;
    @FXML
    public ToggleGroup connectionOption;
    @FXML
    public RadioButton socketButton;
    @FXML
    public Button continueButton;
    @FXML
    public Button backButton;
    @FXML
    public TextField ipField;


    @FXML
    public void onContinueButton(ActionEvent event){
        String selectedOption = getSelectedConnectionOption();
        String ipAddress = getIpAddress();
        new ClientController(selectedOption, ipAddress);

        Platform.runLater(() -> {
            try {
                StageController.switchScene(event, "fxml/nickname-scene.fxml", "Insert nickname");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String getSelectedConnectionOption() {
        if (socketButton.isSelected()) {
            return "Socket";
        } else {
            return "RMI";
        }
    }

    public String getIpAddress() {
        if (ipField.getText().equals("0")) {
            return "localhost";
        }
        return ipField.getText();
    }

    @FXML
    public void onBackButton(ActionEvent event){
        Platform.runLater(() -> {
            try {
                StageController.switchScene(event, "fxml/start-scene.fxml", "Welcome to Codex Naturalis");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        continueButton.setDisable(true);
    }

    @FXML
    public void onClickConnectionButton() {
        continueButton.setDisable(false);
    }
}