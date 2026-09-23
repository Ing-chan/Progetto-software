package it.polimi.ingsw.Server.Controller.GameObservers;

import it.polimi.ingsw.Server.Controller.MessagesToClient.ObjectiveMTC;
import it.polimi.ingsw.Server.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.Server.Server;

import java.util.ArrayList;

public class AskForObjectives implements Observer{

    ArrayList<ObjectiveCard> objectives;
    String username;

    public AskForObjectives(ArrayList<ObjectiveCard> objectives, String username) {
        this.objectives = objectives;
        this.username=username;
    }

    @Override
    public void update() {
        Server.getHandle(username).sendMessage(new ObjectiveMTC(objectives));
    }
}
