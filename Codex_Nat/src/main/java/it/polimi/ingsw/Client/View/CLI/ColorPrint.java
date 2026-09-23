package it.polimi.ingsw.Client.View.CLI;

import it.polimi.ingsw.Server.Model.Enums.CardColour;

public class ColorPrint {
    static final String RED = "\u001b[31m";
    static final String BLUE = "\u001b[34m";
    static final String GREEN = "\u001b[32m";
    static final String PURPLE = "\u001b[35m";
    static final String rstCOLOR ="\u001b[0m";

    static public String getRED() {
        return RED;
    }

    static public String getBLUE() {
        return BLUE;
    }

    static public String getGREEN() {
        return GREEN;
    }

    static public String getPURPLE() {
        return PURPLE;
    }

    static public String getRstCOLOR() {
        return rstCOLOR;
    }

    public static String get(CardColour colour) {
        switch (colour){
            case CardColour.BLUE -> {
                return getBLUE();
            }
            case CardColour.RED -> {
                return getRED();
            }
            case CardColour.GREEN -> {
                return getGREEN();
            }
            case CardColour.PURPLE -> {
                return getPURPLE();
            }
            case CardColour.GOLD -> {
                return getRstCOLOR();
            }
        }
        return "ERRORE COLORE";
    }
}