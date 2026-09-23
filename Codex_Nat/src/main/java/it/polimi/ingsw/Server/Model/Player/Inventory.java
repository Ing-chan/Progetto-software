package it.polimi.ingsw.Server.Model.Player;

import it.polimi.ingsw.Server.Model.Enums.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public class Inventory{

    private int[] inv;

    public Inventory() {
        // initialize array of num, size of array = number of resources
        //note: resources are always 7, therefore the array could be fixed to 7 cells, having, in each cell, 0 set as default
        inv = new int[Resource.values().length - 2];
    }

    // returns number of resources
    public int getResources(Resource res) {
        // orders resources by number, identifying every array cell with a univocal number
        // ex: ANIMAL.ordinal() set to position 0
        return inv[res.ordinal()];
    }

    //adds resources to inventory
    public void addResource(Resource res, int amount) {
        inv[res.ordinal()] += amount;
    }

    // removes resources from inventory, throws exception in case of negative number of resources
    public void removeResource(Resource res, int amount) {

        if (inv[res.ordinal()] - amount < 0) {
            //todo uncomment below
            //throw new NegativeResourcesException();
            System.out.println("ooooopps, devi uncommentare l'eccezione!");
        } else {
            inv[res.ordinal()] -= amount;
        }
    }

    // function checks if there are enough resources to get a certain Goldcard, return true or false
    // function checks if there are enough resources to get a certain Goldcard, return true or false
    public boolean hasEnoughResources(Inventory inventory) {
        for (int i = 0; i < inv.length; i++) {
            Resource[] allResources = Resource.values();
            if (inv[i] < inventory.getResources(allResources[i])) {
                return false;
            }
        }
        return true;
    }

    public void add(Inventory toAdd) {
        for (int i = 0; i < Resource.values().length - 2; i++) {
            inv[i] += toAdd.getResources(Resource.values()[i]);
        }
    }

    public void remove(Inventory toRemove) {
        for (int i = 0; i < Resource.values().length - 2; i++) {
            inv[i] -= toRemove.getResources(Resource.values()[i]);
        }
    }

    public String toString(){
        String string = "";
        for(int i = 0; i<inv.length;i++)
        {
            for(int j = 0; j <inv[i]; j++)
            {
                if(inv[i]>0)
                {
                    char c = Resource.values()[i].toString().charAt(0);
                    string = string.concat(Character.toString(c));
                }
            }
        }

        if(string.isEmpty())
        {
            string="";
        }

        return string;
    }

    public ArrayList<String> toPrint(){

        ArrayList<String> inventoryPrint = new ArrayList<>();
        inventoryPrint.add("╔"+"═".repeat(30)+"╗   ");
        int resCounter;
        for(int i = 0; i<inv.length;i++) {
            resCounter = 0;
            for (int j = 0; j < inv[i]; j++) {
                if (inv[i] > 0) {
                    resCounter++;
                }
            }
            if(i<4) {
                inventoryPrint.add("║ "+Resource.values()[i].toString() + ":"+" ".repeat(7-Resource.values()[i].toString().length()) + resCounter);
            }else{
                inventoryPrint.set(i-3,inventoryPrint.get(i-3).concat(" ".repeat(5-String.valueOf(inv[i-4]).length())+Resource.values()[i].toString() + ":" +" ".repeat(11-Resource.values()[i].toString().length()) + resCounter)+" ".repeat(4-String.valueOf(inv[i-4]).length())+"║   ");
            }
        }
        inventoryPrint.set(4,inventoryPrint.get(4).concat(" ".repeat(21-String.valueOf(inv[3]).length())+"║   "));

        inventoryPrint.add("╚"+"═".repeat(30)+"╝   ");

        return inventoryPrint;
    }

    public int Size()
    {
        return inv.length;
    }

    public int[] getAll() {
        return inv;
    }
}

