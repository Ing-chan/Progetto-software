package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Controller.MessagesToClient.CurrentGamesMTC;
import it.polimi.ingsw.Server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class GamesManager {

    static Logger logger = LoggerUtility.getLogger();

    private HashMap<Integer, GameController> gamesData;
    private static GamesManager gamesManagerInstance;

    private ConcurrentHashMap <String, Integer> ClientRooms;


    public GamesManager() {
        gamesData = new HashMap<>();
        ClientRooms = new ConcurrentHashMap<>();
        gamesManagerInstance = this;
    }

    public static GamesManager getInstance() {
        if (gamesManagerInstance == null) {
            gamesManagerInstance = new GamesManager();
        }
        return gamesManagerInstance;
    }

    public synchronized boolean isUnique(int gameID) {
        return gamesData.containsKey(gameID);
    }

    public void putGame(int key, GameController value) {
        gamesData.put(key, value);
    }

    public void removeGame(int key, GameController value) { gamesData.remove(key, value); }

    public GameController getGameController(int key) {
        //the case key == 0 is in case the player wants to join a random match. The number 0 is not created as a random seed of a game
        if(key!=0){
            return gamesData.get(key);
        }
//        List<ArrayList<Integer>> entryList = new ArrayList<>(gamesData.entrySet());
//        Random rand = new Random();
//        int index = rand.nextInt(games.size()-1);

        return gamesData.get(key);
    }


    public void notifyGames(String username , int playernumber){

        ArrayList<String> currentGames = getActiveGames(playernumber);
        Server.getHandle(username).sendMessage(new CurrentGamesMTC(currentGames));

    }

    public ArrayList<String> getActiveGames(int playerNumber) {
        ArrayList<String> CurrentGames = new ArrayList<>();

        for(GameController gameController : gamesData.values()) {
            if (!gameController.gameIsFull() && gameController.getGameModel().getMaxPlayers() == playerNumber) {
                CurrentGames.add("Game ID:" + gameController.getID() + " Players connected: " +gameController.getGameModel().getConnectedPlayers());
            }
        }
        return CurrentGames;
    }

    public void registerClientRoom(int gameID, String player){
        ClientRooms.put(player,gameID);
    }

    public void GameInterrupted(String username) {

        try {
            int gameID = ClientRooms.get(username);

            getGameController(gameID).removePlayer(username);
            getGameController(gameID).setGameDisconnected();

            removeGame(gameID, getGameController(gameID));
        }
        catch (NullPointerException e) {
            logger.info("Game is not found because game closed due to disconnection");
            //todo: gestire meglio il ritorno
        }

    }
}
