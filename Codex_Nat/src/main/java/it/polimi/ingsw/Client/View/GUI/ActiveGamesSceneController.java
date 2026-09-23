package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.MessagesToServer.ActiveGamesRequestMTS;
import it.polimi.ingsw.Client.Controller.MessagesToServer.CanIPlayMTS;
import it.polimi.ingsw.Network.Client.ClientConnectionHandler;
import it.polimi.ingsw.Server.Controller.MessagesToClient.CurrentGamesMTC;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MTCtype;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActiveGamesSceneController extends Gui implements Initializable {

    @FXML
    public Button refreshButton;
    @FXML
    private VBox activeGamesBox;
    @FXML
    Button continueButton;

    private int finalGameId = 500;

    private final ArrayList<Button> gameButtons = new ArrayList<>();
    ClientConnectionHandler clientConnectionHandler;
    private Button selectedButton = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientConnectionHandler = ClientController.getInstance().getClientConnectionHandler();

        continueButton.setDisable(true);

        // Retrieve current games information

        CurrentGamesMTC msg = (CurrentGamesMTC) clientConnectionHandler.GetMessageToClient();

        if (msg != null) {
            // Update UI on the JavaFX Application Thread
            Platform.runLater(() -> setButtons(msg));
        } else {
            System.out.println("Received null message from server");
        }
    }

    public static int getGameId(String gameInfo) {
        Matcher matcher;
        do {
            Pattern pattern = Pattern.compile("Game ID:-?(\\d+)");
            matcher = pattern.matcher(gameInfo);
        } while (!matcher.find()); //todo è un crimine alla programmazione
        return Integer.parseInt(matcher.group(1));
    }

    private DropShadow createDropShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20);
        dropShadow.setSpread(0.5);
        dropShadow.setColor(Color.YELLOW);
        return dropShadow;
    }

    public void setButtons(CurrentGamesMTC message) {
        ArrayList<String> games = message.getCurrentGames();

        Platform.runLater(() -> {
            if (selectedButton != null) {
                selectedButton.setEffect(null);
                selectedButton = null;
            }

            activeGamesBox.getChildren().clear();

            for (String gameInfo : games) {
                Button button = createStyledButton(gameInfo);
                int gameID = getGameId(gameInfo);

                button.setOnAction(event -> {
                    handleButtonClick(gameID);
                });

                gameButtons.add(button);
                activeGamesBox.getChildren().add(button);
            }

            activeGamesBox.setAlignment(Pos.CENTER);
        });
    }


    private Button createStyledButton(String gameInfo) {
        Button button = new Button(gameInfo);
        button.setFont(Font.font("Luminari", 15));
        button.setStyle("-fx-background-color: rgba(186,139,72,0.5); " +
                "-fx-text-fill: black; " +
                "-fx-font-size: 15px; " +
                "-fx-padding: 10px 20px; " +
                "-fx-background-radius: 5px; " +
                "-fx-border-color: transparent;");
        return button;
    }

    private void handleButtonClick(int gameID) {
        // Rimuovi il DropShadow dal bottone precedente se esiste uno
        if (selectedButton != null) {
            selectedButton.setEffect(null);  // Rimuovi l'effetto DropShadow
        }

        // Trova il bottone corrispondente al gameID e aggiungi il DropShadow
        for (Button button : gameButtons) {
            if (button.getText().contains("Game ID:" + gameID)) {
                button.setEffect(createDropShadow());  // Aggiungi l'effetto DropShadow al bottone selezionato
                selectedButton = button;  // Aggiorna il bottone selezionato corrente
            } else {
                button.setEffect(null);  // Rimuovi l'effetto DropShadow dagli altri bottoni
            }
        }

        // Perform actions based on the gameId and playersConnected
        finalGameId = gameID;
        continueButton.setDisable(false);
    }



    @FXML
    public void onContinue(ActionEvent event) {
        ClientConnectionHandler clientConnectionHandler = ClientController.getInstance().getClientConnectionHandler();
        clientConnectionHandler.SendMessageToSever(new CanIPlayMTS(finalGameId, ClientController.getInstance().getUsername()));

        MessageToClient msg = clientConnectionHandler.GetMessageToClient();
        if (msg.getType().equals(MTCtype.NOTOKAY)) {
            try {
                onRefresh();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }else {

            try {
                StageController.switchScene(event, "fxml/waiting-room.fxml", "Waiting Room");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    public void onRefresh() throws IOException {
        // Clear current game buttons
        ClientController.getInstance().getClientConnectionHandler().SendMessageToSever(new ActiveGamesRequestMTS(ClientController.getInstance().getUsername(), ClientController.getInstance().getPlayersInMatch()));
        Platform.runLater(() -> StageController.changePrimaryScene("fxml/active-games.fxml", "List of Active Games"));}
}
