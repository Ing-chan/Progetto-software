package it.polimi.ingsw.Server.Model.Cards;

import it.polimi.ingsw.Server.Model.Enums.CardColour;
import it.polimi.ingsw.Server.Model.Enums.Objectives;
import it.polimi.ingsw.Client.View.CLI.ColorPrint;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

public class ObjectiveCard{

    private String idCard;
    private Objectives Objective;
    private Boolean Secret;   // default card are always not secret
    private CardColour Colour;
    private int points;


    //OBJ constructor
    @JsonCreator
    public ObjectiveCard(@JsonProperty("Color") CardColour col, @JsonProperty("CardID") int cardID, @JsonProperty("Points")  int pts,@JsonProperty("CardRule")  Objectives obj){

        idCard = "O".concat(String.valueOf(cardID));
        Secret=false;
        Colour=col;
        Objective=obj;
        points=pts;
    }

    public CardColour getColour(){
        return Colour;
    }

    public Objectives getObjectiveType() {
        return Objective;
    }

    public boolean isSecret() {
        return Secret;
    }

    public String getIDCard() {
        return idCard;
    }

    public void setSecret() {
        Secret = true;
    }

    public int getPoints() {
        return points;
    }

    public ArrayList<String> toPrintString() {

        ArrayList<String> objToPrint = new ArrayList<>();
        if(this.isSecret())
        {
            objToPrint.add("┌SCRT!┐");
        }else {
            objToPrint.add("┌─────┐");
        }

        if(!(Objective.toString().charAt(0)=='D'||Objective.toString().charAt(0)=='U'))
        {
            objToPrint.add("│     │");
        }

        switch (Objective){
            case Objectives.ATRIS:
                objToPrint.add("│ AAA │");
                objToPrint.add(this.points+ "pt for every 3A");
                break;
            case Objectives.PTRIS:
                objToPrint.add("│ PPP │");
                objToPrint.add(this.points+ "pt for every 3P");
                break;
            case Objectives.FTRIS:
                objToPrint.add("│ FFF │");
                objToPrint.add(this.points+ "pt for every 3F");
                break;
            case Objectives.ITRIS:
                objToPrint.add("│ III │");
                objToPrint.add(this.points+ "pt for every 3I");
                break;
            case Objectives.MDUO:
                objToPrint.add("│ M M │");
                objToPrint.add(this.points+ "pt for every 2M");
                break;
            case Objectives.IDUO:
                objToPrint.add("│ I I │");
                objToPrint.add(this.points+ "pt for every 2I");
                break;
            case Objectives.QDUO:
                objToPrint.add("│ Q Q │");
                objToPrint.add(this.points+ "pt for every 2Q");
                break;
            case Objectives.GTRIS:
                objToPrint.add("│ MIQ │");
                objToPrint.add(this.points+ "pt for every 3 special item");
                break;
            case Objectives.DIAGONALUP:
                objToPrint.add("│   "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+" │");
                objToPrint.add("│  "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+"  │");
                objToPrint.add("│ "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+"   │");
                objToPrint.add(this.points+ "pt for every right diagonal created");
                break;
            case Objectives.DIAGONALDOWN:
                objToPrint.add("│ "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+"   │");
                objToPrint.add("│  "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+"  │");
                objToPrint.add("│   "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+" │");
                objToPrint.add(this.points+ "pt for every left diagonal created");
                break;
            case Objectives.DOWNLGREENR:
                objToPrint.add("│ "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+"   │");
                objToPrint.add("│ "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+"   │");
                objToPrint.add("│  "+ ColorPrint.getGREEN() +"█"+ColorPrint.getRstCOLOR()+"  │");
                objToPrint.add(this.points+ "pt for every └ created");
                break;
            case Objectives.DOWNLPURPLEL:
                objToPrint.add("│  "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+"  │");
                objToPrint.add("│  "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+"  │");
                objToPrint.add("│ "+ ColorPrint.getPURPLE() +"█"+ColorPrint.getRstCOLOR()+"   │");
                objToPrint.add(this.points+ "pt for every ┘ created");
                break;
            case Objectives.UPLREDR:
                objToPrint.add("│  "+ ColorPrint.getRED() +"█"+ColorPrint.getRstCOLOR()+"  │");
                objToPrint.add("│ "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+"   │");
                objToPrint.add("│ "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+"   │");
                objToPrint.add(this.points+ "pt for every ┌ created");
                break;
            case Objectives.UPLBLUEL:
                objToPrint.add("│ "+ ColorPrint.getBLUE() +"█"+ColorPrint.getRstCOLOR()+"   │");
                objToPrint.add("│  "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+"  │");
                objToPrint.add("│  "+ ColorPrint.get(Colour) +"█"+ColorPrint.getRstCOLOR()+"  │");
                objToPrint.add(this.points+ "pt for every ┐ created");
                break;
        }

        if(!(Objective.toString().charAt(0)=='D'||Objective.toString().charAt(0)=='U'))
        {
            objToPrint.add(3,"│     │");
        }

        objToPrint.add(4,"└─────┘");

        return objToPrint;

    }
}



