package it.polimi.ingsw.Client.View.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Gui extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        StageController.setPrimaryStage(stage);
        try{
            Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/start-scene.fxml")));
            Scene scene = new Scene(root);
            //stage.setFullScreen(true);
            stage.setTitle("Welcome to Codex Naturalis");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}