package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.MessagesToServer.ActiveGamesRequestMTS;
import it.polimi.ingsw.Client.Controller.MessagesToServer.NewGameMTS;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameSettingsSceneController extends Gui implements Initializable {

    @FXML
    public RadioButton newGameButton;
    @FXML
    public ToggleGroup gameOption;
    @FXML
    public RadioButton joinGameButton;
    @FXML
    public Button continueButton;
    @FXML
    public RadioButton fourPlayersOption;
    @FXML
    public ToggleGroup playersOption;
    @FXML
    public RadioButton threePlayersOption;
    @FXML
    public RadioButton twoPlayersOption;

    public int getSelectedNumberOfPlayersOption() {
        if(twoPlayersOption.isSelected()){
            return 2;
        }
        if(threePlayersOption.isSelected()){
            return 3;
        }
        if (fourPlayersOption.isSelected()){
            return 4;
        }
        return 0;
    }

    @FXML
    public void onContinueButton(ActionEvent event) {
        int numberOfPlayers = getSelectedNumberOfPlayersOption();
        ClientController gui = ClientController.getInstance();
        gui.setPlayersInMatch(numberOfPlayers);

        if(newGameButton.isSelected()){
            ClientController.getInstance().getClientConnectionHandler().SendMessageToSever(new NewGameMTS(ClientController.getInstance().getUsername(), ClientController.getInstance().getPlayersInMatch()));

            Platform.runLater(() -> {
                try {
                    StageController.switchScene(event, "fxml/waiting-room.fxml", "Waiting Room");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if(joinGameButton.isSelected()){
            ClientController.getInstance().getClientConnectionHandler().SendMessageToSever(new ActiveGamesRequestMTS(ClientController.getInstance().getUsername(), ClientController.getInstance().getPlayersInMatch()));
            Platform.runLater(() -> {
                try {
                    StageController.switchScene(event, "fxml/active-games.fxml", "List of Active Games");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Disable the continue button initially
        continueButton.setDisable(true);

        // Add listeners to the ToggleGroups
        gameOption.selectedToggleProperty().addListener((observable, oldValue, newValue) -> checkSelection());
        playersOption.selectedToggleProperty().addListener((observable, oldValue, newValue) -> checkSelection());
    }

    private void checkSelection() {
        // Check if both ToggleGroups have a selected Toggle
        boolean gameSelected = gameOption.getSelectedToggle() != null && gameOption.getSelectedToggle().isSelected();
        boolean playersSelected = playersOption.getSelectedToggle() != null && playersOption.getSelectedToggle().isSelected();

        // Enable the continue button if both are selected
        continueButton.setDisable(!(gameSelected && playersSelected));
    }
}