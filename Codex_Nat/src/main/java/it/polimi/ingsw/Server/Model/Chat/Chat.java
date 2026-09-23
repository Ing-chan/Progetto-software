package it.polimi.ingsw.Server.Model.Chat;

import java.util.ArrayList;

public class Chat {

    private final int messagesPerPlayer;


    private final ArrayList<ChatMessage> messages;

    public Chat() {
        this.messages = new ArrayList<>();
        messagesPerPlayer = 20; //todo
    }

    public void addMessage(ChatMessage msg) {
        if(!msg.msg().isEmpty()){
            messages.add(msg);
        }
    }


//    public ArrayList<String> downloadMessages2(String receiverPlayer) { //todo rifinire o cancellare
//
//        int broadcastCounter = 0;
//        int msgForPlayerCounter = 0;
//        boolean limitReached = false;
//        ArrayList<String> messagesToSend = new ArrayList<>();
//
//        for (int i = messages.size() - 1; i >= 0; i--) {
//            ChatMessage message = messages.get(i);
//            boolean isBroadcast = message.broadcast();
//            //boolean containsPlayer = receiverPlayer.equals(messages.get(i).receiver()) || receiverPlayer.equals(messages.get(i).sender());
//            boolean forPlayer = involvesPlayer(messages.get(i), receiverPlayer);
//
//            if (!forPlayer) {
//                continue; // skip messages that are neither broadcast nor for the player
//            }
//
//            if (isBroadcast) {
//                broadcastCounter++;
//            } else {
//                msgForPlayerCounter++;
//            }
//
//            if (msgForPlayerCounter + broadcastCounter == messagesPerPlayer) {
//                limitReached = true;
//            }
//
//            if ((limitReached && !isBroadcast) || broadcastCounter > messagesPerPlayer) {
//                messages.remove(i);
//                i++; // adjust the index after removal
//                if (isBroadcast) {
//                    broadcastCounter--;
//                }
//                continue; // skip to the next iteration
//            }
//
//            if (isBroadcast) {
//                messagesToSend.add(message.sender() + ": " + message.msg());
//            } else {
//                messagesToSend.add(message.sender() + " whispered to you: " + message.msg());
//            }
//        }
//        Collections.reverse(messagesToSend);
//        return messagesToSend;
//
//    }

    public ArrayList<String> downloadMessages(String receiverPlayer) {
        ArrayList<String> messagesToSend = new ArrayList<>();

        int messageCouter = 0;
        for (int i = messages.size() - 1; i >= 0; i--) {
            ChatMessage message = messages.get(i);
            boolean isBroadcast = message.broadcast();
            boolean forPlayer = involvesPlayer(message, receiverPlayer);

            if (!forPlayer) {
                continue; // skip messages that are neither broadcast nor for the player
            }
            if (messageCouter == messagesPerPlayer) {
                break;
            }
            if (isBroadcast) {
                messagesToSend.add(message.sender() + ": " + message.msg());
            } else if (message.receiver().equals(receiverPlayer)) {
                messagesToSend.add(message.sender() + " whispered to you: " + message.msg());
            } else {
                messagesToSend.add("you whispered to " + message.receiver() + ": " + message.msg());
            }
            messageCouter++;

        }

        return messagesToSend;

    }

    private boolean involvesPlayer(ChatMessage msg, String player) {
        return msg.broadcast() || msg.sender().equals(player) || msg.receiver().equals(player);
    }
}