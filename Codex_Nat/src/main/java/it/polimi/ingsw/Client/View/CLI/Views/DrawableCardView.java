package it.polimi.ingsw.Client.View.CLI.Views;


import it.polimi.ingsw.Client.View.CLI.ColorPrint;
import it.polimi.ingsw.Network.Virtuals.VirtualCard;
import it.polimi.ingsw.Network.Virtuals.VirtualView;
import it.polimi.ingsw.Server.Model.Enums.CardType;

import java.util.ArrayList;

public class DrawableCardView extends ViewElement {

    ArrayList<VirtualCard> drawableCards;
    int remainingCards;

    public DrawableCardView() {
        this.drawableCards = VirtualView.getInstance().getVirtualDrawableCards().getDrawablaCards();
        this.remainingCards = VirtualView.getInstance().getVirtualDrawableCards().getRemainingCards();
    }

    @Override
    public ArrayList<String> getPrint(ArrayList<String> output) {

        StringBuilder string = new StringBuilder();

        //header of drawable card grafics
        output.add("╔" + "═".repeat(9) + "╗   ");

        for (VirtualCard card : drawableCards) {
            int i = 0;
            for (String cardString : card.getPrint()) {
                if (i < 3) {
                    if (i == 1) {
                        if (card.getCardType().equals(CardType.RESOURCECARD)) {
                            if (card.getFace()) {
                                output.add("R " + ColorPrint.get(card.getCardColor()) + cardString + ColorPrint.getRstCOLOR() + " " + card.getID() + " ");
                            } else {
                                output.add("R " + ColorPrint.get(card.getCardColor()) + cardString + ColorPrint.getRstCOLOR() + " ║   ");
                            }
                        } else {
                            if (card.getFace()) {
                                output.add("G " + ColorPrint.get(card.getCardColor()) + cardString + ColorPrint.getRstCOLOR() + " " + card.getID() + " ");
                            } else {
                                output.add("G " + ColorPrint.get(card.getCardColor()) + cardString + ColorPrint.getRstCOLOR() + " ║   ");
                            }
                        }
                    } else {
                        output.add("║ " + ColorPrint.get(card.getCardColor()) + cardString + ColorPrint.getRstCOLOR() + " ║   ");
                    }
                } else {
                    if (!card.equals(drawableCards.getLast())) {
                        output.add("║         ║   ");
                    }
                    break;
                }
                i++;
            }
        }
        output.add("╚" + "═".repeat(9) + "╝   ");

        //se ho meno carte del previsto metto dei filler per non scombinare la stampa
        for (int i = 24; i > drawableCards.size() * 4; i--) {
            output.add("              ");
        }

        output.add("DRAWABLE CARDS ↑  (" + remainingCards + ")");
        //objectives and cost
        for (VirtualCard card : drawableCards) {
            string.append(card.getDescription());
            if (!string.isEmpty()) {
                output.add(card.getID() + ") " + string);
                string.setLength(0);
            }
        }
        output.add("≡".repeat(80));

        return output;
    }
}