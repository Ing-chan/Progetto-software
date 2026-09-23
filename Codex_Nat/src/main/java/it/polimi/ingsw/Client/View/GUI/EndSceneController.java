package it.polimi.ingsw.Client.View.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class EndSceneController implements Initializable {

    @FXML
    private Text winnersName; // Text node to display the winner's name

    @FXML
    private Text scores; // Text node to display the scores

    // Map containing player names and their respective scores
    private final Map<String, Integer> playerScores = GameBoardController.getScoreboard();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Display the winner's name (example: the first player with the highest score)
        String winner = playerScores.entrySet().stream()
                .max(Map.Entry.comparingByValue()) // Find the entry with the highest value (score)
                .map(Map.Entry::getKey) // Get the key (player's name) of the entry
                .orElse("Nessun vincitore trovato"); // Default message if no winner is found

        winnersName.setText(winner); // Set the winner's name in the Text node

        // Construct the text for scores
        StringBuilder scoresText = new StringBuilder();
        for (Map.Entry<String, Integer> entry : playerScores.entrySet()) {
            scoresText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n"); // Append each player's name and score
        }
        scores.setText(scoresText.toString()); // Set the constructed scores text in the Text node
    }
}
