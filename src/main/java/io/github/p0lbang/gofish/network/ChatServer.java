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
import java.util.Objects;

// ChatServer.java
public class ChatServer implements ChatInterface {
    public static String USERNAME;
    private static Server server;
    private static GameServer GAMEServer;

    private static int PORT;

    public ChatServer(MainApp gui, String name, String ipaddr, int port) {
        USERNAME = name;
        PORT = port;
        GAMEServer = new GameServer(gui, this);
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initialize() throws IOException {
        server = new Server();
        Network.register(server);
        System.out.println(server.getConnections().size());

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {

            }

            public void received(Connection connection, Object object) {
                if (object instanceof PacketChatMessage) {
                    PacketChatMessage chatMessage = (PacketChatMessage) object;
//                    GUI.addToChatBar(chatMessage.senderName + ": " + chatMessage.messageText);
                    server.sendToAllExceptTCP(connection.getID(), chatMessage);
                } else if (object instanceof PacketPlayerJoin) {
                    PacketPlayerJoin packet = (PacketPlayerJoin) object;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GAMEServer.Network_AddPlayer(packet.name, connection.getID());
                            System.out.println(packet.name);
                            System.out.println(connection.getID());
                        }
                    });
                } else if (object instanceof PacketPlayerAction) {
                    /*Platform.runLater(new Runnable() {
                        @Override
                        public void run() {*/

                    PacketPlayerAction action = (PacketPlayerAction) object;
                    String status = GAMEServer.NETWORK_checkPlayerCard(action.askerID, action.targetID, action.rank);

                    PacketUpdatePlayer askerupdate = new PacketUpdatePlayer();
                    askerupdate.player = GAMEServer.getPlayer(action.asker);
                    server.sendToTCP(action.askerID, askerupdate);

                    PacketUpdatePlayer targetupdate = new PacketUpdatePlayer();
                    targetupdate.player = GAMEServer.getPlayer(action.target);
                    server.sendToTCP(action.targetID, targetupdate);

                    if (Objects.equals(status, "End")) {
                        GAME_sendPlayerTurn(GAMEServer.getNextPlayer());
                    } else {
                        GAME_sendPlayerTurn(GAMEServer.getCurrentPlayer());
                    }
                   /*     }
                    });*/
                }
            }
        });

        server.start();
        server.bind(PORT);
    }

    public void GUI_startGame() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GAMEServer.setupCards();
                for (Player player : GAMEServer.players.PlayerList()) {
                    PacketGameStart packet = new PacketGameStart();
                    packet.player = player;
//                    packet.targets = GAMEServer.getTargetPlayers(player.getName());
                    packet.PlayerMap = GAMEServer.getTargetPlayersMap(player.getName());
                    server.sendToTCP(player.getID(), packet);
                    player.displayHand();
                }
                GAME_sendPlayerTurn(GAMEServer.getNextPlayer());
            }
        });

    }

    @Override
    public void checkPlayerCard(Player self, Player player, String playerSelectedRank) {

    }

    public void sendMessage(String message) {
    }

    @Override
    public void joinServer(String name) {

    }

    @Override
    public void GAME_Action() {

    }

    public static void GAME_sendPlayerTurn(Player player) {
        PacketPlayerTurn packet = new PacketPlayerTurn();
        packet.id = player.getID();
        packet.name = player.getName();
        server.sendToAllTCP(packet);
    }