package it.polimi.ingsw.Model;

import it.polimi.ingsw.Server.Model.Cards.PlayableCard;
import it.polimi.ingsw.Server.Model.Chat.ChatMessage;
import it.polimi.ingsw.Server.Model.GameModel;
import it.polimi.ingsw.Server.Model.Player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameModelTest {

    private GameModel gameModel;

    @Before
    public void setUp() {
        gameModel = new GameModel(4); // supponiamo che il gioco supporti 4 giocatori
        gameModel.addPlayer("Alice");
        gameModel.addPlayer("Bob");
        gameModel.addPlayer("Charlie");
        gameModel.addPlayer("Diana");
    }

    @Test
    public void testAddPlayer() {
        assertEquals(4, gameModel.getConnectedPlayers());
        gameModel.addPlayer("Eve");
        assertEquals(5, gameModel.getConnectedPlayers());
    }

    @Test
    public void testRemovePlayer() {
        gameModel.removePlayer("Bob");
        assertEquals(3, gameModel.getConnectedPlayers());
        assertNull(gameModel.findPlayerByName("Bob"));
    }

    @Test
    public void testFindPlayerByName() {
        Player player = gameModel.findPlayerByName("Alice");
        assertNotNull(player);
        assertEquals("Alice", player.getNickname());
    }

    @Test
    public void testStartGame() {
        gameModel.startGame();
        assertNotNull(gameModel.getScoreBoard());
    }

    @Test
    public void testDrawCard() {
        gameModel.startGame(); // Assicurati che le carte siano generate
        gameModel.DrawCard("Alice", "R1");
        assertEquals(1, gameModel.getPlayer("Alice").getMyCards().size());
    }

    @Test
    public void testAddMessage() {
        ChatMessage message = new ChatMessage("Hello", "Bob", "Alice", false);
        gameModel.addMessage(message);
        ArrayList<String> messages = gameModel.getMessages("Alice");
        assertTrue(messages.contains("Hello"));
    }

    @Test
    public void testGameIsFull() {
        assertTrue(gameModel.gameIsFull());
        gameModel.removePlayer("Diana");
        assertFalse(gameModel.gameIsFull());
    }

    @Test
    public void testGetDrawableCards() {
        ArrayList<PlayableCard> drawableCards = gameModel.GetDrawableCards();
        assertNotNull(drawableCards);
        assertFalse(drawableCards.isEmpty());
    }

    @Test
    public void testSetSecretObjectives() {
        gameModel.startGame();
        gameModel.setSecretObjectives("Alice", "O1");
        Player player = gameModel.getPlayer("Alice");
        assertEquals(1, player.getObjectives().size());
    }

    @Test
    public void testGetScore() {
        gameModel.startGame();
        int score = gameModel.getScore("Alice");
        assertEquals(0, score); // Assuming initial score is 0
    }

    @Test
    public void testCalculateFinalScore() {
        gameModel.startGame();
        gameModel.CalculateFinalSCore();
        assertNotNull(gameModel.getScoreBoard());
    }
}
