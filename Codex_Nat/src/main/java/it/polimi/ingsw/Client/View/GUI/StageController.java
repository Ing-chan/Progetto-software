package it.polimi.ingsw.Client.View.GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StageController {

    private static Stage primaryStage;

    public static void switchScene(ActionEvent event, String fxml, String title) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(StageController.class.getResource(fxml)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle(title);
        stage.show();
    }
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void changePrimaryScene(String fxml, String title) {
        Platform.runLater(() -> {
            try {
                Stage stage = StageController.getPrimaryStage();
                Parent root = FXMLLoader.load(Objects.requireNonNull(StageController.class.getResource(fxml)));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle(title);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Gestisci l'eccezione in modo appropriato
            }
        });
    }

    public static void switchToEndScene(String fxml, String title) {
        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(Objects.requireNonNull(StageController.class.getResource(fxml)));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle(title);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Gestisci l'eccezione in modo appropriato
            }
        });
    }
}