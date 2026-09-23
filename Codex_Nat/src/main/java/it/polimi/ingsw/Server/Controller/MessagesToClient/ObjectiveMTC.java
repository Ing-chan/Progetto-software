package it.polimi.ingsw.Server.Controller.MessagesToClient;

import it.polimi.ingsw.Server.Model.Cards.ObjectiveCard;

import java.util.ArrayList;

public class ObjectiveMTC extends MessageToClient {

    ArrayList<String> objectivesDATA1;
    ArrayList<String> objectivesDATA2;

    public ObjectiveMTC(ArrayList<ObjectiveCard> objs) {
        this.type = MTCtype.OBJECTIVE;

        objectivesDATA1 = new ArrayList<>();
        objectivesDATA1.add(objs.getFirst().getIDCard());
        objectivesDATA1.addAll(objs.getFirst().toPrintString());

        objectivesDATA2 = new ArrayList<>();
        objectivesDATA2.add(objs.getLast().getIDCard());
        objectivesDATA2.addAll(objs.getLast().toPrintString());
    }

    @Override
    public void update() {
    }

    public ArrayList<String> getObjective1() {
        return objectivesDATA1;
    }

    public ArrayList<String> getObjective2() {
        return objectivesDATA2;
    }

    public ArrayList<String> getObjectives() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add(objectivesDATA1.get(0));
        obj.add(objectivesDATA2.get(0));
        return obj;
    }
}
