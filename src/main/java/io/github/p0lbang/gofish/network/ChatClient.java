package io.github.p0lbang.gofish.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import io.github.p0lbang.gofish.MainApp;
import io.github.p0lbang.gofish.game.GameBase;
import io.github.p0lbang.gofish.game.Player;
import io.github.p0lbang.gofish.network.packets.*;
import javafx.application.Platform;

import java.io.IOException;

// ChatClient.java
public class ChatClient {

    public static Client client;
    public static String USERNAME;
    public static GameBase gameHandler;
    private static MainApp GUI;
    private static String IPADDR;
    private static int PORT;

    public ChatClient(MainApp gui, String name, String ipaddr, int port) {
        GUI = gui;
        USERNAME = name;
        IPADDR = ipaddr;
        PORT = port;
        gameHandler = new GameBase(GUI);
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initialize() throws IOException {
        client = new Client();
        Network.register(client);

        client.start();
        client.connect(5000, IPADDR, PORT);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof PacketStartGame) {
                    PacketStartGame packet = (PacketStartGame) object;
                    Platform.runLater(() -> GUI.startGame());
                } else if (object instanceof PacketChatMessage) {
                    PacketChatMessage chatMessage = (PacketChatMessage) object;
                    if (chatMessage.senderName.isBlank()) {
                        GUI.addToChatBar(chatMessage.messageText);
                    } else {
                        GUI.addToChatBar(chatMessage.senderName + ": " + chatMessage.messageText);
                    }
                } else if (object instanceof PacketUpdatePlayerDetails) {
                    PacketUpdatePlayerDetails packetGameStart = (PacketUpdatePlayerDetails) object;
                    gameHandler.setSelf(packetGameStart.player);
                    gameHandler.players = packetGameStart.playerGroup;
                    gameHandler.PlayerMap = packetGameStart.PlayerMap;
                    Platform.runLater(() -> GUI.updateUI());

                } else if (object instanceof PacketPlayerTurn) {
                    PacketPlayerTurn packet = (PacketPlayerTurn) object;
                    Platform.runLater(() -> {
                        GUI.setCurrentPlayer(packet.id, packet.name);
                        if (gameHandler.getSelf().getHand().length == 0) {
                            GUI.displayGofishButton();
                        }
                    });

                } else if (object instanceof PacketPlayersWaiting) {
                    PacketPlayersWaiting packet = (PacketPlayersWaiting) object;
                    Platform.runLater(() -> {
                        GUI.addToJoinedBar(packet.PlayerMap.keySet());
                    });
                }
            }
        });

    }

    public void sendMessage(String message) {
        PacketChatMessage chatMessage = new PacketChatMessage();
        chatMessage.senderName = USERNAME;
        chatMessage.messageText = message;
        client.sendTCP(chatMessage);
    }

    public void joinServer(String name) {
        PacketPlayerJoin packet = new PacketPlayerJoin();
        packet.name = name;
        client.sendTCP(packet);
    }


    public void checkPlayerCard(Player asker, Player target, String playerSelectedRank) {
        Platform.runLater(() -> {
            PacketPlayerAction packet = new PacketPlayerAction();
            packet.askerID = asker.getID();
            packet.asker = asker.getName();
            packet.target = target.getName();
            packet.targetID = target.getID();
            packet.rank = playerSelectedRank;
            client.sendTCP(packet);
        });

    }

    public void goFish() {
        PacketPlayerNoCard packet = new PacketPlayerNoCard();
        packet.askerID = gameHandler.getSelf().getID();
        client.sendTCP(packet);
    }
}