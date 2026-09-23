package it.polimi.ingsw.Client.View.CLI.Views;

import it.polimi.ingsw.Network.Virtuals.VirtualView;

import java.util.ArrayList;

public class MyInventoryView extends ViewElement{

    public MyInventoryView() {
        this.player= VirtualView.getInstance().getVirtualPlayer();
    }
    @Override
    public ArrayList<String> getPrint(ArrayList<String> output) {

        output.add("YOUR INVENTORY ↓");
        output.addAll(player.getVirtualInventory().getPrint());
        output.add("YOUR POINTS: "+ VirtualView.getInstance().getVirtualScoreBoard().getScore(player.getUsername()));

        return output;

    }
}
