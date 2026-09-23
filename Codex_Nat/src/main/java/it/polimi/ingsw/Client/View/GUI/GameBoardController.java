package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.MessagesToServer.DrawCardMTS;
import it.polimi.ingsw.Client.Controller.MessagesToServer.EndTurnMTS;
import it.polimi.ingsw.Client.Controller.MessagesToServer.PlaceCardMTS;
import it.polimi.ingsw.Client.Controller.MessagesToServer.ViewUtilitiesMTS;
import it.polimi.ingsw.Network.Client.ClientConnectionHandler;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Network.Virtuals.*;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MTCtype;
import it.polimi.ingsw.Server.Controller.MessagesToClient.MessageToClient;
import it.polimi.ingsw.Server.Controller.MessagesToClient.ViewUtilMTC;
import it.polimi.ingsw.Server.Model.Enums.Resource;
import it.polimi.ingsw.Server.Model.Enums.TurnPhase;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class GameBoardController extends Gui implements Initializable {
    @FXML
    public ImageView firstCard;
    @FXML
    public ImageView res1;
    @FXML
    public ImageView res2;
    @FXML
    public ImageView res3;
    @FXML
    public ImageView gold1;
    @FXML
    public ImageView gold2;
    @FXML
    public ImageView gold3;
    @FXML
    public ImageView secretObj;
    @FXML
    public ImageView commonObj1;
    @FXML
    public ImageView commonObj2;
    @FXML
    public ImageView card1;
    @FXML
    public ImageView card2;
    @FXML
    public ImageView card3;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public Pane scrollPaneContent;
    @FXML
    public Text fungi;
    @FXML
    public Text insect;
    @FXML
    public Text animal;
    @FXML
    public Text plant;
    @FXML
    public Text itemquill;
    @FXML
    public Text manuscript;
    @FXML
    public Text inkwell;
    @FXML
    public Text points;
    @FXML
    public Text player4;
    @FXML
    public Text player3;
    @FXML
    public Text player2;
    @FXML
    public Text player1;
    @FXML
    public ImageView token1;
    @FXML
    public ImageView token2;
    @FXML
    public ImageView token3;
    @FXML
    public ImageView token4;
    @FXML
    public Button rulesButton;
    @FXML
    public Button scoreboardButton;


    static ClientController gui;
    static VirtualView view;

    @FXML
    public Text nickname;
    @FXML
    public Text gameStatus;
    @FXML
    public Button okButton;
    @FXML
    public Button chatButton;
    ClientConnectionHandler clientConnectionHandler;
    VirtualPlayer player;
    ArrayList<VirtualCard> drawables;
    ArrayList<VirtualCard> playerCards;
    boolean firstTurn = true;
    VirtualCard playedCard = null; // carta della mia mano che ho giocato
    ImageView slctdCard; // img view della carta che ho appena cliccato
    double CARD_X = 3400;
    double CARD_Y = 1975;
    ArrayList<Point> validCoords;
    static HashMap<String, Integer> scoreboard = new HashMap<>();
    static String playerOne;
    static String playerTwo;
    static String playerThree;
    static String playerFour;
    static ArrayList<Integer> playersScores = new ArrayList<>();
    ArrayList<String> playersTurns = new ArrayList<>();
    boolean cancel = false;
    boolean lastTurn = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        view = VirtualView.getInstance();
        gui = ClientController.getInstance();
        clientConnectionHandler = gui.getClientConnectionHandler();
        drawables = view.getVirtualDrawableCards().getDrawablaCards();
        player = view.getVirtualPlayer();
        playerCards = player.getPlayerCards();
        scoreboardButton.setDisable(true);

        // Calcola i valori di vvalue e hvalue per centrare la ScrollPane
        double vvalue = calculateVValueToCenter();
        double hvalue = calculateHValueToCenter();

        // Imposta i valori di vvalue e hvalue della ScrollPane
        scrollPane.setVvalue(vvalue);
        scrollPane.setHvalue(hvalue);

        // Add click listeners to cards
        addFirstCardListener(firstCard);
        Point coords = new Point(0, 0);
        firstCard.getProperties().put("coords", coords);

        disableDrawables();
        disableMyCards();
        nickname.setText(gui.getUsername());
        ClientController.getInstance().getClientConnectionHandler().SendMessageToSever(new ViewUtilitiesMTS(ClientController.getInstance().getGameID(), ClientController.getInstance().getUsername()));
        ViewUtilMTC msg = (ViewUtilMTC) ClientController.getInstance().getClientConnectionHandler().GetMessageToClient();
        msg.update();
        player1.setText(String.valueOf(view.getPlayers().get(0)));
        playersTurns.addFirst(player1.getText());
        playerOne = view.getPlayers().get(0);
        playerTwo = view.getPlayers().get(1);
        player2.setText(String.valueOf(view.getPlayers().get(1)));
        playersTurns.add(1, player2.getText());
        scoreboard.put(playerOne, 0);
        scoreboard.put(playerTwo, 0);
        gameStatus.setText("First turn: place or flip the first card");

        fungi.setText("0");
        insect.setText("0");
        animal.setText("0");
        plant.setText("0");
        itemquill.setText("0");
        manuscript.setText("0");
        inkwell.setText("0");
        points.setText("0");

        if (gui.getPlayersInMatch() == 3) {
            player3.setText(String.valueOf(view.getPlayers().get(2)));
            token3.setVisible(true);
            playerThree = view.getPlayers().get(2);
            scoreboard.put(playerThree, 0);
            playersTurns.add(2, player3.getText());
        }

        if (gui.getPlayersInMatch() == 4) {
            player3.setText(String.valueOf(view.getPlayers().get(2)));
            token3.setVisible(true);
            playerThree = view.getPlayers().get(2);
            scoreboard.put(playerThree, 0);
            playersTurns.add(2, player3.getText());

            player4.setText(String.valueOf(view.getPlayers().get(3)));
            token4.setVisible(true);
            playerThree = view.getPlayers().get(3);
            scoreboard.put(playerFour, 0);
            playersTurns.add(3, player4.getText());
        }

        gui.update();
        updateBoard();

        addMyCardListener(card1);
        addMyCardListener(card2);
        addMyCardListener(card3);

        addDrawCardListener(res1);
        addDrawCardListener(res2);
        addDrawCardListener(res3);
        addDrawCardListener(gold1);
        addDrawCardListener(gold2);
        addDrawCardListener(gold3);

        firstCard.setEffect(createDropShadow());

        ArrayList<VirtualObjective> objectives = player.getPlayerObject();

        createCardImageObj(objectives.getFirst(), commonObj1);
        createCardImageObj(objectives.get(1), commonObj2);
        createCardImageObj(objectives.get(2), secretObj);
        updateBoard();
        Play();
    }

    @FXML
    private void onRulesButton() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/rule-book.fxml")));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Rulebook");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo per calcolare vvalue per centrare verticalmente la ScrollPane
    private double calculateVValueToCenter() {
        double contentHeight = scrollPaneContent.getBoundsInLocal().getHeight();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();
        double offset = (contentHeight - viewportHeight) / 2;
        return offset / contentHeight;
    }

    // Metodo per calcolare hvalue per centrare orizzontalmente la ScrollPane
    private double calculateHValueToCenter() {
        double contentWidth = scrollPaneContent.getBoundsInLocal().getWidth();
        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double offset = (contentWidth - viewportWidth) / 2;
        return offset / contentWidth;
    }

    private void addDrawCardListener(ImageView card) {
        card.setOnMouseClicked(event -> {
            slctdCard = card;
            removeHighlightFromCard(res1);
            removeHighlightFromCard(res2);
            removeHighlightFromCard(res3);
            removeHighlightFromCard(gold1);
            removeHighlightFromCard(gold2);
            removeHighlightFromCard(gold3);
            clearHighlightedPositions();
            highlightCard(card);
            try {
                showDrawConfirmationAlert(card, event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void addMyCardListener(ImageView card) {
        card.setOnMouseClicked(event -> {
            clearHighlightedPositions();
            slctdCard = card;
            removeHighlightFromCard(card1);
            removeHighlightFromCard(card2);
            removeHighlightFromCard(card3);
            highlightCard(card);
            try {
                promptForAction(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            event.consume();
        });
    }

    private void clearHighlightedPositions() {
        // Rimuovi tutti i rettangoli colorati dal contenitore
        scrollPaneContent.getChildren().removeIf(node -> node instanceof Rectangle);
    }

    private ImageView handleRectangleClick(double x, double y, Rectangle rectangle) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/confirm-place-alert.fxml"));
        Parent root = loader.load();
        AtomicReference<ImageView> img = new AtomicReference<>(new ImageView());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Place Card Confirmation");
        Button placeCard = (Button) scene.lookup("#placeCard");
        Button cncl = (Button) scene.lookup("#cncl");
        playedCard = (VirtualCard) slctdCard.getUserData();

        if (cncl != null) {
            cncl.setOnAction(e -> {
                stage.close();
                clearHighlightedPositions();
                cancel = true;
                slctdCard = null;
                img.set(null);
            });
        }

        if (placeCard != null) {
            placeCard.setOnAction(e -> {
                stage.close();
                Point coords = (Point) rectangle.getProperties().get("coords");
                img.set(createImageView(x, y, scrollPaneContent, playedCard, coords));
                slctdCard.setImage(null);
            });
        }

        stage.showAndWait();
        return img.get();

    }

    private void highlightRectangle(Rectangle rectangle) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(189, 255, 22, 0.8));
        dropShadow.setRadius(10);
        rectangle.setEffect(dropShadow);
    }

    private Rectangle createColoredRectangle(Point guiCoords, Point coords) {
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(200);
        rectangle.setHeight(136);
        rectangle.setLayoutX(guiCoords.getX());
        rectangle.setLayoutY(guiCoords.getY());
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.WHITE);
        rectangle.setStrokeWidth(3);

        rectangle.getProperties().put("coords", coords);

        rectangle.setOnMouseClicked(event -> {
            ImageView img;
            try {
                img = handleRectangleClick(guiCoords.getX(), guiCoords.getY(), rectangle);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (!cancel) {
                handlePlaceAction(event, img);
            }
        });

        scrollPaneContent.getChildren().add(rectangle);
        return rectangle;
    }

    private void addFirstCardListener(ImageView card) {
        card.setOnMouseClicked(event -> {
            slctdCard = card;
            highlightCard(card);
            try {
                promptForAction(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void createCardImage(VirtualCard card, ImageView img) {
        String id = card.getID();
        boolean face = card.getFace();
        String path = createPath(id, face);
        loadImage(img, path);
        img.setUserData(card);
    }

    private void createCardImageObj(VirtualObjective card, ImageView img) {
        String id = card.getId();
        String path = createPath(id, true);
        loadImage(img, path);
        img.setUserData(card);
    }

    private void highlightCard(ImageView card) {
        card.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(189,255,22,0.8), 10, 0, 0, 0);");
    }

    private void promptForAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/place-card-alert.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Place or Flip Card");

        Button cancelButton = (Button) scene.lookup("#cancelButton");
        Button placeButton = (Button) scene.lookup("#placeButton");
        Button flipButton = (Button) scene.lookup("#flipButton");


        ImageView card;
        if (event.getSource() instanceof ImageView) {
            card = (ImageView) event.getSource();
        } else {
            // Handle the error, e.g., log it or show a message to the user
            System.err.println("Event source is not an ImageView");
            return;
        }

        slctdCard = (ImageView) event.getSource();

        if (cancelButton != null) {
            cancelButton.setOnAction(e -> stage.close());
        }
        if (placeButton != null) {
            placeButton.setOnAction(e -> {
                if (firstTurn) {
                    handlePlaceAction(event, card);
                    stage.close();
                } else {
                    highlightEmptyPositions();
                    stage.close();
                }
            });
        }
        if (flipButton != null) {
            flipButton.setOnAction(e -> {
                handleFlipAction();
                stage.close();
            });
        }

        stage.showAndWait();
    }

    private void handleFlipAction() {

        if (slctdCard.equals(firstCard)) {
            gui.flip(0);
            createCardImage(playerCards.getFirst(), firstCard);
        } else if (slctdCard.equals(card1)) {
            gui.flip(0);
            createCardImage(playerCards.getFirst(), card1);
        } else if (slctdCard.equals(card2)) {
            gui.flip(1);
            createCardImage(playerCards.get(1), card2);
        } else if (slctdCard.equals(card3)) {
            gui.flip(2);
            createCardImage(playerCards.get(2), card3);
        }
        removeHighlightFromCard(slctdCard);
    }

    private void handlePlaceAction(MouseEvent event, ImageView img) {
        gui = ClientController.getInstance();
        clientConnectionHandler = gui.getClientConnectionHandler();
        VirtualCard cardToPlace;
        Point coords;

        if (firstTurn) {
            coords = (Point) slctdCard.getProperties().get("coords");
            cardToPlace = (VirtualCard) slctdCard.getUserData();
            firstCard.setDisable(true);
            //Platform.runLater(this::disableBoard);
            firstTurn = false;
        } else {
            Rectangle selectedRectangle = (Rectangle) event.getSource();
            cardToPlace = (VirtualCard) slctdCard.getUserData();
            coords = (Point) selectedRectangle.getProperties().get("coords");
        }

        if (slctdCard != null) {
            clientConnectionHandler.SendMessageToSever(new PlaceCardMTS(gui.getGameID(), gui.getUsername(), cardToPlace.getID(), coords, cardToPlace.getFace()));
            clearHighlightedPositions();
            removeHighlightFromCard(slctdCard);
            slctdCard = img;
        }
    }

    private void removeImageView(Pane pane, ImageView imageView) {
        pane.getChildren().remove(imageView);
    }

    private void showDrawConfirmationAlert(ImageView card, MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/draw-card-alert.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Draw Card");

        Button cancel = (Button) scene.lookup("#cancel");
        if (cancel != null) {
            cancel.setOnAction(e -> {
                stage.close();
                removeHighlightFromCard(card);
            });
        }
        Button drawButton = (Button) scene.lookup("#drawButton");
        if (drawButton != null) {
            drawButton.setOnAction(e -> {
                stage.close();
                drawCard(event);
            });
        }
        stage.showAndWait();
    }

    public void drawCard(MouseEvent event) {
        ImageView chosenCard = (ImageView) event.getSource();
        VirtualCard cardChoice = (VirtualCard) chosenCard.getUserData();
        String chosencard = cardChoice.getID();

        ClientController.getInstance().getClientConnectionHandler().SendMessageToSever(new DrawCardMTS(ClientController.getInstance().getGameID(), ClientController.getInstance().getUsername(), chosencard));
        removeHighlightFromCard(chosenCard);
    }

    public static ImageView createImageView(double x, double y, Pane containerPane, VirtualCard card, Point coords) {
        ImageView img = new ImageView();
        img.setFitWidth(200);  // Imposta larghezza desiderata
        img.setFitHeight(136); // Imposta altezza desiderata
        img.setLayoutX(x);     // Imposta posizione x
        img.setLayoutY(y);     // Imposta posizione y

        img.getProperties().put("coords", coords);
        createCardImage(card, img);

        // Aggiungi ImageView al contenitore desiderato
        containerPane.getChildren().add(img);
        return img;
    }

    private void removeHighlightFromCard(ImageView card) {
        card.setStyle(null);
    }

    public static String createPath(String cardID, boolean face) {
        String url = "";
        char id = cardID.charAt(0);
        String cardNr = cardID;

        switch (id) {
            case 'O':
                url = "img/cards/obj/";
                break;
            case 'F':
                url = "img/cards/first/";
                cardNr = cardID.substring(1);
                break;
            case 'G':
                url = "img/cards/gold/";
                cardNr = cardID.substring(1);
                break;
            case 'R':
                url = "img/cards/resource/";
                cardNr = cardID.substring(1);
                break;
        }

        String faceFolder = face ? "front/" : "back/";
        return url + faceFolder + cardNr + ".png";
    }

    private static void loadImage(ImageView imageView, String imagePath) {
        try {
            URL imageUrl = GameBoardController.class.getResource(imagePath);
            if (imageUrl == null) {
                throw new RuntimeException("Image not found: " + imagePath);
            }
            Image image = new Image(imageUrl.toString());
            imageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load image: " + imagePath + ". Error: " + e.getMessage());
        }
    }


    private void updateBoard() {
        gui = ClientController.getInstance();

        playerCards = view.getVirtualPlayer().getPlayerCards();
        drawables = view.getVirtualDrawableCards().getDrawablaCards();
        // Aggiorna le carte del giocatore sulla board

        if (firstTurn) {
            createCardImage(playerCards.getFirst(), firstCard);
        }

        if (playerCards.size() == 3) {
            createCardImage(playerCards.get(0), card1);
            createCardImage(playerCards.get(1), card2);
            createCardImage(playerCards.get(2), card3);
        }

        // Aggiorna le altre parti della board (risorse, altre carte, ecc.)
        if (!drawables.isEmpty()) {

            int resCards = 0;
            int goldCards = 0;
            int firstGoldIndex = -1;

            for (int i = 0; i < drawables.size(); i++) {
                if (drawables.get(i).getID().charAt(0) == 'R') {
                    resCards++;
                } else {
                    goldCards++;
                    if (firstGoldIndex == -1) {
                        firstGoldIndex = i;
                    }
                }
            }

            if (resCards != 0) {
                createCardImage(drawables.getFirst(), res1);
                resCards--;
            } else {
                res1.setImage(null);
                res1.setDisable(true);
            }
            if (resCards != 0) {
                createCardImage(drawables.get(1), res2);
                resCards--;
            } else {
                res2.setImage(null);
                res2.setDisable(true);
            }
            if (resCards != 0) {
                createCardImage(drawables.get(2), res3);
            } else {
                res3.setImage(null);
                res3.setDisable(true);
            }
            if (goldCards != 0) {
                createCardImage(drawables.get(firstGoldIndex), gold1);
                firstGoldIndex++;
                goldCards--;
            } else {
                gold1.setImage(null);
                gold1.setDisable(true);
            }
            if (goldCards != 0) {
                createCardImage(drawables.get(firstGoldIndex), gold2);
                firstGoldIndex++;
                goldCards--;
            } else {
                gold2.setImage(null);
                gold2.setDisable(true);
            }
            if (goldCards != 0) {
                createCardImage(drawables.get(firstGoldIndex), gold3);
            } else {
                gold3.setImage(null);
                gold3.setDisable(true);
            }
        } else {
            res1.setImage(null);
            res1.setDisable(true);
            res2.setImage(null);
            res2.setDisable(true);
            res3.setImage(null);
            res3.setDisable(true);
            gold1.setImage(null);
            gold1.setDisable(true);
            gold2.setImage(null);
            gold2.setDisable(true);
            gold3.setImage(null);
            gold3.setDisable(true);
        }
    }

    private void highlightEmptyPositions() {
        Platform.runLater(() -> {
            cancel = false;
            for (Point validCoord : validCoords) {
                Point guiCoords = calculateCoords(validCoord);
                Rectangle rectangle = createColoredRectangle(guiCoords, validCoord);
                highlightRectangle(rectangle);
            }
        });
    }


    private Point calculateCoords(Point coords) {
        double x = coords.getX();
        double y = coords.getY();
        double calculatedX;
        double calculatedY;

        // Coordinate Y
        if (y == 0) {
            calculatedY = CARD_Y; // Default if y == 0
        } else {
            calculatedY = CARD_Y - (80 * y);
        }

        // Coordinate X
        if (x == 0) {
            calculatedX = CARD_X; // Default if x == 0
        } else {
            calculatedX = CARD_X + (155 * x);
        }

        return new Point((int) calculatedX, (int) calculatedY);
    }

    private void showGoldCardAlert() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/gold-card-alert.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Gold Card Alert");

        // Accessing the button from the loaded FXML
        Button gotItButton = (Button) scene.lookup("#gotItButton");
        if (gotItButton != null) {
            gotItButton.setOnAction(event -> stage.close());
        }

        stage.showAndWait();
    }

    public void updateInventoryAndPoints(MessageToClient mtc) {

        ViewUtilMTC msg = (ViewUtilMTC) mtc;
        VirtualBoard vBoard = msg.getPlayer().getVirtualBoard();
        VirtualInventory virtualInventory = msg.getPlayer().getVirtualInventory();
        VirtualScoreBoard vScoreBoard = msg.getVirtualScoreBoard();
        validCoords = vBoard.getPlayablePositions();
        animal.setText(String.valueOf(virtualInventory.getResources(Resource.ANIMAL)));
        plant.setText(String.valueOf(virtualInventory.getResources(Resource.PLANT)));
        fungi.setText(String.valueOf(virtualInventory.getResources(Resource.FUNGI)));
        inkwell.setText(String.valueOf(virtualInventory.getResources(Resource.VIAL)));
        insect.setText(String.valueOf(virtualInventory.getResources(Resource.INSECT)));
        itemquill.setText(String.valueOf(virtualInventory.getResources(Resource.QUILL)));
        manuscript.setText(String.valueOf(virtualInventory.getResources(Resource.MANUSCRIPT)));

        points.setText(String.valueOf(vScoreBoard.getScore(gui.getUsername())));

        int score1 = vScoreBoard.getScore(playerOne);
        int score2 = vScoreBoard.getScore(playerTwo);
        scoreboard.put(playerOne, score1);
        scoreboard.put(playerTwo, score2);

        if (gui.getPlayersInMatch() == 3) {
            int score3 = vScoreBoard.getScore(playerThree);
            scoreboard.put(playerThree, score3);
        }

        if (gui.getPlayersInMatch() == 4) {
            int score3 = vScoreBoard.getScore(playerThree);
            scoreboard.put(playerThree, score3);
            int score4 = vScoreBoard.getScore(playerFour);
            scoreboard.put(playerFour, score4);
        }


        if (view.getActivePlayer().equals(gui.getUsername())) {
            if (view.getTurnPhase().equals(TurnPhase.hasToPlaceCard)) {
                gameStatus.setText("It's your turn! Pick a card and place it");
            } else if (view.getTurnPhase().equals(TurnPhase.hasToDrawCard)) {
                gameStatus.setText("Draw a gold or resource card");
            }
        } else {
            gameStatus.setText((view.getActivePlayer() + " now Playing"));
        }

        if (((ViewUtilMTC) msg).isLastTurnsSet()) {
            lastTurn = true;
        }
    }

    private void showLastTurnAlert() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/last-turns-allert.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Last Turn Alert");

        // Accessing the button from the loaded FXML
        Button okButton = (Button) scene.lookup("#okButton");
        if (okButton != null) {
            okButton.setOnAction(event -> stage.close());
        }

        stage.showAndWait();
    }

    public static ArrayList<Integer> getPlayersScores() {
        playersScores.add(0, scoreboard.get(playerOne));
        playersScores.add(1, scoreboard.get(playerTwo));
        if (gui.getPlayersInMatch() == 3) {
            playersScores.add(2, scoreboard.get(playerThree));
        }
        if (gui.getPlayersInMatch() == 4) {
            playersScores.add(2, scoreboard.get(playerThree));
            playersScores.add(3, scoreboard.get(playerFour));
        }
        return playersScores;
    }

    public static HashMap<String, Integer> getScoreboard() {
        return scoreboard;
    }

    public void disableMyCards() {
        card1.setDisable(true);
        card2.setDisable(true);
        card3.setDisable(true);
    }

    public void enableMyCards() {
        card1.setDisable(false);
        card2.setDisable(false);
        card3.setDisable(false);
    }

    public void disableDrawables() {
        res1.setDisable(true);
        res2.setDisable(true);
        res3.setDisable(true);
        gold1.setDisable(true);
        gold2.setDisable(true);
        gold3.setDisable(true);
    }

    public void enableDrawables() {
        res1.setDisable(false);
        res2.setDisable(false);
        res3.setDisable(false);
        gold1.setDisable(false);
        gold2.setDisable(false);
        gold3.setDisable(false);
    }

    @FXML
    public void onScoreboardButton() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/scoreboard.fxml")));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Scoreboard");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean flag = false;

    public void highlightCards(boolean flag) {
        if (flag) {
            card1.setEffect(createDropShadow());
            card2.setEffect(createDropShadow());
            card3.setEffect(createDropShadow());
            res1.setEffect(null);
            res2.setEffect(null);
            res3.setEffect(null);
            gold1.setEffect(null);
            gold2.setEffect(null);
            gold3.setEffect(null);
        } else {
            card1.setEffect(null);
            card2.setEffect(null);
            card3.setEffect(null);
            res1.setEffect(createDropShadow());
            res2.setEffect(createDropShadow());
            res3.setEffect(createDropShadow());
            gold1.setEffect(createDropShadow());
            gold2.setEffect(createDropShadow());
            gold3.setEffect(createDropShadow());
        }
    }

    private DropShadow createDropShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20);
        dropShadow.setSpread(0.5);
        dropShadow.setColor(Color.YELLOW);
        return dropShadow;
    }

    public void Play() {
        new Thread(() -> {
            MessageToClient confirmMsg;
            ClientConnectionHandler clientConnectionHandler = ClientController.getInstance().getClientConnectionHandler();
            ClientController gui = ClientController.getInstance();

            gui.update();
            updateBoard();

            //piazzi prima carta
            confirmMsg = ClientController.getInstance().getClientConnectionHandler().GetMessageToClient();
            gui.update();
            updateBoard();

            //ritorna l' ok della prima carta piazzata
            //System.out.println("checkpoint 1" + confirmMsg.getClass());

            firstCard.setEffect(null);

            //aspetta il secondo turno (inizio fase di gioco)
            do{
                confirmMsg = clientConnectionHandler.GetMessageToClient();
                confirmMsg.update();
            } while(!confirmMsg.getType().equals(MTCtype.ISYOURTURN));

//            System.out.println("checkpoint 2" + confirmMsg.getClass());
            enableMyCards();
            card1.setEffect(createDropShadow());
            card2.setEffect(createDropShadow());
            card3.setEffect(createDropShadow());

            do {
                ClientController.getInstance().getClientConnectionHandler().SendMessageToSever(new ViewUtilitiesMTS(ClientController.getInstance().getGameID(), ClientController.getInstance().getUsername()));
                ViewUtilMTC msg = (ViewUtilMTC) ClientController.getInstance().getClientConnectionHandler().GetMessageToClient(); //I wait here
                msg.update();
                updateInventoryAndPoints(msg);

                if (lastTurn && !flag) {
                    Platform.runLater(() -> {
                        try {
                            showLastTurnAlert();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    flag = true;
                }

                gui.update();
                updateBoard();
                enableMyCards();
                updateInventoryAndPoints(msg);
                highlightCards(true);

                do {
                    //okay di place card
                    confirmMsg = gui.getClientConnectionHandler().GetMessageToClient();
                    gui.update();
                    updateBoard();
//                    System.out.println("checkpoint 3" + confirmMsg.getClass());

                    if (confirmMsg.getType().equals(MTCtype.NOTOKAY)) {
                        Platform.runLater(() -> {
                            removeImageView(scrollPaneContent, slctdCard);
                            try {
                                showGoldCardAlert();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            updateBoard();
                        });
                    }

                } while (!confirmMsg.getType().equals(MTCtype.OKAY));

                disableMyCards();
                enableDrawables();
                highlightCards(false);

                ClientController.getInstance().getClientConnectionHandler().SendMessageToSever(new ViewUtilitiesMTS(ClientController.getInstance().getGameID(), ClientController.getInstance().getUsername()));
                msg = (ViewUtilMTC) ClientController.getInstance().getClientConnectionHandler().GetMessageToClient();
                msg.update();
                updateInventoryAndPoints(msg);
                updateBoard();

                LoggerUtility.getLogger().info("Placed a card");

                do {
                    //okay di draw card
                    confirmMsg = ClientController.getInstance().getClientConnectionHandler().GetMessageToClient();
                    gui.update();
                    updateBoard();
//                    System.out.println("checkpoint 4" + confirmMsg.getClass());
                } while (!confirmMsg.getType().equals(MTCtype.OKAY));
                disableDrawables();
                res1.setEffect(null);
                res2.setEffect(null);
                res3.setEffect(null);
                gold1.setEffect(null);
                gold2.setEffect(null);
                gold3.setEffect(null);
                ClientController.getInstance().getClientConnectionHandler().SendMessageToSever(new ViewUtilitiesMTS(ClientController.getInstance().getGameID(), ClientController.getInstance().getUsername()));

                msg = (ViewUtilMTC) ClientController.getInstance().getClientConnectionHandler().GetMessageToClient();
                msg.update();
                gui.update();
                updateInventoryAndPoints(msg);
                updateBoard();

                LoggerUtility.getLogger().info("Drawn a card");

                if (!firstTurn) {
                    clientConnectionHandler.SendMessageToSever(new EndTurnMTS(ClientController.getInstance().getGameID(), ClientController.getInstance().getUsername()));
                    do {
                        //okay di end turn
                        updateBoard();
                        confirmMsg = ClientController.getInstance().getClientConnectionHandler().GetMessageToClient();
//                        System.out.println("checkpoint 5" + confirmMsg.getClass());
                    } while (!confirmMsg.getType().equals(MTCtype.OKAY));
                }
                    updateBoard();

                    //catches the is your turn to repeat the proces or endgameMTC to exit
                    confirmMsg = clientConnectionHandler.GetMessageToClient();

                scoreboardButton.setDisable(false);
            } while (!confirmMsg.getType().equals(MTCtype.ENDGAME));

            ClientController.getInstance().getClientConnectionHandler().SendMessageToSever(new ViewUtilitiesMTS(ClientController.getInstance().getGameID(), ClientController.getInstance().getUsername()));
            ViewUtilMTC msg = (ViewUtilMTC) ClientController.getInstance().getClientConnectionHandler().GetMessageToClient();
            msg.update();
            updateInventoryAndPoints(msg);
            System.out.println("Fine turno");
            StageController.switchToEndScene("fxml/end-game.fxml", "End Game");
        }).start();
    }

    @FXML
    public void onChatButton() {
        StageController.switchToEndScene("fxml/chat.fxml", "Chat");
    }

}
