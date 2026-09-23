package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.MessagesToClient.IsYourTurnMTC;
import it.polimi.ingsw.Server.Model.Board.ScoreBoard;
import it.polimi.ingsw.Server.Model.Cards.CardCreators.CardCreator;
import it.polimi.ingsw.Server.Model.Cards.CardCreators.ObjectiveCreator;
import it.polimi.ingsw.Server.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.Server.Model.Cards.PlayableCard;
import it.polimi.ingsw.Server.Model.Chat.Chat;
import it.polimi.ingsw.Server.Model.Chat.ChatMessage;
import it.polimi.ingsw.Server.Model.Enums.CardType;
import it.polimi.ingsw.Server.Model.Exceptions.NotDrawableCardException;
import it.polimi.ingsw.Server.Model.Player.Player;
import it.polimi.ingsw.Server.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class GameModel {


    private final ArrayList<Player> players;
    private final int maxPlayers;

    private ScoreBoard points;

    private ArrayList<PlayableCard> deckGoldCards;
    private ArrayList<PlayableCard> deckResourceCards;
    private ArrayList<ObjectiveCard> deckObjectiveCards;
    private ArrayList<PlayableCard> deckFirstCards;
    private final ArrayList<CardCreator> creators;


    private final Chat chat;


    // constructor for GameModel
    public GameModel(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        creators = new ArrayList<>();
        players = new ArrayList<>();
        chat = new Chat();


        generateCards();
    }

    //given an arrayList of cards, the function shuffles the deck
    private void shuffleCards(ArrayList<PlayableCard> Deck) {

        Collections.shuffle(Deck, new Random()); //todo seed tolto
        if (Deck.getFirst().getCardType() != CardType.OBJECTIVECARD) {
            Deck.get(0).FlipCard();
            Deck.get(1).FlipCard();
        }
    }

    private void shuffleObjectives(ArrayList<ObjectiveCard> Deck) {

        Collections.shuffle(Deck, new Random());
    }

    // function sets up the game
    public void startGame() {
        points = new ScoreBoard(players);
        setCommonObjectives();
        giveFirstCards();
        //todo notify observer
    }

    private void generateCards() {

        String[] filenames = {"/FirstCardMain.json", "/GoldCardMain.json", "/ResourceCardMain.json", "/ObjectiveCardMain.json"};

        ArrayList<Thread> threads = new ArrayList<>();

        for (String filename : filenames) {
            CardCreator creator = new CardCreator(filename);
            creators.add(creator);
            Thread thread = new Thread(creator);
            thread.start();
            threads.add(thread);
        }

        ObjectiveCreator ObjCreator = new ObjectiveCreator(filenames[3]);
        Thread Objthread = new Thread(ObjCreator);
        Objthread.start();
        threads.add(Objthread);

        // waiting for all threads to end
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // all decks have been created, the produced cards are now stored in the decks
        deckGoldCards = getCreatorByFileType("/GoldCardMain.json").getCards();
        deckResourceCards = getCreatorByFileType("/ResourceCardMain.json").getCards();
        deckFirstCards = getCreatorByFileType("/FirstCardMain.json").getCards();
        deckObjectiveCards = ObjCreator.getCards();

        shuffleCards(deckGoldCards);
        shuffleCards(deckResourceCards);
        shuffleCards(deckFirstCards);
        shuffleObjectives(deckObjectiveCards);
    }

    public Chat getChat() {
        return chat;
    }

    // adds player to the game
    public void addPlayer(String username) {
        players.add(new Player(username));
    }

    public void removePlayer(String username) { players.remove(findPlayerByName(username)); }

    public Player findPlayerByName(String username){
        for(Player player: players)
        {
            if(player.getNickname().equals(username))
            {
                return player;
            }
        }
        //TODO gestire il return null altrimenti esplode server
        return null;
    }

    public ArrayList<String> getMessages(String player){
        return chat.downloadMessages(player);
    }
    // function draws card for player
    public void DrawCard(String PlayerNickname, String idcard) {
        Player target = findPlayerByNickname(PlayerNickname);
        PlayableCard ChosenCard;
        int CardPosition;

        // draws a RESOURCE card
        if (idcard.startsWith("R")) {
            ChosenCard = findCardByIDCard(deckResourceCards, idcard);
            if(!ChosenCard.getCardSide()){
                ChosenCard.FlipCard();
            }
            target.addCard(ChosenCard);
            CardPosition = deckResourceCards.indexOf(ChosenCard);
            deckResourceCards.remove(CardPosition);
            if (CardPosition != 2 && (deckResourceCards.size()>1)) {
                deckResourceCards.get(1).FlipCard();
            }
        }
        // draws GOLDCARD
        else if (idcard.startsWith("G")) {
            ChosenCard = findCardByIDCard(deckGoldCards, idcard);
            if(!ChosenCard.getCardSide()){
                ChosenCard.FlipCard();
            }
            target.addCard(ChosenCard);
            CardPosition = deckGoldCards.indexOf(ChosenCard);
            deckGoldCards.remove(CardPosition);
            if (CardPosition != 2 && (deckGoldCards.size()>1)) {
                deckGoldCards.get(1).FlipCard();
            }
        }
    }

    public void addMessage(ChatMessage chatMessage){
        chat.addMessage(chatMessage);
    }

    public ArrayList<PlayableCard> GetDrawableCards() {
        ArrayList<PlayableCard> drawableCards = new ArrayList<>();

        // Aggiungi carte dal deckResourceCards se esistono
        addCardsIfExists(deckResourceCards, drawableCards);

        // Aggiungi carte dal deckGoldCards se esistono
        addCardsIfExists(deckGoldCards, drawableCards);

        return drawableCards;
    }

    private void addCardsIfExists(ArrayList<PlayableCard> deck, ArrayList<PlayableCard> drawableCards) {
        for (int i = 0; i < 3; i++) {
            if (i < deck.size()) {
                drawableCards.add(deck.get(i));
            } else {
                break; // Esce dal ciclo se l'indice supera la dimensione della lista
            }
        }
    }

    // function sets common objectives for all players
    private void setCommonObjectives() {
        ObjectiveCard ChosenObjCard;

        for (int i = 0; i < 2; i++) {
            ChosenObjCard = deckObjectiveCards.getFirst();
            deckObjectiveCards.removeFirst();

            for (Player player : players) {
                player.addCard(ChosenObjCard);
            }
        }
    }

    public ArrayList<ObjectiveCard> getTwoObjectives() {
        synchronized (deckObjectiveCards) {
            ArrayList<ObjectiveCard> twoObjectives = new ArrayList<>();
            Random rand = new Random();

            for (int i = 0; i < 2; i++) {
                int index1Obj = rand.nextInt(deckObjectiveCards.size());
                if (deckObjectiveCards.get(index1Obj).isSecret()) {
                    i--;
                } else {
                    twoObjectives.add(deckObjectiveCards.get(index1Obj));
                    deckObjectiveCards.get(index1Obj).setSecret();
                }
            }

            return twoObjectives;
        }
    }

    public void setSecretObjectives(String playerName, String ChosenObjective) {
        findPlayerByNickname(playerName).addCard(findObjCardByIDCard(ChosenObjective));

        boolean flag = true;
        for(Player player: players)
        {
            if(player.getObjectives().size()!=3)
            {
              flag =false;
              break;
            }
        }
        if(flag){
            //todo, farlo fare da un observer
            //at the beggining of the game the active player is set based on the first postion
            Server.getHandle(players.get(0).getNickname()).sendMessage(new IsYourTurnMTC(players.get(0).getNickname()));
        }
    }

    // function gives an initial card to all players
    private void giveFirstCards() {
        for (Player player : players) {
            player.addCard(deckFirstCards.getFirst());
            deckFirstCards.removeFirst();
        }
    }

    public void givePlayableCards() {
        Random rand = new Random();

        for (Player players : players) {
            for (int i = 0; i < 3; i++) {
                int luck = rand.nextInt(2);
                if (luck == 1) {
                    deckResourceCards.get(2).FlipCard();
                    players.addCard(deckResourceCards.get(2));
                    deckResourceCards.remove(2);
                } else {
                    deckGoldCards.get(2).FlipCard();
                    players.addCard(deckGoldCards.get(2));
                    deckGoldCards.remove(2);
                }
            }
        }
    }

    public int getScore(String NickName) {
        return findPlayerByNickname(NickName).getScore();
    }

    public Boolean gameIsFull() {
        return maxPlayers == players.size();
    }

    //utility methods to work with arraylists
    public Player findPlayerByNickname(String nickname) {
        for (Player player : players) {
            if (player.getNickname().equals(nickname)) {
                return player;
            }
        }
        return null;
    }

    //todo testing function, make private
    public PlayableCard findCardByIDCard(ArrayList<PlayableCard> cards, String IDCard) {
        for (PlayableCard Card : cards) {
            if (Card.getIDCard().equals(IDCard)) {
                return Card;
            }
        }
        return null;
    }

    private ObjectiveCard findObjCardByIDCard(String IDCard) {
        for (ObjectiveCard Card : deckObjectiveCards) {
            if (Card.getIDCard().equals(IDCard)) {
                return Card;
            }
        }
        return null;
    }

    private CardCreator getCreatorByFileType(String filename) {
        for (CardCreator creator : creators) {
            if (creator.getFile().equals(filename)) {
                return creator;
            }
        }
        return null;
    }


    //todo funzione di testing
    public Player getPlayer(String player) {
        return findPlayerByNickname(player);
    }

    public ArrayList<PlayableCard> getDeckGoldCards() {
        return deckGoldCards;
    }

    public int getMaxPoints() {
        String Fplayer = points.getFirstPlayer();
        return points.getPlayerScore(Fplayer);
    }

    public boolean decksAreEmpty() {
        return (deckGoldCards.isEmpty() && deckResourceCards.isEmpty());
    }


    public ArrayList<String> getPlayersNicks(){
        return players.stream().map(Player::getNickname).collect(Collectors.toCollection(ArrayList::new));
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getConnectedPlayers() {
        return players.size();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ScoreBoard getScoreBoard() {
        return points;
    }

    public void updateScore(String activePlayer) {
        points.UpdatePlayerScore(activePlayer);
    }

    public int getRemaningCards() {
        return deckGoldCards.size() + deckResourceCards.size();
    }

    public void validDraw(String chosenCard) throws NotDrawableCardException {
        boolean flag = true;

        for (PlayableCard card : GetDrawableCards()) {
            if (card.getIDCard().equals(chosenCard)) {
                flag = false;
                break;
            }
        }

        if (flag) {
            throw new NotDrawableCardException();
        }
    }

    public void CalculateFinalSCore() {
        points.UpdatePlayerScoreFinal();
    }


}