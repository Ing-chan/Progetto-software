package it.polimi.ingsw.Server.Model.Board;

import it.polimi.ingsw.Client.View.CLI.ColorPrint;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Model.Cards.Corner;
import it.polimi.ingsw.Server.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.Server.Model.Cards.PlayableCard;
import it.polimi.ingsw.Server.Model.Enums.*;
import it.polimi.ingsw.Server.Model.Exceptions.InvalidPositionException;
import it.polimi.ingsw.Server.Model.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.Server.Model.Player.Inventory;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class PlayerBoard{

    private Inventory inventory;

    private int objectivePoints = 0;
    private int objectivesCompleted = 0;
    private int playableCardsPoints = 0;
    private HashMap<Point, PlayableCard> placedCards;
    private HashMap<Point, Boolean> playablePositions;

    //TODO quando ho finito il turno lastplayablecard deve essere riportata a null
    PlayableCard lastPlayedCard;


    public PlayerBoard() {
        placedCards = new HashMap<>();
        playablePositions = new HashMap<>();
        inventory = new Inventory();
        playablePositions.put(new Point(0, 0), true);
        lastPlayedCard = null;
    }

    public void placeNewCard(PlayableCard card, Point coords) throws NotEnoughResourcesException {


        if (card.getCardType() == CardType.GOLDCARD && card.getCardSide()) {
            if (!inventory.hasEnoughResources(card.getCost()))//not enough resources. note that this only accounts for 1 resource
            {
                throw new NotEnoughResourcesException();
            }
        }

        // removing the position from the playablePositions, adding the played card to the placedCards map

        placedCards.put(coords, card);

        //adding resources to inventory
        inventory.add(card.getActiveResurces());

        if (card.getCardType() != CardType.INITIALCARD) {
            playableCardsPoints += calculateGoldCardPoints(card, coords);
        }

        //check the 4 corners of the just placed card to see if modifications to the inventory must be done
        //it also deactivates the underneath corners
        DynamicInventoryUpdateAndCornerDeactivate(coords);

        UpdatePlayablePositions(card, coords);
        //check the 4 corners of the just placed card to see if modifications to playablePositions are needed

        lastPlayedCard = card;
    }

    public int getPlayablePositionsSize() {
        return playablePositions.size();
    }

    private void DynamicInventoryUpdateAndCornerDeactivate(Point coords) {

        Point topLeftPoint = new Point(coords.x - 1, coords.y + 1);
        Point topRightPoint = new Point(coords.x + 1, coords.y + 1);
        Point bottomLeftPoint = new Point(coords.x - 1, coords.y - 1);
        Point bottomRightPoint = new Point(coords.x + 1, coords.y - 1);

        if (placedCards.containsKey(topLeftPoint)) {
            Resource cardCornerResource = placedCards.get(topLeftPoint).getCorners().get(CornerPosition.BOTTOMRIGHT.ordinal()).getResource();
            if (cardCornerResource != Resource.PLAYABLE && cardCornerResource != Resource.UNPLAYABLE) {
                inventory.removeResource(cardCornerResource, 1);
            }
            placedCards.get(topLeftPoint).getCorners().get(CornerPosition.BOTTOMRIGHT.ordinal()).deactivate();
        }
        if (placedCards.containsKey(topRightPoint)) {
            Resource cardCornerResource = placedCards.get(topRightPoint).getCorners().get(CornerPosition.BOTTOMLEFT.ordinal()).getResource();
            if (cardCornerResource != Resource.PLAYABLE && cardCornerResource != Resource.UNPLAYABLE) {
                inventory.removeResource(cardCornerResource, 1);
            }
            placedCards.get(topRightPoint).getCorners().get(CornerPosition.BOTTOMLEFT.ordinal()).deactivate();
        }
        if (placedCards.containsKey(bottomLeftPoint)) {
            Resource cardCornerResource = placedCards.get(bottomLeftPoint).getCorners().get(CornerPosition.TOPRIGHT.ordinal()).getResource();
            if (cardCornerResource != Resource.PLAYABLE && cardCornerResource != Resource.UNPLAYABLE) {
                inventory.removeResource(cardCornerResource, 1);
            }
            placedCards.get(bottomLeftPoint).getCorners().get(CornerPosition.TOPRIGHT.ordinal()).deactivate();
        }
        if (placedCards.containsKey(bottomRightPoint)) {
            Resource cardCornerResource = placedCards.get(bottomRightPoint).getCorners().get(CornerPosition.TOPLEFT.ordinal()).getResource();
            if (cardCornerResource != Resource.PLAYABLE && cardCornerResource != Resource.UNPLAYABLE) {
                inventory.removeResource(cardCornerResource, 1);
            }
            placedCards.get(bottomRightPoint).getCorners().get(CornerPosition.TOPLEFT.ordinal()).deactivate();
        }
    }


    private void UpdatePlayablePositions(PlayableCard card, Point coords) {

        Point topLeftPoint = new Point(coords.x - 1, coords.y + 1);
        Point topRightPoint = new Point(coords.x + 1, coords.y + 1);
        Point bottomLeftPoint = new Point(coords.x - 1, coords.y - 1);
        Point bottomRightPoint = new Point(coords.x + 1, coords.y - 1);
        ArrayList<Corner> corners = card.getCorners();
        for (CornerPosition position : CornerPosition.values()) {
            //if a corner is UNPLAYABLE I must remove the position it touches from the playable positions
            if (corners.get(position.ordinal()).getResource() == Resource.UNPLAYABLE) {
                switch (position) {
                    case TOPLEFT:
                        playablePositions.remove(topLeftPoint);
                        break;
                    case TOPRIGHT:
                        playablePositions.remove(topRightPoint);
                        break;
                    case BOTTOMLEFT:
                        playablePositions.remove(bottomLeftPoint);
                        break;
                    case BOTTOMRIGHT:
                        playablePositions.remove(bottomRightPoint);
                        break;
                }
            }
            // if a corner is non-UNPLAYABLE then we check if the position it touches, touches any
            // UNPLAYABLE corners. If it doesn't we can add it to the playablePositions
            else {
                switch (position) {
                    case TOPLEFT:
                        if (doesntTouchAnyUnplayableCorner(topLeftPoint) && !placedCards.containsKey(topLeftPoint))
                            playablePositions.putIfAbsent(topLeftPoint, true);
                    case TOPRIGHT:
                        if (doesntTouchAnyUnplayableCorner(topRightPoint) && !placedCards.containsKey(topRightPoint))
                            playablePositions.putIfAbsent(topRightPoint, true);
                    case BOTTOMRIGHT:
                        if (doesntTouchAnyUnplayableCorner(bottomRightPoint) && !placedCards.containsKey(bottomRightPoint))
                            playablePositions.putIfAbsent(bottomRightPoint, true);
                    case BOTTOMLEFT:
                        if (doesntTouchAnyUnplayableCorner(bottomLeftPoint) && !placedCards.containsKey(bottomLeftPoint))
                            playablePositions.putIfAbsent(bottomLeftPoint, true);
                }
            }
        }
        playablePositions.remove(coords);
    }


    public boolean doesntTouchAnyUnplayableCorner(Point coords) {
        //utility function used in PlaceNewCard when updating the list of playablePositions. Name is self-explanatory
        Point topLeftPoint = new Point(coords.x - 1, coords.y + 1);
        Point topRightPoint = new Point(coords.x + 1, coords.y + 1);
        Point bottomLeftPoint = new Point(coords.x - 1, coords.y - 1);
        Point bottomRightPoint = new Point(coords.x + 1, coords.y - 1);

        if (placedCards.containsKey(topLeftPoint)) {
            if (placedCards.get(topLeftPoint).getCorners().get(CornerPosition.BOTTOMRIGHT.ordinal()).getResource() == Resource.UNPLAYABLE) {
                return false;
            }
        }
        if (placedCards.containsKey(topRightPoint)) {
            if (placedCards.get(topRightPoint).getCorners().get(CornerPosition.BOTTOMLEFT.ordinal()).getResource() == Resource.UNPLAYABLE) {
                return false;
            }
        }
        if (placedCards.containsKey(bottomLeftPoint)) {
            if (placedCards.get(bottomLeftPoint).getCorners().get(CornerPosition.TOPRIGHT.ordinal()).getResource() == Resource.UNPLAYABLE) {
                return false;
            }
        }
        if (placedCards.containsKey(bottomRightPoint)) {
            if (placedCards.get(bottomRightPoint).getCorners().get(CornerPosition.TOPLEFT.ordinal()).getResource() == Resource.UNPLAYABLE) {
                return false;
            }
        }

        return true;

    }


    public int calculateObjectivePoints(ArrayList<ObjectiveCard> objectiveCards) {
        Iterator<HashMap.Entry<Point, PlayableCard>> it;
        objectivePoints = 0;
        int completed = 0;

        for (ObjectiveCard objCard : objectiveCards) {

            it = placedCards.entrySet().iterator();
            HashMap<Point, Boolean> objsUsed = new HashMap<>();

            Objectives objectiveType = objCard.getObjectiveType();
            HashMap.Entry<Point, PlayableCard> entry;

            CardColour colour = objCard.getColour();
            int pointsGiven = objCard.getPoints();

            switch (objectiveType) {
                case DIAGONALDOWN: {
                    //card taken as point of reference: top left.
                    while (it.hasNext()) {
                        entry = it.next();
                        Point entryPos = entry.getKey();
                        if (entry.getValue().getColour() == colour) {
                            Point point1 = new Point(entryPos.x + 1, entryPos.y - 1);
                            Point point2 = new Point(entryPos.x + 2, entryPos.y - 2);

                            if (placedCards.containsKey(point1) && placedCards.containsKey(point2) && !objsUsed.containsKey(entry.getKey()) && !objsUsed.containsKey(point1) && !objsUsed.containsKey(point2) && placedCards.get(point1).getColour() == colour && placedCards.get(point2).getColour() == colour) {
                                objectivePoints += pointsGiven;
                                objsUsed.put(point1, true);
                                objsUsed.put(point2, true);
                                objsUsed.put(entry.getKey(), true);
                                objectivesCompleted += 1;

                            }
                        }
                    }
                    LoggerUtility.getLogger().info("DIAGONALRIGHT has earned: " + objectivePoints);
                }
                break;

                case DIAGONALUP: {
                    //card taken as point of reference: bottom left.
                    while (it.hasNext()) {
                        entry = it.next();
                        Point entryPos = entry.getKey();
                        if (entry.getValue().getColour() == colour) {
                            Point point1 = new Point(entryPos.x + 1, entryPos.y + 1);
                            Point point2 = new Point(entryPos.x + 2, entryPos.y + 2);
                            if (placedCards.containsKey(point1) && placedCards.containsKey(point2) && !objsUsed.containsKey(entry.getKey()) && !objsUsed.containsKey(point1) && !objsUsed.containsKey(point2) && placedCards.get(point1).getColour() == colour && placedCards.get(point2).getColour() == colour) {
                                objectivePoints += pointsGiven;
                                objsUsed.put(point1, true);
                                objsUsed.put(point2, true);
                                objsUsed.put(entry.getKey(), true);
                                objectivesCompleted += 1;
                            }
                        }
                    }
                    LoggerUtility.getLogger().info("DIAGONALLEFT has earn: " + objectivePoints);
                }
                break;

                case DOWNLGREENR: {
                    //card taken as point of reference: top card of the L-shape
                    while (it.hasNext()) {
                        entry = it.next();
                        Point entryPos = entry.getKey();
                        if (entry.getValue().getColour() == colour) {
                            Point point1 = new Point(entryPos.x, entryPos.y - 2);
                            Point point2 = new Point(entryPos.x + 1, entryPos.y - 3);
                            if (placedCards.containsKey(point1) && placedCards.containsKey(point2) && !objsUsed.containsKey(entry.getKey()) && !objsUsed.containsKey(point1) && !objsUsed.containsKey(point2) && placedCards.get(point1).getColour() == colour && placedCards.get(point2).getColour() == CardColour.GREEN) {
                                objectivePoints += pointsGiven;
                                objsUsed.put(point1, true);
                                objsUsed.put(point2, true);
                                objsUsed.put(entry.getKey(), true);
                                objectivesCompleted += 1;
                            }
                        }
                    }
                    LoggerUtility.getLogger().info("DOWNLGREENR has earn: " + objectivePoints);
                }
                break;

                case UPLREDR: {
                    //card taken as point of reference: bottom card of the Γ-shape
                    while (it.hasNext()) {
                        entry = it.next();
                        Point entryPos = entry.getKey();
                        if (entry.getValue().getColour() == colour) {
                            Point point1 = new Point(entryPos.x, entryPos.y + 2);
                            Point point2 = new Point(entryPos.x + 1, entryPos.y + 3);
                            if (placedCards.containsKey(point1) && placedCards.containsKey(point2) && !objsUsed.containsKey(entry.getKey()) && !objsUsed.containsKey(point1) && !objsUsed.containsKey(point2) && placedCards.get(point1).getColour() == colour && placedCards.get(point2).getColour() == CardColour.RED) {
                                objectivePoints += pointsGiven;
                                objsUsed.put(point1, true);
                                objsUsed.put(point2, true);
                                objsUsed.put(entry.getKey(), true);
                                objectivesCompleted += 1;
                            }
                        }
                    }
                    LoggerUtility.getLogger().info("UPLREDR has earn: " + objectivePoints);
                }
                break;

                case UPLBLUEL: {
                    //card taken as point of reference: bottom card of the Ꞁ-shape
                    while (it.hasNext()) {
                        entry = it.next();
                        Point entryPos = entry.getKey();
                        if (entry.getValue().getColour() == colour) {
                            Point point1 = new Point(entryPos.x, entryPos.y + 2);
                            Point point2 = new Point(entryPos.x - 1, entryPos.y + 3);
                            if (placedCards.containsKey(point1) && placedCards.containsKey(point2) && !objsUsed.containsKey(entry.getKey()) && !objsUsed.containsKey(point1) && !objsUsed.containsKey(point2) && placedCards.get(point1).getColour() == colour && placedCards.get(point2).getColour() == CardColour.BLUE) {
                                objectivePoints += pointsGiven;
                                objsUsed.put(point1, true);
                                objsUsed.put(point2, true);
                                objsUsed.put(entry.getKey(), true);
                                objectivesCompleted += 1;
                            }
                        }
                    }
                    LoggerUtility.getLogger().info("UPLBLUEL has earn: " + objectivePoints);
                }
                break;

                case DOWNLPURPLEL: {
                    //card taken as point of reference: top card of the ⅃-shape
                    while (it.hasNext()) {
                        entry = it.next();
                        Point entryPos = entry.getKey();
                        if (entry.getValue().getColour() == colour) {
                            Point point1 = new Point(entryPos.x, entryPos.y - 2);
                            Point point2 = new Point(entryPos.x - 1, entryPos.y - 3);
                            if (placedCards.containsKey(point1) && placedCards.containsKey(point2) && !objsUsed.containsKey(entry.getKey()) && !objsUsed.containsKey(point1) && !objsUsed.containsKey(point2) && placedCards.get(point1).getColour() == colour && placedCards.get(point2).getColour() == CardColour.PURPLE) {
                                objectivePoints += pointsGiven;
                                objsUsed.put(point1, true);
                                objsUsed.put(point2, true);
                                objsUsed.put(entry.getKey(), true);
                                objectivesCompleted += 1;
                            }
                        }
                    }
                    LoggerUtility.getLogger().info("DOWNLPURPLEL has earn: " + objectivePoints);
                }
                break;


                case FTRIS:
                    completed = inventory.getResources(Resource.FUNGI) / 3;
                    objectivePoints += (completed) * pointsGiven;
                    objectivesCompleted += completed;
                    LoggerUtility.getLogger().info("FTRIS has earn: " + objectivePoints);
                    break;

                case PTRIS:
                    completed = inventory.getResources(Resource.PLANT) / 3;
                    objectivePoints += completed * pointsGiven;
                    objectivesCompleted += completed;
                    LoggerUtility.getLogger().info("PTRIS has earn: " + objectivePoints);
                    break;

                case ATRIS:
                    completed = inventory.getResources(Resource.ANIMAL) / 3;
                    objectivePoints += completed * pointsGiven;
                    objectivesCompleted += completed;
                    LoggerUtility.getLogger().info("ATRIS has earn: " + objectivePoints);
                    break;

                case ITRIS:
                    completed = inventory.getResources(Resource.INSECT) / 3;
                    objectivePoints += completed * pointsGiven;
                    objectivesCompleted += completed;
                    LoggerUtility.getLogger().info("ITRIS has earn: " + objectivePoints);
                    break;

                case MDUO:
                    completed = inventory.getResources(Resource.MANUSCRIPT) / 2;
                    objectivePoints += completed * pointsGiven;
                    objectivesCompleted += completed;
                    LoggerUtility.getLogger().info("MDUO has earn: " + objectivePoints);
                    break;

                case IDUO:
                    completed = inventory.getResources(Resource.VIAL) / 2;
                    objectivePoints += completed * pointsGiven;
                    objectivesCompleted += completed;
                    LoggerUtility.getLogger().info("IDUO has earn: " + objectivePoints);
                    break;

                case QDUO:
                    completed = inventory.getResources(Resource.QUILL) / 2;
                    objectivePoints += completed * pointsGiven;
                    objectivesCompleted += completed;
                    LoggerUtility.getLogger().info("QDUO has earn: " + objectivePoints);
                    break;

                case GTRIS:
                    //special tris, one of each resource, gives 3 points.
                    completed = Math.min(inventory.getResources(Resource.QUILL), Math.min(inventory.getResources(Resource.MANUSCRIPT), inventory.getResources(Resource.VIAL)));
                    objectivePoints += pointsGiven * completed;
                    objectivesCompleted += completed;
                    LoggerUtility.getLogger().info(" GTRIS has earn: " + objectivePoints);
                    break;

                default:
                    System.out.println("error, objective type not in list (somehow)");
                    break;
            }
        }
        LoggerUtility.getLogger().info("Total Objectives completed" + objectivesCompleted);
        return objectivePoints;
    }


    int calculateGoldCardPoints(PlayableCard goldCard, Point pos) {
        GoldCardObjective cardObjective = goldCard.getObjective();
        int cardPoints = 0;

        int pointsGiven = goldCard.getPoints();

        switch (cardObjective) {
            case ITEMQUILL:
                cardPoints += inventory.getResources(Resource.QUILL) * pointsGiven;
                LoggerUtility.getLogger().info("has earn: " + cardPoints);
                break;

            case ITEMMANUSCRIPT:
                cardPoints += inventory.getResources(Resource.MANUSCRIPT) * pointsGiven;
                LoggerUtility.getLogger().info("has earn: " + cardPoints);
                break;

            case ITEMVIAL:
                cardPoints += inventory.getResources(Resource.VIAL) * pointsGiven;
                LoggerUtility.getLogger().info("has earn: " + cardPoints);
                break;

            case CORNER:
                if (placedCards.containsKey(new Point(pos.x - 1, pos.y + 1))) {
                    cardPoints += pointsGiven;
                }
                if (placedCards.containsKey(new Point(pos.x + 1, pos.y + 1))) {
                    cardPoints += pointsGiven;
                }
                if (placedCards.containsKey(new Point(pos.x - 1, pos.y - 1))) {
                    cardPoints += pointsGiven;
                }
                if (placedCards.containsKey(new Point(pos.x + 1, pos.y - 1))) {
                    cardPoints += pointsGiven;
                }
                LoggerUtility.getLogger().info("has earn: " + cardPoints);
                break;

            case IMMEDIATE:
                cardPoints += pointsGiven;
                LoggerUtility.getLogger().info("has earn: " + cardPoints);
                break;
            default:

        }
        return cardPoints;
    }

    public int getNumPlacedCards() {
        return placedCards.size();
    }

    public HashMap<Point, Boolean> getPlayablePositions() {
        return new HashMap<>(playablePositions);
    }

    public ArrayList<Point> getPlayablePositionsArrayList() {
        // Ottieni il set di chiavi (Points) dalla mappa
        Set<Point> pointsSet = getPlayablePositions().keySet();

        // Converte il set in un ArrayList di Point
        ArrayList<Point> pointsList = new ArrayList<>(pointsSet);

        return pointsList;
    }

    //todo funzione di testing
    public PlayableCard getCard(Point pos) {
        return placedCards.getOrDefault(pos, null);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getPlayableCardScore() {
        return playableCardsPoints;
    }

    public PlayableCard getLastPlayedCard() {
        return lastPlayedCard;
    }

    public ArrayList<String> toPrint() {

        //calcolo della grandezza del tabellone di gioco in base alla distribuzione delle posizioni giocabili
        int Xmax = 0;
        int Xmin = 0;
        int Ymax = 0;
        int Ymin = 0;


        int[] utilMaxMin = findMaxMin(playablePositions.keySet());

        Xmax = utilMaxMin[0];
        Xmin = utilMaxMin[1];
        Ymax = utilMaxMin[2];
        Ymin = utilMaxMin[3];

        utilMaxMin = findMaxMin(placedCards.keySet());

        if(utilMaxMin[0]>Xmax)
        {
            Xmax = utilMaxMin[0];
        }
        if(utilMaxMin[1]<Xmin)
        {
            Xmin = utilMaxMin[1];
        }
        if(utilMaxMin[2]>Ymax)
        {
            Ymax = utilMaxMin[2];
        }
        if(utilMaxMin[3]<Ymin)
        {
            Ymin = utilMaxMin[3];
        }


        int xlenght = Xmax - Xmin + 1;
        int ylenght = Ymax - Ymin + 1;

        ArrayList<String> filler = new ArrayList<>();
        //FILLER modify character if you want
        for (int i = 0; i < 3; i++) {
            filler.add(" ".repeat(7));
        }

        if (!placedCards.isEmpty()) {

            ArrayList<String>[][] printedBoard = new ArrayList[xlenght][ylenght];

            ArrayList<String> board = new ArrayList<>();
            ArrayList<String> cardToTrim;
            StringBuilder string = new StringBuilder();

            for (int i = 0; i < xlenght; i++) {
                for (int j = 0; j < ylenght; j++) {
                    printedBoard[i][j] = filler;
                }
            }


            //primo parametro in printed board è la y la seconda la x
            for (Point coords : placedCards.keySet()) {
                PlayableCard card = placedCards.get(coords);
                cardToTrim = card.toPrintString();
                int cornerCounter = 0;
                String cornerToTrim;

                for (Corner corner : card.getCorners()) {
                    if (!corner.isActive()) {
                        switch (cornerCounter) {
                            case 0:
                                cornerToTrim = cardToTrim.get(0);
                                cardToTrim.set(0, cornerToTrim.substring(0, cornerToTrim.length() - 1).concat("►"));
                                break;
                            case 1:
                                cornerToTrim = cardToTrim.get(2);
                                cardToTrim.set(2, cornerToTrim.substring(0, cornerToTrim.length() - 1).concat("►"));
                                break;
                            case 2:
                                cornerToTrim = cardToTrim.get(2);
                                cardToTrim.set(2, "◄" + cornerToTrim.substring(1));
                                break;
                            case 3:
                                cornerToTrim = cardToTrim.get(0);
                                cardToTrim.set(0, "◄" + cornerToTrim.substring(1));
                                break;

                        }
                    }
                    cornerCounter++;
                }
                //aggiungo i colori con codice ansii
                for (int i = 0; i < cardToTrim.size(); i++) {
                    String modifiedString = ColorPrint.get(card.getColour()) + cardToTrim.get(i) + ColorPrint.getRstCOLOR();
                    cardToTrim.set(i, modifiedString);
                }

                //l'offset per centrare la matrice sono Xmin e Ymax
                printedBoard[((int) coords.getX() - Xmin)][(Ymax - (int) coords.getY())] = cardToTrim;
            }

            ArrayList<String> pCoords;
            //stamp the playable positions
            for (Point coords : playablePositions.keySet()) {
                pCoords = new ArrayList<>();
                pCoords.add(" ╔   ╗ ");
                String xUtil;
                if ((int) coords.getX() >= 0) {
                    xUtil = (" " + (int) coords.getX() + " ~");
                } else {
                    xUtil = ((int) coords.getX() + " ~");
                }
                if ((int) coords.getY() >= 0) {
                    pCoords.add(xUtil +" "+ (int) coords.getY() + " ");
                } else {
                    pCoords.add(xUtil + (int) coords.getY() + " ");
                }
                pCoords.add(" ╚   ╝ ");
                printedBoard[((int) coords.getX() - Xmin)][(Ymax - (int) coords.getY())] = pCoords;
            }

            //transforms the matrix into an arraylist
            boolean isShifted = true;
            for (int j = 0; j < xlenght; j++) {
                if (printedBoard[j][0].getFirst().equals(" ╔   ╗ ")) {
                    if (j % 2 == 0) {
                        isShifted = false;
                    }
                    break;
                }
            }

            for (int i = 0; i < ylenght; i++) {
                for (int k = 0; k < 3; k++) {
                    for (int j = 0; j < xlenght; j++) {

                        if (!isShifted) {
                            if (i == 0 && k == 0) {
                                string.append(printedBoard[j][i].get(k));
                            } else if (k == 0) {
                                if (i % 2 == 0) {
                                    if (j % 2 == 0) {
                                        string.append(printedBoard[j][i].get(k));
                                    } else {
                                        string.append(printedBoard[j][i - 1].get(2));
                                    }
                                } else {
                                    if (j % 2 == 0) {
                                        string.append(printedBoard[j][i - 1].get(2));
                                    } else {
                                        string.append(printedBoard[j][i].get(k));
                                    }
                                }
                            } else if (k == 1) {
                                if (i % 2 == 0) {
                                    if (j % 2 == 0) {
                                        string.append(printedBoard[j][i].get(k));
                                    } else {
                                        string.append(filler.getFirst());
                                    }
                                } else {
                                    if (j % 2 == 0) {
                                        string.append(filler.getFirst());
                                    } else {
                                        string.append(printedBoard[j][i].get(k));
                                    }
                                }
                            } else if (i == ylenght - 1) {
                                string.append(printedBoard[j][i].get(k));
                            }
                        } else {
                            if (i == 0 && k == 0) {
                                string.append(printedBoard[j][i].get(k));
                            } else if (k == 0) {
                                if (i % 2 == 1) {
                                    if (j % 2 == 0) {
                                        string.append(printedBoard[j][i].get(k));
                                    } else {
                                        string.append(printedBoard[j][i - 1].get(2));
                                    }
                                } else {
                                    if (j % 2 == 0) {
                                        string.append(printedBoard[j][i - 1].get(2));
                                    } else {
                                        string.append(printedBoard[j][i].get(k));
                                    }
                                }
                            } else if (k == 1) {
                                if (i % 2 == 1) {
                                    if (j % 2 == 0) {
                                        string.append(printedBoard[j][i].get(k));
                                    } else {
                                        string.append(filler.getFirst());
                                    }
                                } else {
                                    if (j % 2 == 0) {
                                        string.append(filler.getFirst());
                                    } else {
                                        string.append(printedBoard[j][i].get(k));
                                    }
                                }
                            } else if (i == ylenght - 1) {
                                string.append(printedBoard[j][i].get(k));
                            }
                        }
                    }

                    if (!string.isEmpty()) {
                        board.add(string.toString());
                        string.setLength(0);
                    }
                }
            }
            return board;
        } else {
            ArrayList<String> emptyCamp = new ArrayList<>();
            emptyCamp.add("╔     ╗");
            emptyCamp.add("  0~0  ");
            emptyCamp.add("╚     ╝");
            return emptyCamp;
        }
    }

    private int[] findMaxMin(Set<Point> setCoords){
        int Xmax=0;
        int Xmin=0;
        int Ymax=0;
        int Ymin=0;

        int[] utils = new int[4];


        for(Point coords : setCoords ) {
            if (coords.getX() > Xmax) {
                Xmax = ((int) coords.getX());
            } else if (coords.getX() < Xmin) {
                Xmin = ((int) coords.getX());
            }
            if (coords.getY() > Ymax) {
                Ymax = ((int) coords.getY());
            } else if (coords.getY() < Ymin) {
                Ymin = ((int) coords.getY());
            }
        }

        utils[0] = Xmax;
        utils[1] = Xmin;
        utils[2] = Ymax;
        utils[3] = Ymin;

        return utils;
    }

    public void validCoords(Point coords) throws InvalidPositionException {
        if (!playablePositions.containsKey(coords)) {
            throw new InvalidPositionException();
        }
    }
}