package it.polimi.ingsw.Server.Controller.Executor;

import it.polimi.ingsw.Client.Controller.MessagesToServer.ViewUtilitiesMTS;
import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.GamesManager;
import it.polimi.ingsw.Server.Controller.MessagesToClient.ViewUtilMTC;
import it.polimi.ingsw.Server.Model.Board.ScoreBoard;
import it.polimi.ingsw.Server.Model.Cards.PlayableCard;
import it.polimi.ingsw.Server.Model.GameModel;
import it.polimi.ingsw.Server.Model.Player.Player;
import it.polimi.ingsw.Server.Server;

import java.util.ArrayList;


public class ViewUtilitiesMTSExecutor implements MTSExecutor {
    public static void execute(ViewUtilitiesMTS viewUtilitiesMTS) {

        GameController controller = GamesManager.getInstance().getGameController(viewUtilitiesMTS.getGameID());
        GameModel model = controller.getGameModel();

        ArrayList<PlayableCard> drawableCards = model.GetDrawableCards();
        int remainingCards = model.getRemaningCards();
        ScoreBoard scoreBoard = model.getScoreBoard();
        Player player = model.getPlayer(viewUtilitiesMTS.getNickname());
        ArrayList<String> players = model.getPlayersNicks();

        Server.getHandle(viewUtilitiesMTS.getNickname()).sendMessage(new ViewUtilMTC(player, players,drawableCards, remainingCards, scoreBoard, controller.getActivePlayer(),controller.getTurnHandler().getTurnPhase(),   controller.getTurnHandler().lastTurnIsSet(), controller.getTurnHandler().getLastTurn(), controller.getGameModel().getMessages(player.getNickname())));
    }
}

