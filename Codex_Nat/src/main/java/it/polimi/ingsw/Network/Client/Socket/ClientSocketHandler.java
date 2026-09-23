package it.polimi.ingsw.Network.Client.Socket;

import it.polimi.ingsw.Client.Controller.MessagesToServer.MessageToServer;
import it.polimi.ingsw.Network.Client.ClientConnectionHandler;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MTCtype;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientSocketHandler implements ClientConnectionHandler {

    Socket socket;
    ObjectOutputStream outStream;
    ObjectInputStream inStream;

    private final Logger logger = LoggerUtility.getLogger();

    public ClientSocketHandler(String address, int port) {
        try {
            socket = new Socket(address, port);
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inStream = new ObjectInputStream(socket.getInputStream());
            logger.info("Connesso al server");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void SendMessageToSever(MessageToServer message) {
        try {
            outStream.reset();
            outStream.writeObject(message);
            outStream.flush();
        } catch (IOException e) {
            logger.severe("Connection closed");
        }
    }

    @Override
    public void WaitForMessage(MTCtype type){
        MessageToClient msg;
        do{
            msg = GetMessageToClient();
            msg.update();
        }while(!msg.getType().equals(type));
    }

    public MessageToClient GetMessageToClient() {
        try {

            MessageToClient msg = (MessageToClient) inStream.readObject();

            if(msg.getType().equals(MTCtype.DISCONNECTED))
            {
               msg.update();
            }

            return msg;
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            throw  new RuntimeException();
        }
    }

}
