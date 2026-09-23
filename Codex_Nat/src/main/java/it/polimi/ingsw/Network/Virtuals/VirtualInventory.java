package it.polimi.ingsw.Network.Virtuals;

import it.polimi.ingsw.Server.Model.Enums.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class VirtualInventory implements Serializable {
    int[] inv;
    ArrayList<String> inventoryPrint;

    public VirtualInventory(int[] inv,ArrayList<String> inventoryPrint) {
        this.inv = inv;
        this.inventoryPrint = inventoryPrint;
    }

    public int getResources(Resource res) {
        return inv[res.ordinal()];
    }

    public ArrayList<String> getPrint() {
        return inventoryPrint;
    }
}
