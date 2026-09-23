package it.polimi.ingsw.Client.View.CLI;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.GameCommand;
import it.polimi.ingsw.Client.Controller.MessagesToServer.ViewUtilitiesMTS;
import it.polimi.ingsw.Client.View.CLI.Views.*;
import it.polimi.ingsw.Network.Virtuals.VirtualView;
import it.polimi.ingsw.Server.Controller.MessagesToClient.ObjectiveMTC;
import it.polimi.ingsw.Server.Model.Enums.TurnPhase;

import java.util.ArrayList;
import java.util.HashMap;

public class Printer {

    int GameID;
    static Printer printerInstance = null;
    static HashMap<TurnPhase, ArrayList<String>> availableCommands;

    Printer(){
        GameID = ClientController.getInstance().getGameID();
        initializeCommands();
    }

    public static Printer getInstance() {
        if (printerInstance == null) printerInstance = new Printer();
        return printerInstance;
    }

    public void choseSecretObj(ObjectiveMTC msg){
        ArrayList<String> output = new ArrayList<>();

        ChooseSecretObjectiveView chooseSecretObjectiveView = new ChooseSecretObjectiveView(msg);

        GameTitle(output);

        output = chooseSecretObjectiveView.getPrint(output);

        clearConsole();

        for(String print: output) {
            System.out.println(print);
        }

    }

    public void update(){

        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> dummy1 = new ArrayList<>();
        ArrayList<String> dummy2 = new ArrayList<>();


        MyHandView myHandView = new MyHandView();
        DrawableCardView drawableCardView = new DrawableCardView();
        MyInventoryView myInventoryView = new MyInventoryView();
        BoardView boardView = new BoardView();
        ObjectiveView objectiveView = new ObjectiveView();
        ScoreBoardView scoreBoardView = new ScoreBoardView();
        TurnView turnView = new TurnView();
        ChatView chatView = new ChatView();

        GameTitle(output);
        output.addAll(ParallelPrint(myInventoryView.getPrint(dummy1),scoreBoardView.getPrint(dummy2)));
        dummy1.clear();
        dummy2.clear();
        output.addAll(ParallelPrint(drawableCardView.getPrint(dummy1),boardView.getPrint(dummy2)));
        dummy1.clear();
        dummy2.clear();

        if(!VirtualView.getInstance().getVirtualPlayer().getPlayerCards().isEmpty()){
            output.addAll(ParallelPrint(myHandView.getPrint(dummy1),objectiveView.getPrint(dummy2)));
        }else{
            output=objectiveView.getPrint(output);
        }

        output=chatView.getPrint(output);
        output=turnView.getPrint(output);

        //printing available commands, input will be managed by the reader
        if(ClientController.getInstance().playerIsActive()) {
            int i = 0;
            for (String commandPrint : availableCommands.get(VirtualView.getInstance().getTurnPhase())) {
                output.add("[" + i + "] = " + commandPrint);
                i++;
            }
        }

        clearConsole();
        for(String print: output) {
            System.out.println(print);
        }
    }

    public void finalScore(){
        ArrayList<String> output = new ArrayList<>();

        //ask the server for the final leaderboard
        ClientController.getInstance().getClientConnectionHandler().SendMessageToSever(new ViewUtilitiesMTS(ClientController.getInstance().getGameID(), VirtualView.getInstance().getVirtualPlayer().getUsername()));
        ClientController.getInstance().getClientConnectionHandler().GetMessageToClient().update();

        ScoreBoardView scoreBoardView = new ScoreBoardView();

        GameTitle(output);
        output = scoreBoardView.getPrint(output);
        output.add("GAME HAS ENDED!");
        for(String print: output) {
            System.out.println(print);
        }

    }

    private ArrayList<String> ParallelPrint(ArrayList<String> boardPrint, ArrayList<String> drawcardprint) {
        ArrayList<String> paralelPrint = new ArrayList<>();

        int offset = 0;
        for(String Bstring: boardPrint)
        {
            if(offset<drawcardprint.size()) {
                paralelPrint.add(Bstring.concat(drawcardprint.get(offset)));
                offset++;
            }else{
                paralelPrint.add(Bstring);
            }
        }

        if(offset!=drawcardprint.size()-1) {
            for (; offset < drawcardprint.size(); offset++) {
                paralelPrint.add(" ".repeat(3).concat(drawcardprint.get(offset)));
            }
        }

        return paralelPrint;
    }

    private void GameTitle(ArrayList<String> output) {

        output.add("≡".repeat(80));
        output.add(" _____           _             _   _       _                   _ _     ");
        output.add("/  __ \\         | |           | \\ | |     | |                 | (_)    ");
        output.add("| /  \\/ ___   __| | _____  __ |  \\| | __ _| |_ _   _ _ __ __ _| |_ ___ ");
        output.add("| |    / _ \\ / _` |/ _ \\ \\/ / | . ` |/ _` | __| | | | '__/ _` | | / __|");
        output.add("| \\__/\\ (_) | (_| |  __/>  <  | |\\  | (_| | |_| |_| | | | (_| | | \\__ \\");
        output.add(" \\____/\\___/ \\__,_|\\___/_/\\_\\ \\_| \\_/\\__,_|\\__|\\__,_|_|  \\__,_|_|_|___/");
        output.add("                                                                       ");
        output.add("≡".repeat(80));

    }

    private static void clearConsole() {
        // Sequenza di caratteri per pulire la console
        System.out.print(" \n".repeat(50));
    }


    static void initializeCommands() {
        availableCommands = new HashMap<>();
        ArrayList<String> placeCardCommands = new ArrayList<>();
        ArrayList<String> drawCardCommands = new ArrayList<>();
        ArrayList<String> endTurnCommands = new ArrayList<>();

        placeCardCommands.add(GameCommand.PlayCard.toString());
        placeCardCommands.add(GameCommand.FlipCard.toString());
        placeCardCommands.add(GameCommand.SendChatMessage.toString());
        availableCommands.put(TurnPhase.hasToPlaceCard, placeCardCommands);

        drawCardCommands.add(GameCommand.DrawCard.toString());
        drawCardCommands.add(GameCommand.SendChatMessage.toString());
        availableCommands.put(TurnPhase.hasToDrawCard, drawCardCommands);

        endTurnCommands.add(GameCommand.EndTurn.toString());
        availableCommands.put(TurnPhase.hasToEndTurn, endTurnCommands);
    }
}