package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Client.Controller.MessagesToServer.*;
import it.polimi.ingsw.Client.View.CLI.Printer;
import it.polimi.ingsw.Client.View.CLI.Reader;
import it.polimi.ingsw.Network.Client.ClientConnectionHandler;
import it.polimi.ingsw.Network.Virtuals.VirtualCard;
import it.polimi.ingsw.Network.Virtuals.VirtualPlayer;
import it.polimi.ingsw.Network.Virtuals.VirtualView;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MTCtype;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;
import it.polimi.ingsw.Server.Controller.MessagesToClient.ObjectiveMTC;
import it.polimi.ingsw.Server.Controller.MessagesToClient.notOkayMTC;
import it.polimi.ingsw.Server.Model.Exceptions.CardNotInHandException;
import it.polimi.ingsw.Server.Model.Exceptions.InvalidPositionException;

import java.awt.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.Math.min;


//Per Matteo, scendi e leggiti i commenti
public enum GameCommand {

    SetConnection() {
        @Override
        public void TUICommand() {
            Scanner scanner = new Scanner(System.in);
            String connection = null;
            int num = -1;
            System.out.println("choose the protocol you want to connect with. \n[0] RMI\n[1] Socket");


            do {
                if (scanner.hasNextInt()) {
                    num = scanner.nextInt();
                    if (num == 0) {
                        connection = "RMI";
                        break;
                    } else if (num == 1) {
                        connection = "Socket";
                        break;
                    } else {
                        System.out.println("Input is not valid. Retry...");
                    }
                } else {
                    System.out.println("Format of input is 0/1");
                    scanner.next(); //discard of the incorrect input
                }
            } while (num != 0 && num != 1);


            String address = getInput("enter SERVER IP address: ");

            //todo controllo in caso ci siano 2 con lo stesso nickname

            new ClientController(connection, address);
        }

        @Override
        public String toString() {
            return null;
        }
    },

    JoinOrCreate() {
        @Override
        public void TUICommand() {
            int number = -1;

            while (number != 1 && number != 0) {

                String input = getInput("Join or create a game:\n[0] Create a new game\n[1] Join an existing Game");
                try {
                    number = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Wrong Input!!");
                }
            }
            GameCommand.ChoosePlayersNumber.TUICommand();
            switch (number) {
                case 0:
                    GameCommand.CreateGame.TUICommand();
                    break;
                case 1:
                    GameCommand.JoinGame.TUICommand();
                    break;
                default:
                    break;
            }

            MessageToClient msg;

            do {
                msg = ClientController.getInstance().getClientConnectionHandler().GetMessageToClient();
            } while (!msg.getType().equals(MTCtype.GAMESTARTED));

            ClientController.getInstance().setGameID(msg.getGameID());


        }


        //TODO mettere qualcosa che non returni null
        @Override
        public String toString() {
            return null;
        }
    },

    SetUsername() {
        @Override
        public void TUICommand() {
            ClientConnectionHandler clientConnectionHandler = ClientController.getInstance().getClientConnectionHandler();
            MessageToClient msg;
            Scanner scanner = new Scanner(System.in);
            String username;
            do {
                System.out.println("choose an username");
                username = scanner.nextLine();
                clientConnectionHandler.SendMessageToSever(new HandShakeMTS(username));
                msg = clientConnectionHandler.GetMessageToClient();

            } while (msg.getType() != MTCtype.OKAY);
            ClientController.getInstance().setUsername(username);
        }

        @Override
        public String toString() {
            return null;
        }
    },


    ChoosePlayersNumber() {
        @Override
        public void TUICommand() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose the number of players you want in your game (from 2 to 4)");
            String input;
            int number = 0;
            //keeps asking for a valid input
            do {
                try {
                    input = scanner.nextLine();
                    number = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("You didn't input a valid number.");
                    System.out.println("Choose the number of players you want in your game");
                }
            } while (number > 4 || number < 2);
            ClientController.getInstance().setPlayersInMatch(number);
        }

        @Override
        public String toString() {
            return null;
        }
    },
    CreateGame() {
        @Override
        public void TUICommand() {
            ClientController.getInstance().getClientConnectionHandler().SendMessageToSever(new NewGameMTS(ClientController.getInstance().getUsername(), ClientController.getInstance().getPlayersInMatch()));
            System.out.println("Game Created. Waiting for players...");
        }

        @Override
        public String toString() {
            return "Create game";
        }
    },

    GetActiveMatches() {
        @Override
        public void TUICommand() {
            ClientConnectionHandler clientConnectionHandler = ClientController.instance.getClientConnectionHandler();
            System.out.println("List of active games");
            clientConnectionHandler.SendMessageToSever(new ActiveGamesRequestMTS(ClientController.getInstance().getUsername(), ClientController.getInstance().getPlayersInMatch()));
        }

        @Override
        public String toString() {
            return "Get active matches";
        }
    },
    SendChatMessage() {
        @Override
        public void TUICommand() {
            Scanner scanner = new Scanner(System.in);

            ClientController cli = ClientController.getInstance();
            VirtualView view = VirtualView.getInstance();


            System.out.println("Enter who do you want to send the message to");
            ArrayList<String> nicks = view.getPlayers();
            nicks.remove(cli.getUsername());

            for (int i = 0; i < nicks.size(); i++) {
                System.out.println("[" + i + "] " + nicks.get(i));
            }

            //printa come opzioni i nomi dei player
            System.out.println("[" + (nicks.size()) + "] " + "everyone");

            int input = -1;
            do {
                try {
                    input = scanner.nextInt();
                } catch (InputMismatchException ignored) {

                }
            } while (input < 0 || input > nicks.size());


            System.out.println("Enter the message the message to send (limit char: 30)");
            scanner.nextLine(); //clears buffer that is dirty after getting an int
            String message = scanner.nextLine();

            if (message.length() > 30) {
                message = message.substring(0, 29);
            }

            if (input < nicks.size()) {
                ClientController.getInstance().getClientConnectionHandler().SendMessageToSever(new ChatMTS(cli.getGameID(), cli.getUsername(), message, nicks.get(input), false));
            } else {
                ClientController.getInstance().getClientConnectionHandler().SendMessageToSever(new ChatMTS(cli.getGameID(), cli.getUsername(), message, null, true));

            }
            cli.update();
            Printer.getInstance().update();


        }


        @Override
        public String toString() {
            return "Send Chat message";
        }
    },

    FlipCard() {
        @Override
        public void TUICommand() {
            ClientController controller = ClientController.getInstance();
            VirtualView view = VirtualView.getInstance();

            if (view.getVirtualPlayer().getPlayerCards().size() == 1) {
                controller.flip(0);
            } else if (ClientController.getInstance().flip(Integer.parseInt(getInput("Which card would you like to flip?\n [0] left\n [1] center\n [2] right")))) {
                System.out.println("card flipped");
            } else {
                System.out.println("invalid number");
            }
            Printer.getInstance().update();
        }

        @Override
        public String toString() {
            return "Flip Card";
        }
    },
    JoinGame() {
        @Override
        public void TUICommand() {
            MessageToClient msg;
            boolean flag;
            Scanner scanner = new Scanner(System.in);
            ClientConnectionHandler clientConnectionHandler = ClientController.instance.getClientConnectionHandler();

            do {
                flag = false;
                GetActiveMatches.TUICommand();
                //waits for response for the current games and executes the message
                clientConnectionHandler.GetMessageToClient().update();

                boolean wrongInput = true;
                String command;
                while (wrongInput) {
                    System.out.println("Digit the GameID or type \"random\" for a random room:  (\"R\"  for refresh the page, \"B\"  to exit)");
                    command = scanner.nextLine();
                    try {
                        if (command.equals("R") || command.equals("r")) {
                            System.out.println("Reloading the available games...\n\n\n");
                            GetActiveMatches.TUICommand();
                            clientConnectionHandler.GetMessageToClient().update();
                        } else if (command.equals("random")) {
                            clientConnectionHandler.SendMessageToSever(new CanIPlayMTS(0, ClientController.getInstance().getUsername()));
                            wrongInput = false;
                        } else if (command.equals("B") || command.equals("b")) {
                            //TODO
                            return;
                        } else {
                            clientConnectionHandler.SendMessageToSever(new CanIPlayMTS(Integer.parseInt(command), ClientController.getInstance().getUsername()));
                            wrongInput = false;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Wrong input!");

                    }
                }

                msg = clientConnectionHandler.GetMessageToClient();
                if (msg.getType().equals(MTCtype.NOTOKAY)) {
                    System.out.println(((notOkayMTC) msg).getMessage());
                    flag = true;
                }
            } while (flag);

            System.out.println("waiting for players to Join");
        }

        @Override
        public String toString() {
            return "Join game";
        }
    },

    JoinRandomGame() {
        @Override
        public void TUICommand() {
            ClientConnectionHandler clientConnectionHandler = ClientController.instance.getClientConnectionHandler();
            clientConnectionHandler.SendMessageToSever(new CanIPlayMTS(0, ClientController.getInstance().getUsername()));
        }

        @Override
        public String toString() {
            return null;
        }
    },

    JoinMatchByID() {
        @Override
        public void TUICommand() {
            ClientConnectionHandler clientConnectionHandler = ClientController.getInstance().getClientConnectionHandler();
            clientConnectionHandler.SendMessageToSever(new CanIPlayMTS(ClientController.getInstance().getGameID(), ClientController.getInstance().getUsername()));
        }

        @Override
        public String toString() {
            return "join match by ID";
        }
    },

    ChooseSecretObjective() {
        @Override
        public void TUICommand() {

            MessageToClient msg;
            ObjectiveMTC msgObj;

            ClientController cli = ClientController.getInstance();
            ClientConnectionHandler clientConnectionHandler = cli.getClientConnectionHandler();
            Scanner scanner = new Scanner(System.in);
            do {
                //sends a request for objectives to pick to the server
                clientConnectionHandler.SendMessageToSever(new GetObjectivesMTS(ClientController.getInstance().getGameID(), ClientController.getInstance().getUsername()));
                //gets the objectives

                msgObj = (ObjectiveMTC)clientConnectionHandler.GetMessageToClient();

                ArrayList<String> objectivesID = new ArrayList<>();
                objectivesID.add(msgObj.getObjective1().getFirst());
                objectivesID.add(msgObj.getObjective2().getFirst());

                int card;
                Printer.getInstance().choseSecretObj(msgObj);
                card = scanner.nextInt();
                while (card != 0 && card != 1 && card != 2) {
                        System.out.println("wrong input!!\n [0] left objective\n[1] left objective");
                        card = scanner.nextInt();
                }
                clientConnectionHandler.SendMessageToSever(new ChooseSecretObjectiveMTS(cli.getGameID(), cli.getUsername(), objectivesID.get(card)));
                msg = clientConnectionHandler.GetMessageToClient();

                if (!msg.getType().equals(MTCtype.OKAY)) {
                    System.out.println("Something went wrong");
                }
            } while (!msg.getType().equals(MTCtype.OKAY));

            System.out.println("Waiting for players...");
        }

        @Override
        public String toString() {
            return "choose secret objective";
        }
    },

    PlayCard() {
        @Override
        public void TUICommand() {

            boolean correctInput = false;
            ClientController cli = ClientController.getInstance();
            ClientConnectionHandler clientConnectionHandler = ClientController.instance.getClientConnectionHandler();
            Scanner scanner = new Scanner(System.in);
            VirtualPlayer player = VirtualView.getInstance().getVirtualPlayer();

            //case player has only one card in his hand. Can occurr at the start of the game or at the end
            if (player.getPlayerCards().size() == 1 && player.getVirtualBoard().getPlayablePositions().size() == 1) {
                VirtualCard cardToPlace = player.getPlayerCards().getFirst();
                Point coords = (Point) player.getVirtualBoard().getPlayablePositions().get(0);

                clientConnectionHandler.SendMessageToSever(new PlaceCardMTS(cli.getGameID(), cli.getUsername(), cardToPlace.getID(), coords, cardToPlace.getFace()));
                MessageToClient msg = clientConnectionHandler.GetMessageToClient();
                if (msg.getType().equals(MTCtype.OKAY)) {
                    ClientController.getInstance().update();
                    Printer.getInstance().update();
                    System.out.println("card Placed");
                }
                return;
            }

            String cardID = getInput("Choose the id of the card you would like to place. (for example G12)");

            do {
                try {

                    for(VirtualCard card: player.getPlayerCards())
                    {
                        if(card.getID().equals(cardID))
                        {
                            correctInput = true;
                            break;
                        }
                    }
                    if(!correctInput)
                    {
                       throw new CardNotInHandException();
                    }
                } catch (CardNotInHandException e) {
                    System.out.println(e.getMessage());
                    cardID = scanner.nextLine();
                }
            } while (!correctInput);
            correctInput = false;

            String input = getInput("Choose the position of the card. (for example: \"1 1\")");

            String[] words = input.split(" ");
            Point coords = null;

            do {
                try {
                    coords = new Point(Integer.parseInt(words[0]), Integer.parseInt(words[1]));
                    if(player.getVirtualBoard().getPlayablePositions().contains(coords)) {
                        correctInput = true;
                    }else{
                        throw new InvalidPositionException();
                    }
                } catch (InvalidPositionException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println(e.getMessage());
                    input = scanner.nextLine();
                    words = input.split(" ", 2);
                }
            } while (!correctInput);

            clientConnectionHandler.SendMessageToSever(new PlaceCardMTS(cli.getGameID(), cli.getUsername(), cardID, coords, player.findCard(cardID).getFace()));
            //the server will then send an Okay that the client will be waiting in Playing State
        }


        @Override
        public String toString() {
            return "Place card";
        }
    },
    DrawCard() {
        @Override
        public void TUICommand() {

            ClientController cli = ClientController.getInstance();
            ClientConnectionHandler clientConnectionHandler = cli.getClientConnectionHandler();
            VirtualView view = VirtualView.getInstance();
            ArrayList<VirtualCard> drawableCards = view.getVirtualDrawableCards().getDrawablaCards();

            System.out.println("Which card you want to draw:");

            String drawCards = "<";
            for (int i = 0; i < min(drawableCards.size(), 6); i++) {
                drawCards = drawCards.concat(" [" + (i + 1) + "] ");
            }
            drawCards = drawCards.concat(">");

            System.out.println(drawCards);


            Scanner scanner = new Scanner(System.in);
            int num = -1;

            do {
                try {
                    num = scanner.nextInt() - 1;
                } catch (InputMismatchException e) {
                    System.out.println("Wrong input retry");
                    scanner.next();
                }
            } while (num < 0 || num > min(VirtualView.getInstance().getVirtualDrawableCards().getDrawablaCards().size(), 6));

            String chosenCard = drawableCards.get(num).getID();
            clientConnectionHandler.SendMessageToSever(new DrawCardMTS(ClientController.getInstance().getGameID(), ClientController.getInstance().getUsername(), chosenCard));
            MessageToClient msg = clientConnectionHandler.GetMessageToClient();

            //receiving Okay to confirm DrawCard
            if (msg.getType().equals(MTCtype.OKAY)) {
                System.out.println("Ending your turn...");
                clientConnectionHandler.SendMessageToSever(new EndTurnMTS(ClientController.getInstance().getGameID(), ClientController.getInstance().getUsername()));
                //the next okay message is processed in the drawcardphase
            } else {
                // gestire se non va in porto, tipo avviso
                System.out.println("Something went wrong");
            }
        }

        @Override
        public String toString() {
            return "Draw card";
        }
    },

    RemoveCard() {
        @Override
        public void TUICommand() {
            System.out.println("Do you want to remove your placed card?");
            Scanner scanner = new Scanner(System.in);
            String reply = scanner.nextLine();
            if (reply.equals("yes")) {
                ClientConnectionHandler clientConnectionHandler = ClientController.instance.getClientConnectionHandler();
                ClientController cli = ClientController.getInstance();
                //cli.removeFromQueue();
                System.out.println("Card Removed.");
            }
        }

        @Override
        public String toString() {
            return "remove card";
        }
    },

    EndTurn() {
        @Override
        public void TUICommand() {
            ClientConnectionHandler clientConnectionHandler = ClientController.instance.getClientConnectionHandler();
            System.out.println("End of Turn");

            clientConnectionHandler.SendMessageToSever(new EndTurnMTS(ClientController.getInstance().getGameID(), ClientController.getInstance().getUsername()));
            //ClientController.getInstance().nextClientPhase();
            ClientController.getInstance().update();
        }

        @Override
        public String toString() {
            return "end turn";
        }
    };


    public static void HandleInput() {

        Reader reader = Reader.getInstance();

        // sveglia il thread per prendere l' input
        synchronized (reader) {
            reader.hasInput();
            reader.notify();
        }

        //aspetta che il reader prende e gestisca l'input
        synchronized (reader) {
             while (reader.isReading()) {
                try {
                    reader.notify();
                    reader.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public static void killReader() {

        Reader reader = Reader.getInstance();

        synchronized (reader) {
            reader.end();
            reader.notify();
        }
    }

    public static void    ReaderIdle(){
        Reader reader = Reader.getInstance();

        // sveglia il thred per prendere l' input
        synchronized (reader) {
            reader.idleClient();
            reader.notify();
        }

    }

    public static void ReaderStopIdle() {
        Reader reader = Reader.getInstance();

        // sveglia il thred per prendere l' input
        synchronized (reader) {
            reader.activeClient();
            reader.notify();
        }
    }

    public abstract void TUICommand();

    public abstract String toString();

    //Per ora sono stati implementati solo i comandi della view.
    //Successivamente quando implementeremo la GUI si potranno riciclare le stesse fasi semplicemente usando
    //GUIEvent invece di TUICommand

    //public abstract EventHandler<MouseEvent> GuiEvent(idk what it needs);

    public String getInput(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        return scanner.nextLine();
    }
}
