package it.polimi.ingsw.Client.View.GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartSceneController extends Gui implements Initializable {

    @FXML
    public Button exitButton;
    @FXML
    private Button playButton;
    @FXML
    private AnchorPane scenePane;

    Stage stage;

    @FXML
    public void onPlay(ActionEvent event) throws Exception{
        Platform.runLater(() -> {
            try {
                StageController.switchScene(event, "fxml/pick-connection.fxml", "Connection Type");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void onExit(ActionEvent actionEvent) {
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}