package it.polimi.ingsw.Server;


import it.polimi.ingsw.Client.Controller.MessagesToServer.EndTurnMTS;
import it.polimi.ingsw.Client.Controller.MessagesToServer.MessageToServer;
import it.polimi.ingsw.Network.Server.Connections.ServerConnectionHandler;
import it.polimi.ingsw.Network.Server.Connections.ServerRMI;
import it.polimi.ingsw.Network.Server.Connections.SocketConnection;
import it.polimi.ingsw.Network.Server.LoggerUtility;
import it.polimi.ingsw.Server.Controller.GamesManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.rmi.RemoteException;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;


public class Server {

    static final int SOCKET_PORT = 42069;
    static final int RMI_PORT = 7777;
    final String serverIP;
//    final String serverIP = "192.168.196.6";

    static ServerSocket serverSocket;
    static ServerRMI serverRMI;


    private static Queue<MessageToServer> messageQueue;
    private static ConcurrentHashMap<String, ServerConnectionHandler> clients;
    final GamesManager gamesManager;
    private static final Logger logger = LoggerUtility.getLogger();

    public static void main(String[] args) {
        Server server = new Server(args[0]);
//        Server server = new Server("127.0.0.1");
//        Server server = new Server("192.168.1.157");
        server.startSocketServer();
        server.startRMIServer();
    }

    public Server(String ip) {
        serverIP = ip;
        clients = new ConcurrentHashMap<>();
        gamesManager = new GamesManager();
        messageQueue = new ConcurrentLinkedQueue<>();
    }


    public void startSocketServer() {

        try {
            serverSocket = new ServerSocket();
            SocketAddress endpoint = new InetSocketAddress(serverIP, SOCKET_PORT);
            int backlog = 50; // Massimo numero di connessioni in coda
            serverSocket.bind(endpoint, backlog);
            logger.info("Socket server created on serverIP " + serverIP + " on port " + SOCKET_PORT + " Backlog set to: " + backlog);
        } catch (IOException e) {
            logger.severe("Socket server failed to start: " + e.getMessage());
        }
        Thread serverSocketListener = new Thread(() -> {
            while (true) {
                try {
                    logger.info("Socket server is listening");
                    Socket socket = serverSocket.accept();
                    SocketConnection connection = new SocketConnection(socket);
                    connection.connect();
                } catch (IOException e) {
                    logger.severe("Socket is no longer listening" + e.getMessage());
                }
            }
        });
        serverSocketListener.start();
    }

    public void startRMIServer() {
        try {
            serverRMI = new ServerRMI(serverIP, RMI_PORT);
        } catch (RemoteException e) {
            logger.severe("RMI server failed to start: " + e.getMessage());
            e.printStackTrace();
        }
        Thread serverRMIlistener = new Thread(() -> {
            while (true) {
                logger.info("RMI server is listening");
                serverRMI.accept();
            }
        });
        serverRMIlistener.start();
    }

    public static boolean login(String username, ServerConnectionHandler connection) {
        if (clients.containsKey(username)) {
            logger.info("username " + username + " already taken. Try another one");
            return false;
        }
        clients.put(username, connection);
        logger.info("Client logged in with username: " + username);
        return true;
    }

    public static void logout(String username, ServerConnectionHandler connection) {
        clients.remove(username, connection);
        logger.info("Client logged out with username: " + username);
        GamesManager.getInstance().GameInterrupted(username);
    }


    public void playerDisconnection(String username) {
        synchronized (clients) {
            clients.remove(username);
        }
        logger.info("Player " + username + " disconnected");
    }

    public void disconnectStopServer() {
        synchronized (clients) {
            clients.values().forEach(ServerConnectionHandler::disconnect);
            clients.clear();
        }
        logger.info("Server stopped");
    }

    //todo probabilmente da cancellare
    public void onMessage(MessageToServer message) {

        if (message instanceof EndTurnMTS) {
            processMessages();
        } else {
            messageQueue.add(message);
        }
    }

    public static void processMessages() {
        while (!messageQueue.isEmpty()) {
            MessageToServer message = messageQueue.poll();
            if (message != null) {
                logger.info("Processing message: " + message);
                //todo processing message
            }
        }
    }


    public static int getRMIPort() {
        return RMI_PORT;
    }

    public static int getSocketPort() {
        return SOCKET_PORT;
    }

    public static ServerConnectionHandler getHandle(String username) {
        return clients.get(username);
    }


}

