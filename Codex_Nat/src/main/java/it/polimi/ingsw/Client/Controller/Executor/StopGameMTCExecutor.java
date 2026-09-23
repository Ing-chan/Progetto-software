package it.polimi.ingsw.Client.Controller.Executor;

import it.polimi.ingsw.Client.Controller.PlayablePhases.DisconnectionState;
import it.polimi.ingsw.Client.View.GUI.StageController;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Network.Virtuals.VirtualView;
import it.polimi.ingsw.Server.Controller.MessagesToClient.StopGameMTC;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * This class implements the execution of {@link it.polimi.ingsw.Controller.Server.MessagesToClient.StopGameMTC}.
 * It handles the logic to execute {@link it.polimi.ingsw.Controller.Client.PlayablePhases.DisconnectionState}.
 */
public class StopGameMTCExecutor implements MTCExecutor{

    /**
     * Executes the logic to handle {@link it.polimi.ingsw.Controller.Server.MessagesToClient.StopGameMTC}.
     * It invokes {@link it.polimi.ingsw.Controller.Client.PlayablePhases.DisconnectionState#execute()}.
     *
     * @param stopGameMTC The message to execute.
     */
    public static void execute(StopGameMTC stopGameMTC) {

        if(VirtualView.getInstance().getView().equals("-cli"))
        {
            //CLI
            DisconnectionState.execute();
        }else{
            //GUI
            LoggerUtility.getLogger().warning("a Player has disconnected");

            Platform.runLater(() -> {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("GAME HAS BEEN DISCONNECTED");
            alert.setHeaderText("A player has disconnected from the game.");
            alert.setContentText("Game ended.");

            ButtonType okButton = new ButtonType("Ok");
            alert.getButtonTypes().setAll(okButton);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == okButton) {
                Platform.runLater(() -> {
                    StageController.changePrimaryScene("fxml/end-game.fxml", "End Game");
                });}

            });}

        }


    }
