package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Client.Controller.MessagesToServer.MessageToServer;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MTCtype;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;

public interface ClientConnectionHandler {

    void SendMessageToSever(MessageToServer message);

    MessageToClient GetMessageToClient();

    void WaitForMessage(MTCtype message);

}
