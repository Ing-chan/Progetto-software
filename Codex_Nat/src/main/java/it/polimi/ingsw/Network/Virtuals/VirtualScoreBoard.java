package it.polimi.ingsw.Network.Virtuals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class VirtualScoreBoard implements Serializable {
    HashMap<String,Integer> virtualScoreBoard;
    ArrayList<String> ScorePrint;

    public VirtualScoreBoard(ArrayList<String> scorePrint) {
        virtualScoreBoard = new HashMap<>();
        this.ScorePrint=scorePrint;
    }

    public void put(String player,Integer score){
        virtualScoreBoard.put(player,score);
    }

    public int getScore(String player)
    {
        return virtualScoreBoard.get(player);
    }

    public ArrayList<String> getPrint(){
        return ScorePrint;
    }

}
