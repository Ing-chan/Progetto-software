package it.polimi.ingsw.Server.Controller.MessagesToClient;


public class notOkayMTC extends MessageToClient {
    String message;

    public notOkayMTC(String message) {
        this.type = MTCtype.NOTOKAY;
        this.message = message;
    }

    @Override
    public void update() {

    }
    
    public String getMessage() {
        return message;
    }

}
