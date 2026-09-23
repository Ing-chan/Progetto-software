package it.polimi.ingsw.Client.View.CLI.Views;

import it.polimi.ingsw.Client.View.CLI.ColorPrint;
import it.polimi.ingsw.Network.Virtuals.VirtualCard;
import it.polimi.ingsw.Network.Virtuals.VirtualView;

import java.util.ArrayList;

public class MyHandView extends ViewElement{

    public MyHandView() {
        this.player= VirtualView.getInstance().getVirtualPlayer();
    }

    @Override
    public ArrayList<String> getPrint(ArrayList<String> output) {

        ArrayList<VirtualCard> playerCards= player.getPlayerCards();
        ArrayList<String> printedCard = new ArrayList<>();
        StringBuilder string = new StringBuilder();

        //header of myhand grafics
        output.add("      ".repeat(playerCards.size()-1)+"YOUR CARDS ↓  "+"     ".repeat(playerCards.size()-1));
        string.append("╔");
        string.append("═══════════".repeat(playerCards.size()));
        string.append("╗");
        output.add(string.toString());

        //card grafics
        string.setLength(0);
        string.append("║");

        for(VirtualCard card: playerCards) {
            for (String cardString : card.getPrint()) {
               printedCard.add(ColorPrint.get(card.getCardColor()) + cardString + ColorPrint.getRstCOLOR());
            }
        }

        for(int i=0; i<playerCards.size();i++)
        {
            string.append("  ");
            string.append(printedCard.get(i*3));
            string.append("  ");
        }
        string.append("║");
        output.add(string.toString());
        string.setLength(0);

        string.append("║");
        for(int i=0; i<playerCards.size();i++)
        {
            string.append("  ");
            string.append(printedCard.get(1+i*3));
            string.append("  ");
        }
        string.append("║");
        output.add(string.toString());
        string.setLength(0);

        string.append("║");
        for(int i=0; i<playerCards.size();i++)
        {
            string.append("  ");
            string.append(printedCard.get(2+i*3));
            string.append("  ");
        }
        string.append("║");
        output.add(string.toString());
        string.setLength(0);

        //bottom part of grafic

        string.append("╚");

        for(VirtualCard card: playerCards)
        {
            string.append("════").append(card.getID()).append("════");
        }

        string.append("╝");
        output.add(string.toString());

        output.add(" ".repeat(output.getFirst().length()-1));
        output.add(" ".repeat(output.getFirst().length()-1));

        //objectives and cost
        string.setLength(0);
        for(VirtualCard card : player.getPlayerCards())
        {
            String obj = card.getDescription();

            if(!obj.isEmpty()) {
                string.append(card.getID()).append(") ").append(obj);
                output.add(string.toString());
                string.setLength(0);
            }
        }

        return output;
    }

}