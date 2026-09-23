package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.MessagesToServer.GetObjectivesMTS;
import it.polimi.ingsw.Network.Client.ClientConnectionHandler;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Controller.MessagesToClient.ObjectiveMTC;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class SecretObjController extends Gui implements Initializable {
    static ArrayList<String> objectives;
    @FXML
    public ImageView obj1;
    @FXML
    public ImageView obj2;
    @FXML
    public Button confirmButton;

    private static String selectedObjective;

    @FXML
    public void handleImageClick(MouseEvent mouseEvent) {

        confirmButton.setDisable(false);
        ImageView clickedImage = (ImageView) mouseEvent.getSource();

        if (clickedImage.equals(obj1)) {
            selectedObjective = objectives.getFirst();
            obj1.setEffect(createDropShadow());
            obj2.setEffect(null);
        } else if (clickedImage.equals(obj2)) {
            selectedObjective = objectives.get(1);
            obj2.setEffect(createDropShadow());
            obj1.setEffect(null);
        }

        LoggerUtility.getLogger().info("Selected Objective: " + selectedObjective);
    }

    private DropShadow createDropShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20);
        dropShadow.setSpread(0.5);
        dropShadow.setColor(Color.YELLOW);
        return dropShadow;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmButton.setDisable(true);
        ClientConnectionHandler clientConnectionHandler = ClientController.getInstance().getClientConnectionHandler();

        // Sends a request for objectives to pick to the server
        clientConnectionHandler.SendMessageToSever(new GetObjectivesMTS(ClientController.getInstance().getGameID(), ClientController.getInstance().getUsername()));
        // Gets the objectives
        objectives = ((ObjectiveMTC)clientConnectionHandler.GetMessageToClient()).getObjectives();

        String id1 = objectives.get(0);
        String id2 = objectives.get(1);
        String url1 = "img/cards/obj/front/" + id1 + ".png";
        String url2 = "img/cards/obj/front/" + id2 + ".png";
        loadImage(obj1, url1);
        loadImage(obj2, url2);

        obj1.setOnMouseClicked(this::handleImageClick);
        obj2.setOnMouseClicked(this::handleImageClick);
    }

    private void loadImage(ImageView imageView, String imagePath) {
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                throw new RuntimeException("Image not found: " + imagePath);
            }
            Image image = new Image(imageUrl.toString());
            imageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load image: " + imagePath + ". Error: " + e.getMessage());
        }
    }

    public static String getSelectedObjective(){
        return selectedObjective;
    }

    @FXML
    public void onConfirm(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                StageController.switchScene(event, "fxml/starting-game.fxml", "Wait for players to place their first card");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}