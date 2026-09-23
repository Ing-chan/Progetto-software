package it.polimi.ingsw.Server.Model.Board;

import it.polimi.ingsw.Server.Model.Player.Player;

import java.io.Serializable;
import java.util.*;

public class ScoreBoard{
    LinkedHashMap<Player, Integer> pawns;

    public ScoreBoard(ArrayList<Player> players)
    {
        pawns = new LinkedHashMap<>();

        for(Player p: players){
        pawns.put(p,0);
        }
    }

    public ArrayList<Player> getPlayers(){
        return new ArrayList<>(pawns.keySet());
    }

    public String getFirstPlayer(){
        Player firstPlayer=null;
        int maxScore = 0;

        for (Map.Entry<Player,Integer> entry : pawns.entrySet()) {
            if ((entry.getValue() > maxScore)||(firstPlayer==null)){
                firstPlayer = entry.getKey();
                maxScore = entry.getValue();
            }
        }

        return firstPlayer.getNickname();
    }



    public int getPlayerScore(String nickname){

        int currentIndex = 0;

        for (Player player : pawns.keySet()) {
            if (player.getNickname().equals(nickname)) {
               return pawns.get(player);
            }
            currentIndex++;
        }

        return 0;
    }

    public void UpdatePlayerScore(String targetPlayer){
        for (Player player : pawns.keySet()) {
            if (player.getNickname().equals(targetPlayer)) {
              pawns.put(player,player.getScore());
            }
        }
    }


    public ArrayList<String> toPrint() {
        ArrayList<String> scorePrint = new ArrayList<>();
        boolean first = true;

        List<Map.Entry<Player, Integer>> playerLeaderboard = new ArrayList<>(pawns.entrySet());

        playerLeaderboard.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        for (Map.Entry<Player, Integer> entry : playerLeaderboard) {
            if(first&&entry.getKey().getScore()!=0){
                scorePrint.add(entry.getKey().getNickname()+"♛:  "+entry.getKey().getScore());
                first=false;
            }else{
                scorePrint.add(entry.getKey().getNickname()+":  "+entry.getKey().getScore());
            }
        }

        return scorePrint;
    }


    public void UpdatePlayerScoreFinal() {
        pawns.replaceAll((p, v) -> p.getFinalScore());
    }
}
