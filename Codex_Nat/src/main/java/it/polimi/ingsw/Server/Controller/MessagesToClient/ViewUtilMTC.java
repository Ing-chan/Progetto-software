package it.polimi.ingsw.Server.Controller.MessagesToClient;

import it.polimi.ingsw.Client.Controller.Executor.UpdateVirtualViewExecutor;
import it.polimi.ingsw.Network.Virtuals.*;
import it.polimi.ingsw.Server.Model.Board.ScoreBoard;
import it.polimi.ingsw.Server.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.Server.Model.Cards.PlayableCard;
import it.polimi.ingsw.Server.Model.Enums.TurnPhase;
import it.polimi.ingsw.Server.Model.Player.Player;

import java.util.ArrayList;

public class ViewUtilMTC extends MessageToClient {


    ArrayList<String> players;
    boolean isLastTurnsSet;
    int lastTurn;
    ArrayList<String> messages;
    ArrayList<VirtualCard> drawableCards;
    int remainingCards;
    VirtualScoreBoard virtualScoreBoard;
    String activePlayer;
    TurnPhase turnPhase;


    public ViewUtilMTC(Player player, ArrayList<String> players, ArrayList<PlayableCard> drawableCards, int remaining, ScoreBoard scoreBoard, String activePlayer, TurnPhase turnPhase, boolean isLastTurnsSet, int lastTurn, ArrayList<String> messages) {
        this.type = MTCtype.VIEWUTIL;
        this.players = players;

        this.drawableCards = new ArrayList<>();
        for (PlayableCard card : drawableCards) {
            ArrayList<String> frontPrint = card.toPrintString();
            card.FlipCard();
            ArrayList<String> backPrint= card.toPrintString();
            card.FlipCard();
            this.drawableCards.add(new VirtualCard(card.getIDCard(), card.getCardSide(), card.getColour(), card.getCardType(), frontPrint, backPrint));
        }

        this.remainingCards = remaining;

        this.virtualScoreBoard = new VirtualScoreBoard(scoreBoard.toPrint());
        for (Player Splayer : scoreBoard.getPlayers()) {
            this.virtualScoreBoard.put(Splayer.getNickname(), scoreBoard.getPlayerScore(Splayer.getNickname()));
        }

        this.activePlayer = activePlayer;
        this.turnPhase = turnPhase;


        this.player = new VirtualPlayer(player.getNickname());
        ArrayList<VirtualCard> playerCards = new ArrayList<>();
        for (PlayableCard card : player.getMyCards()) {
            ArrayList<String> frontPrint = card.toPrintString();
            card.FlipCard();
            ArrayList<String> backPrint= card.toPrintString();
            card.FlipCard();
            playerCards.add(new VirtualCard(card.getIDCard(), card.getCardSide(), card.getColour(), card.getCardType(), frontPrint, backPrint));
        }
        this.player.setPlayerCards(playerCards);
        this.player.setVirtualBoard(new VirtualBoard(player.getpBoard().getPlayablePositionsArrayList(), player.getpBoard().toPrint()));
        this.player.setVirtualInventory(new VirtualInventory(player.getpBoard().getInventory().getAll(), player.getpBoard().getInventory().toPrint()));

        ArrayList<VirtualObjective> playerObjects = new ArrayList<>();
        for (ObjectiveCard card : player.getObjectives()) {
            playerObjects.add(new VirtualObjective(card.getIDCard(), card.toPrintString()));
        }
        this.player.setPlayerObject(playerObjects);

        this.isLastTurnsSet = isLastTurnsSet;
        this.lastTurn = lastTurn;
        this.messages = messages;
    }

    public VirtualDrawableCards getDrawableCards() {
        return new VirtualDrawableCards(drawableCards, remainingCards);
    }

    public VirtualScoreBoard getVirtualScoreBoard() {
        return virtualScoreBoard;
    }

    public boolean isLastTurnsSet() {
        return isLastTurnsSet;
    }

    public int getLastTurn() {
        return lastTurn;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public String getActivePlayer() {
        return activePlayer;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    @Override
    public void update() {
        UpdateVirtualViewExecutor.execute(this);
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

}
