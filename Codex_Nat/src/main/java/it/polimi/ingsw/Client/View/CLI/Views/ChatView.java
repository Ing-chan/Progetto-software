package it.polimi.ingsw.Client.View.CLI.Views;

import it.polimi.ingsw.Network.Virtuals.VirtualView;

import java.util.ArrayList;
import java.util.Collections;

public class ChatView extends ViewElement {

    ArrayList<String> messages;

    public ChatView() {
        this.messages = VirtualView.getInstance().getMessages();
    }

    @Override
    public ArrayList<String> getPrint(ArrayList<String> output) {


        output.add("≡".repeat(80));
        output.add("");
        output.add("╔════════════════════════════════════════════════════════════════════════════════════════════════╗");
        output.add("║                                              CHAT                                              ║");
        output.add("╠════════════════════════════════════════════════════════════════════════════════════════════════╣");

        for(String msg: messages){
            msg = "║" + msg + String.join("", Collections.nCopies(96-msg.length(), " ")) + "║";

            output.add(msg);
        }

        output.add("╚════════════════════════════════════════════════════════════════════════════════════════════════╝");

        return output;
    }
}