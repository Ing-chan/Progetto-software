package it.polimi.ingsw.Client.View.CLI.Views;

import it.polimi.ingsw.Network.Virtuals.VirtualPlayer;
import it.polimi.ingsw.Server.Model.Player.Player;

import java.util.ArrayList;

public abstract class ViewElement {

    VirtualPlayer player;
    String GameID;
    abstract ArrayList<String> getPrint(ArrayList<String> output);

}
