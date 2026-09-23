package it.polimi.ingsw.Client.Controller.PlayablePhases;

import it.polimi.ingsw.Client.Controller.GameCommand;

public class InitialState extends PlayableState {

    public static void execute() {

        GameCommand.SetConnection.TUICommand();
        GameCommand.SetUsername.TUICommand();
        GameCommand.JoinOrCreate.TUICommand();
        System.out.println("Starting game");
    }
}
