package it.polimi.ingsw.Client.View.CLI;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.GameCommand;
import it.polimi.ingsw.Network.Virtuals.VirtualView;
import it.polimi.ingsw.Server.Controller.GameObservers.Observer;
import it.polimi.ingsw.Server.Model.Enums.TurnPhase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Reader implements Runnable, Observer {

    static HashMap<TurnPhase, ArrayList<GameCommand>> availableCommands;
    boolean hasInput;
    boolean isReading;
    boolean end;
    boolean idlePhase;
    static Reader readerInstance = null;
    Scanner scanner;

    public Reader() {
        scanner = new Scanner(System.in);
        isReading = true;
        hasInput = false;
        idlePhase = false;
        end = false;
//        VirtualView.getInstance().subscribeToThis(this);
        initializeCommands();
    }

    public static Reader getInstance() {
        if (readerInstance == null) readerInstance = new Reader();
        return readerInstance;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {

                    while(!hasInput  && !idlePhase) {
                        this.wait();
                    }

                    if(end)
                    {
                        break;
                    }

                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }

                //the reader processes the input
                if(!idlePhase) {
                    chooseCommand();
                    isReading = false;
                    hasInput = false;
                    this.notifyAll();
                }else{
                    chooseCommand();
                }
            }
        }
    }


    private void chooseCommand() {
        VirtualView view = VirtualView.getInstance();
        TurnPhase turnPhase = view.getTurnPhase();

//        TurnPhase turnPhase = ClientController.getInstance().playerIsActive() ? view.getTurnPhase() : TurnPhase.hasToEndTurn;
        int index;

        do {
            index = getIntInput();
            availableCommands.get(turnPhase).get(index).TUICommand();

        }while (commandIs(GameCommand.FlipCard, index) || commandIs(GameCommand.SendChatMessage, index));
    }

    public void idleClient(){
        idlePhase = true;
    }

    public void activeClient(){
        idlePhase = false;
    }

    public int getIntInput() {
        VirtualView view = VirtualView.getInstance();
        System.out.println("Enter input: ");
//        TurnPhase turnPhase = ClientController.getInstance().playerIsActive() ? view.getTurnPhase() : TurnPhase.hasToEndTurn;
        TurnPhase turnPhase = view.getTurnPhase();
        int input = -1;
        do {
            try {
                input = scanner.nextInt();
            } catch (InputMismatchException ignored) {
                System.out.println("Invalid input, retry...");
            }
        } while (input < 0 || input > availableCommands.get(turnPhase).size());
        return input;
    }

    public boolean isReading() {
        return isReading;
    }

    static void initializeCommands() {
        //todo capire come cavolo funziona un treeset ed usare quello
        availableCommands = new HashMap<>();
        ArrayList<GameCommand> placeCardCommands = new ArrayList<>();
        ArrayList<GameCommand> drawCardCommands = new ArrayList<>();
        ArrayList<GameCommand> endTurnCommands = new ArrayList<>();

        placeCardCommands.add(GameCommand.PlayCard);
        placeCardCommands.add(GameCommand.FlipCard);
        placeCardCommands.add(GameCommand.SendChatMessage);
        availableCommands.put(TurnPhase.hasToPlaceCard, placeCardCommands);

        drawCardCommands.add(GameCommand.DrawCard);
        drawCardCommands.add(GameCommand.SendChatMessage);
        availableCommands.put(TurnPhase.hasToDrawCard, drawCardCommands);

        endTurnCommands.add(GameCommand.SendChatMessage);
        availableCommands.put(TurnPhase.hasToEndTurn, endTurnCommands);
    }

    static boolean commandIs(GameCommand command, int choice){
        return availableCommands.get(VirtualView.getInstance().getTurnPhase()).get(choice).equals(command);
    }

    public void hasInput(){
        this.hasInput=true;
        this.isReading=true;
    }

    public void end() {
        end = true;
        hasInput=true;
    }

    @Override
    public void update() {
        if(ClientController.getInstance().playerIsActive()){
        }
    }
}
