package it.polimi.ingsw.Server.Controller.Executor;

import it.polimi.ingsw.Client.Controller.MessagesToServer.DrawCardMTS;
import it.polimi.ingsw.Server.Controller.GamesManager;
import it.polimi.ingsw.Server.Controller.MessagesToClient.notOkayMTC;
import it.polimi.ingsw.Server.Controller.MessagesToClient.okayMTC;
import it.polimi.ingsw.Server.Controller.TurnHandler;
import it.polimi.ingsw.Server.Model.Enums.TurnPhase;
import it.polimi.ingsw.Server.Model.Exceptions.NotDrawableCardException;
import it.polimi.ingsw.Server.Model.GameModel;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Server;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DrawCardMTSExecutor implements MTSExecutor {


    public static void execute(DrawCardMTS drawCardMTS) {
        final Logger logger = LoggerUtility.getLogger();
        String activePlayer = GamesManager.getInstance().getGameController(drawCardMTS.getGameID()).getTurnHandler().getActivePlayer();
        TurnHandler turnHandler = GamesManager.getInstance().getGameController(drawCardMTS.getGameID()).getTurnHandler();
        GameModel game = GamesManager.getInstance().getGameController(drawCardMTS.getGameID()).getGameModel();

        String player = drawCardMTS.getNickname();
        String chosenCard = drawCardMTS.getCardID();

        try {
            game.validDraw(chosenCard);

           if (activePlayer.equals(player) && turnHandler.getTurnPhase() == TurnPhase.hasToDrawCard) {


            game.DrawCard(player, chosenCard);
            turnHandler.nextTurnPhase();
            logger.log(Level.FINE, "GAME:" + drawCardMTS.getGameID() +" logging DrawCardMTS");
            logger.info("Card " +chosenCard+" was drawn");
            Server.getHandle(player).sendMessage(new okayMTC());
           }
        } catch (NotDrawableCardException e) {
            logger.severe(e.getMessage());
            Server.getHandle(player).sendMessage(new notOkayMTC(e.getMessage()));
        }
    }

}
