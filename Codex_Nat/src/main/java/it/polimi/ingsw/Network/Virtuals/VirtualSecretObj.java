package it.polimi.ingsw.Network.Virtuals;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.MessagesToServer.GetObjectivesMTS;
import it.polimi.ingsw.Server.Controller.MessagesToClient.ObjectiveMTC;

import java.io.Serializable;
import java.util.ArrayList;

public class VirtualSecretObj implements Serializable {

    ArrayList<String> objective1INFO;
    ArrayList<String> objective2INFO;


    public VirtualSecretObj() {
        //when initialized asks the server for two objectives
        ClientController cli = ClientController.getInstance();
        cli.getClientConnectionHandler().SendMessageToSever(new GetObjectivesMTS(cli.getGameID(), cli.getUsername()));

        //gets the objectives INFO
        ObjectiveMTC objectivemsg = (ObjectiveMTC) cli.getClientConnectionHandler().GetMessageToClient();

        objective1INFO = objectivemsg.getObjective1();
        objective2INFO = objectivemsg.getObjective2();
    }

    public String getObjective1ID() {
        return objective1INFO.getFirst();
    }

    public ArrayList<String> getObjective1PRINT() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 1; i < objective1INFO.size(); i++) {
            result.add(objective1INFO.get(i));
        }
        return result;
    }

    public String getObjective2ID() {
        return objective2INFO.getFirst();
    }

    public ArrayList<String> getObjective2PRINT() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 1; i < objective2INFO.size(); i++) {
            result.add(objective2INFO.get(i));
        }
        return result;
    }
}
