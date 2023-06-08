package io.github.p0lbang.gofish.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import io.github.p0lbang.gofish.MainApp;
import io.github.p0lbang.gofish.game.GameServer;
import io.github.p0lbang.gofish.game.Player;
import io.github.p0lbang.gofish.network.packets.*;
import javafx.application.Platform;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;

// ChatServer.java
public class ChatServer {
    public static String USERNAME;
    private static Server server;
    private static GameServer GAMEServer;

    private static int PORT;

    private static boolean GAMESTARTED;

    public ChatServer(MainApp gui, String name, String ipaddr, int port) {
        USERNAME = name;
        PORT = port;
        GAMEServer = new GameServer(gui, this);
        GAMESTARTED = false;
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initialize() throws IOException {
        server = new Server();
        Network.register(server);
        InetAddress localhost = InetAddress.getLocalHost();

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
            }

            public void received(Connection connection, Object object) {
                if (object instanceof PacketChatMessage) {
                    PacketChatMessage chatMessage = (PacketChatMessage) object;
                    server.sendToAllExceptTCP(connection.getID(), chatMessage);
                } else if (object instanceof PacketPlayerJoin) {
                    if (GAMESTARTED) {
                        return;
                    }
                    PacketPlayerJoin packet = (PacketPlayerJoin) object;
                    Platform.runLater(() -> {
                        GAMEServer.Network_AddPlayer(packet.name, connection.getID());
                        System.out.println(packet.name);
                        System.out.println(connection.getID());
                        sendMessage(packet.name + " joined the game.");
                        sendMessage("Server IP: " + localhost.getHostAddress() + ":" + PORT);
                        PacketPlayersWaiting packetwaiting = new PacketPlayersWaiting();
                        packetwaiting.PlayerMap = GAMEServer.players.players;
                        server.sendToAllTCP(packetwaiting);
                    });
                } else if (object instanceof PacketPlayerAction) {
                    PacketPlayerAction action = (PacketPlayerAction) object;
                    String status = GAMEServer.NETWORK_checkPlayerCard(action.askerID, action.targetID, action.rank);
                    startgameminimal();
                    if (Objects.equals(status, "End")) {
                        GAME_sendPlayerTurn(GAMEServer.getNextPlayer());
                    } else {
                        GAME_sendPlayerTurn(GAMEServer.getCurrentPlayer());
                    }
                } else if (object instanceof PacketPlayerNoCard) {
                    PacketPlayerNoCard packet = (PacketPlayerNoCard) object;
                    GAMEServer.NETWORK_playerGoFish(packet.askerID);
                    startgameminimal();
                    GAME_sendPlayerTurn(GAMEServer.getNextPlayer());
                }
            }
        });

        server.start();
        server.bind(PORT);
    }

    public static void GAME_sendPlayerTurn(Player player) {
        PacketPlayerTurn packet = new PacketPlayerTurn();
        packet.id = player.getID();
        packet.name = player.getName();
        server.sendToAllTCP(packet);
    }

    public static void startgameminimal() {
        for (Player player : GAMEServer.players.PlayerList()) {
            PacketUpdatePlayerDetails packet = new PacketUpdatePlayerDetails();
            packet.player = player;
            packet.PlayerMap = GAMEServer.getTargetPlayersMap(player.getName());
            server.sendToTCP(player.getID(), packet);
            player.displayHand();
        }
    }

    public static void GUI_startGame() {
        GAMESTARTED = true;
        PacketStartGame packet = new PacketStartGame();
        server.sendToAllTCP(packet);
        GAMEServer.setupCards();
        Platform.runLater(() -> {
            startgameminimal();
            GAME_sendPlayerTurn(GAMEServer.getNextPlayer());
        });

    }

    public static void sendMessage(String message) {
        PacketChatMessage chatMessage = new PacketChatMessage();
        chatMessage.senderName = "";
        chatMessage.messageText = message;
        server.sendToAllTCP(chatMessage);
    }

}