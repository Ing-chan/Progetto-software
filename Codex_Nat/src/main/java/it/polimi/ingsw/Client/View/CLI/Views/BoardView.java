package it.polimi.ingsw.Client.View.CLI.Views;

import it.polimi.ingsw.Network.Virtuals.VirtualView;

import java.util.ArrayList;

public class BoardView extends ViewElement{

    public BoardView() {
        this.player= VirtualView.getInstance().getVirtualPlayer();
    }
    @Override
    public ArrayList<String> getPrint(ArrayList<String> output) {
        output.add(player.getUsername()+"'S BOARD ↓            YOUR SCORE: "+ VirtualView.getInstance().getVirtualScoreBoard().getScore(player.getUsername()));
        output.addAll(player.getVirtualBoard().getPrint());
        return output;
    }
}
