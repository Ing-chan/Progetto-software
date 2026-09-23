package it.polimi.ingsw.Client.View.CLI.Views;

import it.polimi.ingsw.Network.Virtuals.VirtualScoreBoard;
import it.polimi.ingsw.Network.Virtuals.VirtualView;

import java.util.ArrayList;

public class ScoreBoardView extends ViewElement {

    VirtualScoreBoard scoreBoard;

    public ScoreBoardView() {
        this.scoreBoard= VirtualView.getInstance().getVirtualScoreBoard();
    }
    @Override
    public ArrayList<String> getPrint(ArrayList<String> output) {
        StringBuilder string = new StringBuilder();
        output.add("");
        string.append("╔LEADERBOARD");
        output.add(string.toString());
        string.setLength(0);
        for(String playerPoints: scoreBoard.getPrint()){
            string.append("║ "+playerPoints);
            output.add(string.toString());
            string.setLength(0);
        }
        string.append("╚LEADERBOARD");
        output.add(string.toString());

        return output;
    }
}
