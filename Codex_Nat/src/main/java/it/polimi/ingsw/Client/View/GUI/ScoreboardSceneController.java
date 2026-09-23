package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.Controller.ClientController;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ScoreboardSceneController extends Gui implements Initializable {
    public ImageView player2;
    public ImageView player1;
    public ImageView player3;
    public ImageView player4;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        int score1 = GameBoardController.getPlayersScores().get(0);
        calculateScoreboardPosition(score1, player1);
        int score2 = GameBoardController.getPlayersScores().get(1);
        calculateScoreboardPosition(score2, player2);

        if (ClientController.getInstance().getPlayersInMatch() == 3) {
            player3.setVisible(true);
            int score3 = GameBoardController.getPlayersScores().get(2);
            calculateScoreboardPosition(score3, player3);
        }

        if (ClientController.getInstance().getPlayersInMatch() == 4) {
            player3.setVisible(true);
            int score3 = GameBoardController.getPlayersScores().get(2);
            calculateScoreboardPosition(score3, player3);
            player4.setVisible(true);
            int score4 = GameBoardController.getPlayersScores().get(3);
            calculateScoreboardPosition(score4, player4);
        }
    }

    public void calculateScoreboardPosition(int score, ImageView token){
        double x = token.getLayoutX();
        double y = token.getLayoutY();
        int i = 1;

        while(i <= score){
            if(i <= 2 || (i > 7 && i <= 10) || (i > 15 && i <= 18)){
                x = x + 90;
            }
            if(i == 3){
                x = x + 46;
                y = y - 85;
            }

            if(i > 3 && i <= 6 || (i > 11 && i <= 14)){
                x = x - 90;
            }

            if(i==7 || i == 11 || i == 15 || i == 19 || (i >21 && i<=23)){
                y = y - 90;
            }

            if(i == 20){
                x = x - 140;
                y = y - 42;
            }
            if(i == 21){
                x = x - 140;
                y = y + 42;
            }

            if(i == 24){
                x = x + 86;
                y = y -74;
            }
            if(i == 25){
                x = x + 86;
                y = y - 17;
            }
            if(i == 26){
                x = x+86;
                y = y+17;
            }
            if(i == 27){
                x = x+54;
                y = y+74;
            }
            if(i == 28){
                y = y+90;
            }
            if(i == 29){
                x = x-140;
                y = y-70;
            }

            i++;
        }
        token.setLayoutX(x);
        token.setLayoutY(y);
    }
}
