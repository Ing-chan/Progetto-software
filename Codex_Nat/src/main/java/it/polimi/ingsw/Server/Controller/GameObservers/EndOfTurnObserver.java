package it.polimi.ingsw.Server.Controller.GameObservers;

import it.polimi.ingsw.Server.Controller.MessagesToClient.IsYourTurnMTC;
import it.polimi.ingsw.Server.Controller.TurnHandler;
import it.polimi.ingsw.Server.Model.Player.Player;
import it.polimi.ingsw.Network.Server.Connections.ServerConnectionHandler;
import it.polimi.ingsw.Server.Server;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class EndOfTurnObserver implements Observer {

    TurnHandler turnHandler;
    //todo non gli servono dei player, gli bastano le stringhe
    ArrayList<Player> players;
    ArrayList<ServerConnectionHandler> connections;



    public EndOfTurnObserver(TurnHandler turnHandler, ArrayList<Player> players) {
        this.turnHandler = turnHandler;
        this.players = players;
        connections = new ArrayList<>();
        for (Player p : players) {
            connections.add(Server.getHandle(p.getNickname()));
        }
    }

    @Override
    public void update() {
        if (!turnHandler.isLastTurn()) {
            logger.info("send is your turn to: " + turnHandler.getActivePlayer());
            int index = IntStream.range(0, players.size())
                    .filter(i -> players.get(i).getNickname().equals(turnHandler.getActivePlayer()))
                    .findFirst()
                    .orElse(-1);

            if (index == -1) {
                //todo qualcosa è andato terribilmente storto
                logger.info("Something went terribly wrong");
                return;
            }

            connections.get(index).sendMessage(new IsYourTurnMTC(turnHandler.getActivePlayer()));

        } else {
            logger.info("Game has ended\n");
        }
        }
    }

