package it.polimi.ingsw.Network.Virtuals;
import java.io.Serializable;
import java.util.ArrayList;

public class VirtualObjective implements Serializable{
    String id;
    ArrayList<String> objPrint;

    public VirtualObjective(String id, ArrayList<String> objPrint) {
        this.id = id;
        this.objPrint = objPrint;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getObjPrint() {
        return objPrint;
    }
}
