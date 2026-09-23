package it.polimi.ingsw.Server.Model.Cards;

import it.polimi.ingsw.Server.Model.Enums.CardColour;
import it.polimi.ingsw.Server.Model.Enums.CornerPosition;
import it.polimi.ingsw.Server.Model.Enums.GoldCardObjective;
import it.polimi.ingsw.Server.Model.Enums.Resource;
import it.polimi.ingsw.Server.Model.Player.Inventory;

import java.io.Serializable;
import java.util.ArrayList;

public class Face{

    private int points;

    private Inventory cost;

    private ArrayList<Corner> corners; // rotation: UPright, DOWNright, DOWNright, UPleft

    private Inventory faceResources;

    private Inventory extraResurces;

    private GoldCardObjective objective;

    //front face resource
    public Face(Resource resource1, Resource resource2, Resource resource3, Resource resource4, int pts) {
        corners = new ArrayList<>();
        this.cost = new Inventory();
        faceResources = new Inventory();

        //initialize corners
        corners.add(new Corner(resource1));
        if((resource1!=Resource.UNPLAYABLE)&&(resource1!=Resource.PLAYABLE)) {
            faceResources.addResource(resource1, 1);
        }
        corners.add(new Corner(resource2));
        if((resource2!=Resource.UNPLAYABLE)&&(resource2!=Resource.PLAYABLE)) {
            faceResources.addResource(resource2, 1);
        }
        corners.add(new Corner(resource3));
        if((resource3!=Resource.UNPLAYABLE)&&(resource3!=Resource.PLAYABLE)) {
            faceResources.addResource(resource3, 1);
        }
        corners.add(new Corner(resource4));
        if((resource4!=Resource.UNPLAYABLE)&&(resource4!=Resource.PLAYABLE)) {
            faceResources.addResource(resource4, 1);
        }

        this.points = pts;

        objective = GoldCardObjective.IMMEDIATE;

    }

    //first face constructor
    public Face(Resource resource1, Resource resource2, Resource resource3, Resource resource4, int pts, ArrayList<Resource> extraResources) {
        corners = new ArrayList<>();
        this.cost = new Inventory();
        faceResources = new Inventory();
        this.extraResurces = new Inventory();

        //initialize corners
        corners.add(new Corner(resource1));
        if((resource1!=Resource.UNPLAYABLE)&&(resource1!=Resource.PLAYABLE)) {
            faceResources.addResource(resource1, 1);
        }
        corners.add(new Corner(resource2));
        if((resource2!=Resource.UNPLAYABLE)&&(resource2!=Resource.PLAYABLE)) {
            faceResources.addResource(resource2, 1);
        }
        corners.add(new Corner(resource3));
        if((resource3!=Resource.UNPLAYABLE)&&(resource3!=Resource.PLAYABLE)) {
            faceResources.addResource(resource3, 1);
        }
        corners.add(new Corner(resource4));
        if((resource4!=Resource.UNPLAYABLE)&&(resource4!=Resource.PLAYABLE)) {
            faceResources.addResource(resource4, 1);
        }

        this.points = pts;

        for (Resource res : extraResources) {
            faceResources.addResource(res, 1);
            this.extraResurces.addResource(res, 1);
        }

        objective = GoldCardObjective.IMMEDIATE;

    }

    //front face gold constructor, obj is the enum for points
    public Face(ArrayList<Resource> resourceNeeded,  GoldCardObjective obj, Resource resource1, Resource resource2, Resource resource3, Resource resource4, int pts) {

        this.corners = new ArrayList<>();
        this.cost = new Inventory();
        faceResources = new Inventory();

        corners.add(new Corner(resource1));
        if((resource1!=Resource.UNPLAYABLE)&&(resource1!=Resource.PLAYABLE)) {
            faceResources.addResource(resource1, 1);
        }
        corners.add(new Corner(resource2));
        if((resource2!=Resource.UNPLAYABLE)&&(resource2!=Resource.PLAYABLE)) {
            faceResources.addResource(resource2, 1);
        }
        corners.add(new Corner(resource3));
        if((resource3!=Resource.UNPLAYABLE)&&(resource3!=Resource.PLAYABLE)) {
            faceResources.addResource(resource3, 1);
        }
        corners.add(new Corner(resource4));
        if((resource4!=Resource.UNPLAYABLE)&&(resource4!=Resource.PLAYABLE)) {
            faceResources.addResource(resource4, 1);
        }

        this.points = pts;

        //translates array list to inventory
        for (Resource res : resourceNeeded) {
            cost.addResource(res, 1);
        }

        objective = obj;

    }


    //back face gold & resource constructor
    public Face(CardColour colour) {

        this.corners = new ArrayList<>();
        this.cost = new Inventory();
        this.faceResources = new Inventory();

        //create 4 empty corners
        for (int i = 0; i < 4; i++) {
            corners.add(new Corner(Resource.PLAYABLE));
        }

        switch (colour) {
            case RED:
                faceResources.addResource(Resource.FUNGI, 1);
                break;
            case GREEN:
                faceResources.addResource(Resource.PLANT, 1);
                break;
            case BLUE:
                faceResources.addResource(Resource.ANIMAL, 1);
                break;
            case PURPLE:
                faceResources.addResource(Resource.INSECT, 1);
                break;

        }
        objective = GoldCardObjective.IMMEDIATE;
    }

    //TODO already exists a function for calculatin free corners, change this in player board

    //calculating free corners
    public ArrayList<CornerPosition> getFreeCorners() {
        ArrayList<CornerPosition> FreeCorners = new ArrayList<>();

        for (Corner corner : corners) {
            if (corner.getCornerState()) {
                switch (corners.indexOf(corner)) {
                    case 0:
                        FreeCorners.add(CornerPosition.TOPRIGHT);
                        break;
                    case 1:
                        FreeCorners.add(CornerPosition.BOTTOMRIGHT);
                        break;
                    case 2:
                        FreeCorners.add(CornerPosition.BOTTOMLEFT);
                        break;
                    case 3:
                        FreeCorners.add(CornerPosition.TOPLEFT);
                        break;
                }
            }
        }

        return FreeCorners;
    }


    public Inventory getCost() {
        return cost;
    }

    public ArrayList<Corner> getCorners() {
        return corners;
    }

    public GoldCardObjective getObjective() {
        return objective;
    }

    public Inventory getResources() {
        return faceResources;
    }

    public int getPoints() {
        return points;
    }

    public Inventory getActiveResurces() {

        Inventory activeResurces = faceResources;

        for(Corner corner : corners)
        {
            if(!(corner.isActive())&&(corner.getResource()!=Resource.UNPLAYABLE)&&(corner.getResource()!=Resource.PLAYABLE))
            {
                activeResurces.removeResource(corner.getResource(),1);
            }
        }

        return activeResurces;
    }

    public Inventory getExtraResources() {
        return extraResurces;
    }
}


