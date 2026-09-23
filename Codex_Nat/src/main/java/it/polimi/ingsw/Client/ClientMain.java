package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Controller.PlayablePhases.IdleState;
import it.polimi.ingsw.Client.Controller.PlayablePhases.InitialState;
import it.polimi.ingsw.Client.Controller.PlayablePhases.SetupState;
import it.polimi.ingsw.Client.View.GUI.Gui;
import it.polimi.ingsw.Network.Virtuals.VirtualView;
import javafx.application.Application;

public class ClientMain {


    public static void main(String[] args) {

//        args = new String[1];
//        args[0] = "-cli";

        if (args[0].equals("-cli")) {
        VirtualView.getInstance("-cli");
            InitialState.execute();
            SetupState.execute();
            IdleState.execute();
        } else {
            VirtualView.getInstance("-gui");
            Application.launch(Gui.class);
        }
    }
}
