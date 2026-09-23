package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Controller.GameObservers.ChatObserver;
import it.polimi.ingsw.Server.Controller.GameObservers.EndOfTurnObserver;
import it.polimi.ingsw.Server.Controller.GameObservers.GameIsStarted;
import it.polimi.ingsw.Server.Controller.MessagesToClient.EndGameMTC;
import it.polimi.ingsw.Server.Controller.MessagesToClient.StopGameMTC;
import it.polimi.ingsw.Server.Model.Chat.ChatMessage;
import it.polimi.ingsw.Server.Model.Enums.GameState;
import it.polimi.ingsw.Server.Model.Enums.TurnPhase;
import it.polimi.ingsw.Server.Model.GameModel;
import it.polimi.ingsw.Server.Model.Player.Player;
import it.polimi.ingsw.Server.Server;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GameController {
    int gameID;
    GameModel gameModel;
    TurnHandler turnHandler;
    GameState gameState;
    ChatObserver chatObserver;
    EndOfTurnObserver endTurnObserver;
    GameIsStarted starGameObserver;


    //todo dare handle al gameController così che non deve prendersele ogni volta e intasare il mio bellissimo server


    static Logger logger = LoggerUtility.getLogger();

    public GameController(int playerNumber) {
        gameID = generateNewGameID();
        gameModel = new GameModel(playerNumber);
        turnHandler = new TurnHandler();
        this.gameState = GameState.INITIAL;
        starGameObserver = new GameIsStarted(gameID);
        logger.log(Level.FINE, "A new game was created: " + gameID + " room size: " + playerNumber);

    }


    private int generateNewGameID() {

        Random rand = new Random(); // Seed removed as per the comment
        int gameID;

        do {
            gameID = rand.nextInt(Integer.MAX_VALUE); // Generate a positive random integer
        } while (GamesManager.getInstance().isUnique(gameID));

        return gameID;
    }


    public GameModel getGameModel() {
        return gameModel;
    }

    public String getActivePlayer() {
        return turnHandler.getActivePlayer();
    }

    public int getID() {
        return gameID;
    }

    public void update() {

        switch (gameState) {
            case INITIAL: {
                turnHandler.changeTurn();
                endTurnObserver.update(); //this command could be in nextTurnPhase() with some work

                if (turnHandler.getNumTurn() == 1) {
                    gameModel.givePlayableCards();
                    gameState = GameState.RUNNING;
                }
                break;
            }
            case RUNNING: {
                //todo cambiare a 20
                int pointsToFinalPhase = 20;

                gameModel.updateScore(getActivePlayer());

                if (turnHandler.getTurnPhase() == TurnPhase.hasToEndTurn) {
                    if (gameModel.getMaxPoints() >= pointsToFinalPhase || gameModel.decksAreEmpty()) {
                        turnHandler.setLastTurn();
                    }
                    turnHandler.changeTurn();
                    endTurnObserver.update();
                }
                if (turnHandler.isLastTurn()) {
                    gameState = GameState.FINAL;
                    update();
                }
                turnHandler.nextTurnPhase();
                break;
            }

            case FINAL: {
                logger.info("\u001B[34m"+"Final State"+ "\u001B[31m");

                gameModel.CalculateFinalSCore();

                //Manda un Game Is Ended a Tutti
                for(Player player: gameModel.getPlayers())
                {

                    Server.getHandle(player.getNickname()).sendMessage(new EndGameMTC());
                }
                break;
            }

            case DISCONNECTED: {

                for(Player player: gameModel.getPlayers())
                {
                   Server.getHandle(player.getNickname()).sendMessage(new StopGameMTC());
                }
                logger.info("Stop game sent to players");

                break;
            }
            default:
                break;
        }

    }


    public void addMessage(ChatMessage message){
        gameModel.addMessage(message);
        chatObserver.update();
    }
    public TurnHandler getTurnHandler() {
        return turnHandler;
    }

    public void addPlayer(String username) {
        gameModel.addPlayer(username);
        turnHandler.addPlayer(username);


        if (gameIsFull()) {
            logger.log(Level.FINEST, "Game: " + gameID + " is ready to start!");

            for (Player player : gameModel.getPlayers()) {
                starGameObserver.add(player.getNickname());
            }
            endTurnObserver = new EndOfTurnObserver(turnHandler, gameModel.getPlayers());
            ArrayList<String> players= gameModel.getPlayers().stream().map(Player::getNickname).collect(Collectors.toCollection(ArrayList::new));
            chatObserver = new ChatObserver(turnHandler, players ,gameModel.getChat());
            gameModel.startGame();
            starGameObserver.update();
        }
    }

    public void removePlayer(String username){
        gameModel.removePlayer(username);
    }

    public void setGameDisconnected(){
        gameState = GameState.DISCONNECTED;
        update();
    }

    public boolean gameIsFull() {
        return gameModel.gameIsFull();
    }
}
