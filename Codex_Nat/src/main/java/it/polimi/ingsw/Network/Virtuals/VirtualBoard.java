package it.polimi.ingsw.Network.Virtuals;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class VirtualBoard implements Serializable {
    ArrayList<Point> playablePositions;
    ArrayList<String> boardPrint;

    public VirtualBoard(ArrayList<Point> playablePositions,ArrayList<String> boardPrint) {
        this.playablePositions = playablePositions;
        this.boardPrint = boardPrint;
    }

    public ArrayList<Point> getPlayablePositions() {
        return playablePositions;
    }

    public ArrayList<String> getBoardPrint() {
        return boardPrint;
    }

    public ArrayList<String> getPrint(){
        return boardPrint;
    }
}
