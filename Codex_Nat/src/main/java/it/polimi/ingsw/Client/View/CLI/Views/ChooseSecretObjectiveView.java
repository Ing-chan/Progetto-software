package it.polimi.ingsw.Client.View.CLI.Views;

import it.polimi.ingsw.Network.Virtuals.VirtualSecretObj;
import it.polimi.ingsw.Network.Virtuals.VirtualView;
import it.polimi.ingsw.Server.Controller.MessagesToClient.ObjectiveMTC;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public class ChooseSecretObjectiveView extends ViewElement {

    ArrayList<String> obj1;
    ArrayList<String> obj2;

    public ChooseSecretObjectiveView(ObjectiveMTC msg) {
        obj1 = msg.getObjective1();
        obj2 = msg.getObjective2();
    }

    @Override
    public ArrayList<String> getPrint(ArrayList<String> output) {

        ArrayList<String> objective1 = obj1;
        String ID1 = obj1.getFirst();
        ArrayList<String> objective2 = obj2;
        String ID2 = obj2.getFirst();

        StringBuilder string = new StringBuilder();

        output.add("CHOOSE YOUR SECRET OBJECTIVE!");
        output.add("╔════════════════════╗");

        for(int i=1;i<6;i++) {
            string.append("║  ");
            string.append(objective1.get(i)).append("  ").append(objective2.get(i)).append("  ║  ");
            if(i==3)
            {
                string.append("►").append(objective1.getLast());
            }
            if(i==4)
            {
                string.append("►").append(objective2.getLast());
            }
            output.add(string.toString());
            string.setLength(0);
        }


        string.append("╚═");
        if(ID1.length()==2){
            string.append("════").append(ID1).append("═══");
        }else{
            string.append("═══").append(ID1).append("═══");
        }

        if(ID2.length()==2){
            string.append("════").append(ID2).append("═══");
        }else{
            string.append("═══").append(ID2).append("═══");
        }
        string.append("═╝");

        output.add(string.toString());

        output.add("");
        output.add("[0] left objective\n[1] right objective\n");
        return output;
    }
}
