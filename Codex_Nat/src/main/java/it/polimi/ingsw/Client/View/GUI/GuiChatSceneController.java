package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.MessagesToServer.ChatMTS;
import it.polimi.ingsw.Network.Virtuals.VirtualView;
import it.polimi.ingsw.Server.Controller.GameObservers.Observer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GuiChatSceneController extends Gui implements Initializable, Observer {

    @FXML
    public TextField textMsg;
    @FXML
    public Button enterButton;
    @FXML
    public ChoiceBox choiceBox;
    @FXML
    public TextArea messagesBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        VirtualView view = VirtualView.getInstance();
        view.subscribeToThis(this);
        ArrayList<String> players = view.getPlayers();
        ArrayList<String> messages = view.getMessages();
        //inserisco nella choice box i nomi dei giocatori
        players.remove(ClientController.getInstance().getUsername());

//        enterButton = (Button) scene.lookup("#enterButton");

        for (String player : players) {
            choiceBox.getItems().add(player);
        }

        choiceBox.getItems().add("everyone");
        // Imposta un valore predefinito
        choiceBox.setValue("everyone");

        //suppongo che messages abbia già la forma "[nomeutente]: messaggio ", mi limito a stampare i messaggi
        messagesBox.setText(String.valueOf(messages));
    }

    // Metodo per ottenere il valore selezionato
    public String getSelectedValue() {
        return choiceBox.getValue().toString();
    }

    public void update() {
        VirtualView view = VirtualView.getInstance();
        ArrayList<String> players = view.getPlayers();
        ArrayList<String> messages = view.getMessages();

        refreshChat();
    }

    // funzione che invia il messaggio
    @FXML
    public void onEnter(ActionEvent actionEvent) {
        String msg = textMsg.getText();
        //logica per inviare il messaggio
        String choice = getSelectedValue();
        ClientController cli = ClientController.getInstance();
        String sender = cli.getUsername();
        String receiver = choice.equals("everyone") ? null : choice;
        boolean isBroadcast = choice.equals("everyone");
        cli.getClientConnectionHandler().SendMessageToSever(new ChatMTS(cli.getGameID(), sender, msg, receiver, isBroadcast));
    }

    public void refreshChat() {
        VirtualView view = VirtualView.getInstance();
        ArrayList<String> messages = view.getMessages();
        Platform.runLater(() -> messagesBox.setText(String.join("\n", messages))); // oppure messagesBox.setText(String.valueOf(messages));, vedi come stampa
    }

}
