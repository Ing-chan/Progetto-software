package it.polimi.ingsw.Client.View.CLI.Views;

import it.polimi.ingsw.Network.Virtuals.VirtualObjective;
import it.polimi.ingsw.Network.Virtuals.VirtualView;

import java.util.ArrayList;

public class ObjectiveView extends ViewElement{

    public ObjectiveView() {
        this.player = VirtualView.getInstance().getVirtualPlayer();
    }
    @Override
    public ArrayList<String> getPrint(ArrayList<String> output) {

        ArrayList<VirtualObjective> objectiveCards= player.getPlayerObject();
        StringBuilder string = new StringBuilder();

        //header of myhand grafics
        output.add("         OBJECTIVES↓");
        string.append("╔═");
        string.append("═════════".repeat(objectiveCards.size()));
        string.append("═╗");
        output.add(string.toString());

        //card grafics
        string.setLength(0);


        for(int i=0;i<5;i++) {
            string.append("║  ");
            for (VirtualObjective obj : objectiveCards) {
                 string.append(obj.getObjPrint().get(i)).append("  ");
                 if(objectiveCards.getLast().equals(obj)&&i<3)
                {
                    string.append("║  " + "►").append(objectiveCards.get(i).getObjPrint().getLast());
                }else if(objectiveCards.getLast().equals(obj)){
                     string.append("║  ");

                 }
            }
            output.add(string.toString());
            string.setLength(0);
        }

        string.append("╚═");
        string.append("═════════".repeat(objectiveCards.size()));
        string.append("═╝");

        output.add(string.toString());

        return output;
    }
}
