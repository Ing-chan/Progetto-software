package it.polimi.ingsw.Client.View.CLI.Views;

import it.polimi.ingsw.Network.Virtuals.VirtualView;
import it.polimi.ingsw.Server.Model.Enums.TurnPhase;

import java.util.ArrayList;

public class TurnView extends ViewElement {
    String activePlayer;
    TurnPhase turnPhase;

    public TurnView() {
        this.activePlayer = VirtualView.getInstance().getActivePlayer();
        this.turnPhase = VirtualView.getInstance().getTurnPhase();
        this.player = VirtualView.getInstance().getVirtualPlayer();
    }
    @Override
    public ArrayList<String> getPrint(ArrayList<String> output) {

        output.add("");
        if(activePlayer.equals(player.getUsername()))
        {
            output.add("IT'S YOUR TURN!  -->" +turnPhase.toString().substring(6));
            output.add("COMMAND: ");
        }else{
            output.add("IT'S "+activePlayer+" TURN");
        }

        return output;
    }
}
