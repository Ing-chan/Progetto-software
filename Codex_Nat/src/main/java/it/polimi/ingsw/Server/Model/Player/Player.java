package it.polimi.ingsw.Server.Model.Player;

import it.polimi.ingsw.Server.Model.Board.PlayerBoard;
import it.polimi.ingsw.Server.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.Server.Model.Cards.PlayableCard;
import it.polimi.ingsw.Server.Model.Exceptions.CardNotInHandException;
import it.polimi.ingsw.Server.Model.Exceptions.InvalidPositionException;
import it.polimi.ingsw.Server.Model.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.Network.Server.LoggerUtility;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Player{

    private final String username;
    private int score;
    private int objScore;
    private ArrayList<ObjectiveCard> objectives;
    private ArrayList<PlayableCard> myCards;
    private PlayerBoard pBoard;


    // pplPlayablealayer's constructor
    public Player(String username) {
        this.username = username;
        this.score = 0;
        this.objScore = 0;
        this.objectives = new ArrayList<>();
        this.myCards = new ArrayList<>();
        this.pBoard = new PlayerBoard();
    }

    // returns all valid positions for card's placement on the board
    private HashMap<Point, Boolean> getValidCardPositions() {
        return pBoard.getPlayablePositions();
    }

    public void addCard(PlayableCard card) {
        myCards.add(card);
    }

    public void addCard(ObjectiveCard card) {
        objectives.add(card);
    }

    // places cards, when coords are available
    public void placeCard(String chosenCard, Point coords, Boolean selectedFace) throws NotEnoughResourcesException {

        PlayableCard card = findCard(chosenCard);



        if(card.getCardSide() != selectedFace)
        {
            card.FlipCard();
        }

        pBoard.placeNewCard(card, coords);
        myCards.remove(card);
        updateScore();
    }

    // remove the lst played card before confirm of the move
    public void removeCard() {
        PlayableCard card = pBoard.getLastPlayedCard();
        myCards.add(card);
        updateScore();
    }

    private void updateScore() {
        score = pBoard.getPlayableCardScore();
    }

    // returns player's username
    public String getNickname() {
        return this.username;
    }

    public int getScore() {
        return this.score;
    }

    public int getFinalScore() {
        LoggerUtility.getLogger().info(username);
        objScore = pBoard.calculateObjectivePoints(objectives);
        return objScore + score;
    }

    public PlayableCard findCard(String IDCard){
        for (PlayableCard Card : myCards) {
            if (Card.getIDCard().equals(IDCard)) {
                return Card;
            }
        }
        //todo set null after testing
        return myCards.getFirst();
    }

    public ArrayList<PlayableCard> getMyCards() {
        return myCards;
    }

    public ArrayList<ObjectiveCard> getObjectives() {
        return objectives;
    }

    public PlayerBoard getpBoard() {
        return pBoard;
    }

    public void validCard(String chosenCard) throws CardNotInHandException{
        boolean flag = true;

        for( PlayableCard card : myCards)
        {
            if(card.getIDCard().equals(chosenCard))
            {
                flag=false;
                break;
            }
        }

        if(flag){
            throw new CardNotInHandException();
        }
    }

    public void validCoords(Point coords) throws InvalidPositionException {
        pBoard.validCoords(coords);
    }
}

