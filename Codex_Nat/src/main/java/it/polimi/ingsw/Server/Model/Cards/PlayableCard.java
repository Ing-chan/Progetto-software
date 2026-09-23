package it.polimi.ingsw.Server.Model.Cards;

import it.polimi.ingsw.Server.Model.Enums.*;
import it.polimi.ingsw.Server.Model.Player.Inventory;

import java.io.Serializable;
import java.util.ArrayList;


public class PlayableCard{

    private String idCard;
    private CardColour colour;
    private CardType type;
    private Face front;
    private Face back;



    private Boolean activeFace; //front is active when true, back when false



    //gold constructor, obj (from ObjectiveType) is the scheme for extra points
    public PlayableCard(String id, int intID, CardColour colour, int points, GoldCardObjective objective, Resource r1, Resource r2, Resource r3, Resource r4, ArrayList<Resource> resourceNeeded) {

        this.idCard = id.concat(String.valueOf(intID));

        this.activeFace = false; //default back is visible, front is flipped by the deck when they are on the top

        this.colour = colour;

        //gold front constructor, scheme is from enum resource
        this.front = new Face(resourceNeeded, objective, r1, r2, r3, r4, points);

        //create back face
        this.back = new Face(colour);

        type = CardType.GOLDCARD;

    }

    //first cards constructor, scheme is 4 enum resources in the middle
    public PlayableCard(String id, int numID, Resource r1, Resource r2, Resource r3, Resource r4, ArrayList<Resource> ExtraResources, Resource br1, Resource br2, Resource br3, Resource br4) {

        this.idCard = id.concat(String.valueOf(numID));

        this.colour = CardColour.GOLD;

        this.activeFace = true; //back face is set to active

        //crete front face
        this.back = new Face(r1, r2, r3, r4, 0, ExtraResources);

        //create back face
        this.front = new Face(br1, br2, br3, br4, 0);

        type = CardType.INITIALCARD;

    }

    //resource card constructor
    public PlayableCard(String id, int numID, CardColour colour,int points,Resource r1,Resource r2,Resource r3,Resource r4) {

        if(numID<10) {
            this.idCard = id.concat("0"+numID);
        }else{
            this.idCard = id.concat(String.valueOf(numID++));
        }

        this.activeFace = false; //default back is visible, front is flipped by the deck when they are on the top

        this.colour = colour;

        //resource front constructor, type is always immediate
        this.front = new Face(r1, r2, r3, r4, points);

        //create back face
        this.back = new Face(colour);

        type = CardType.RESOURCECARD;

    }

    public void FlipCard() {
        activeFace = !activeFace;
    }

    Inventory getCardResources() {
        if (activeFace) {
            return front.getResources();
        } else
            return back.getResources();
    }


    public CardType getCardType() {
        return type;
    }

    public CardColour getColour() {
        return colour;
    }

    public ArrayList<Corner> getCorners() {
        if (activeFace) return front.getCorners();
        return back.getCorners();
    }

    public ArrayList<CornerPosition> getFreeCorners() {
        if (activeFace) return front.getFreeCorners();
        return back.getFreeCorners();
    }

    public String getIDCard() {
        return idCard;
    }

    public GoldCardObjective getObjective() {
        if (activeFace) {
            return front.getObjective();
        }
        return back.getObjective();
    }

    public int getPoints() {
        if (activeFace) {
            return front.getPoints();
        }
        return 0;
    }

    public Inventory getCost() {
        if (activeFace) {
            return front.getCost();
        }
        return back.getCost();
    }

    public boolean getCardSide() {
        return activeFace;
    }
    public Face getActiveFace() {
        if(activeFace) {
            return front;
        }
        else {
            return back;
        }
    }

    public Inventory getActiveResurces() {
        if (activeFace) {
            return front.getActiveResurces();
        }
        return back.getActiveResurces();
    }


    public ArrayList<String> toPrintString(){

        ArrayList<String> cardToString = new ArrayList<>();
        StringBuilder string = new StringBuilder();

        char corner1 = this.getCorners().get(3).getResource().toString().charAt(0);
        char corner2 = this.getCorners().get(1).getResource().toString().charAt(0);
        char corner3 = this.getCorners().get(2).getResource().toString().charAt(0);
        char corner4 = this.getCorners().get(0).getResource().toString().charAt(0);

        //converts upper part of the card
        if(this.getCorners().get(3).getResource().equals(Resource.PLAYABLE)){
            string.append("¤");
        }else if(corner1=='U'){
            string.append("┌");
        }
        else{
            string.append(corner1);
        }

        string.append("─".repeat(5));

        if(this.getCorners().get(0).getResource().equals(Resource.PLAYABLE)){
            string.append("¤");
        }else if(corner4=='U'){
            string.append("┐");
        }
        else{
            string.append(corner4);
        }
        cardToString.add(string.toString());
        string.setLength(0);

        //converts middle part of the card
        string.append("│ ");
        if(!this.getCardSide()) {
            String faceRes = this.getExtraResurces().toString();
            switch (faceRes.length()){
                case 1:
                    string.append(" ");
                    string.append(faceRes);
                    string.append(" ");
                    break;
                case 2:
                    string.append(" ");
                    string.append(faceRes);
                    break;
                case 3:
                    string.append(faceRes);
                    break;
            }

        }else{
            string.append("   ");
        }
        string.append(" │");
        cardToString.add(string.toString());
        string.setLength(0);

        //print the bottom part
        if(this.getCorners().get(2).getResource().equals(Resource.PLAYABLE)){
            string.append("¤");
        }else if(corner3=='U'){
            string.append("└");
        }
        else{
            string.append(corner3);
        }
        string.append("─".repeat(5));
        if(this.getCorners().get(1).getResource().equals(Resource.PLAYABLE)){
            string.append("¤");
        }else if(corner2=='U'){
            string.append("┘");
        }
        else{
            string.append(corner2);
        }
        cardToString.add(string.toString());

        //print cost an objective
        int points = this.getActiveFace().getPoints();
        String obj = "";
        switch (this.getActiveFace().getObjective()){
            case GoldCardObjective.IMMEDIATE:
                if(points!=0) {
                    obj = (points + "pt");
                }
                else{
                    obj = ("");
                }
                break;
            case GoldCardObjective.CORNER:
                obj = (points + "pt for every corner covered");
                break;
            case GoldCardObjective.ITEMVIAL:
                obj = (points + "pt for every inkwell you have");
                break;
            case GoldCardObjective.ITEMMANUSCRIPT:
                obj = (points + "pt for every manuscript you have");
                break;
            case GoldCardObjective.ITEMQUILL:
                obj = (points + "pt for every quill you have");
                break;

        }

        String cost = this.getActiveFace().getCost().toString();
        if(!cost.isEmpty()) {
            cardToString.add(obj.concat("   Cost: " + cost));
        }
        else {
            cardToString.add(obj);
        }

        return cardToString;

    }

    private Inventory getExtraResurces() {

        if(type.equals(CardType.INITIALCARD)) {
            return back.getExtraResources();
        }else {
            return back.getActiveResurces();
        }
    }
}
