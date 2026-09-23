package it.polimi.ingsw.Server.Controller.Executor;

import it.polimi.ingsw.Client.Controller.MessagesToServer.PlaceCardMTS;
import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.GamesManager;
import it.polimi.ingsw.Server.Controller.MessagesToClient.okayMTC;
import it.polimi.ingsw.Server.Controller.MessagesToClient.notOkayMTC;
import it.polimi.ingsw.Server.Model.Enums.TurnPhase;
import it.polimi.ingsw.Server.Model.Exceptions.CardNotInHandException;
import it.polimi.ingsw.Server.Model.Exceptions.InvalidPositionException;
import it.polimi.ingsw.Server.Model.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.Server.Model.GameModel;
import it.polimi.ingsw.Server.Model.Player.Player;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Server;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaceCardMTSExecutor implements MTSExecutor {
    public static void execute(PlaceCardMTS placeCardMTS) {

        final Logger logger = LoggerUtility.getLogger();

        //we get the variables we will need from the MTS
        GameModel game = GamesManager.getInstance().getGameController(placeCardMTS.getGameID()).getGameModel();

        GameController gameController = GamesManager.getInstance().getGameController(placeCardMTS.getGameID());
        String activePlayer = gameController.getTurnHandler().getActivePlayer();

        Point coords = placeCardMTS.getCoords();
        Player player = game.getPlayer(placeCardMTS.getNickname());
        String chosenCard = placeCardMTS.getCardID();
        boolean selectedFace = placeCardMTS.getSelectedFace();

        try {
            player.validCard(chosenCard);
            player.validCoords(coords);

            if ((placeCardMTS.getNickname().equals(activePlayer)) && gameController.getTurnHandler().getTurnPhase() == TurnPhase.hasToPlaceCard) {
                player.placeCard(chosenCard, coords, selectedFace);
                logger.log(Level.FINE, "GAME:" + placeCardMTS.getGameID() +" logging PlaceCardMTS");
                logger.info("Card " + placeCardMTS.getCardID() + " placed in " + placeCardMTS.getCoords().toString());
                //update behaves differently from turnhandler.nextTurnPhase() in the initial phase of the game.
                gameController.update();
                Server.getHandle(activePlayer).sendMessage(new okayMTC());
            } else {
                Server.getHandle(activePlayer).sendMessage(new notOkayMTC("It's not your turn"));

            }

        } catch (CardNotInHandException | InvalidPositionException | NotEnoughResourcesException e) {
            Server.getHandle(activePlayer).sendMessage(new notOkayMTC(e.getMessage()));
        }
    }
}
