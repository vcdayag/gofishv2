package io.github.p0lbang.gofish.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import io.github.p0lbang.gofish.MainApp;
import io.github.p0lbang.gofish.game.Player;
import io.github.p0lbang.gofish.network.packets.*;
import javafx.application.Platform;

import java.io.IOException;

// ChatClient.java
public class ChatClient implements ChatInterface {

    public static Client client;
    public static String USERNAME;

    private static MainApp GUI;

    private static String IPADDR;
    private static int PORT;

    public ChatClient(MainApp gui, String name, String ipaddr, int port) {
        GUI = gui;
        USERNAME = name;
        IPADDR = ipaddr;
        PORT = port;
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
            public void received(Connection connection, Object object) {
                if (object instanceof PacketChatMessage) {
                    PacketChatMessage chatMessage = (PacketChatMessage) object;
                    GUI.addToChatBar(chatMessage.senderName + ": " + chatMessage.messageText);
                } else if (object instanceof PacketGameStart) {
                    PacketGameStart packetGameStart = (PacketGameStart) object;
                    GUI.gameHandler.setSelf(packetGameStart.player);
                    GUI.gameHandler.players = packetGameStart.playerGroup;
//                    GUI.gameHandler.targetPlayers = packetGameStart.targets;
                    GUI.gameHandler.PlayerMap = packetGameStart.PlayerMap;

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GUI.updateUI();
                        }
                    });
                } else if (object instanceof PacketUpdatePlayer) {
                    PacketUpdatePlayer packet = (PacketUpdatePlayer) object;
                    GUI.gameHandler.setSelf(packet.player);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GUI.updateUI();
                        }
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

    public void GAME_Action() {

    }

    @Override
    public void GUI_startGame() {

    }

    @Override
    public void checkPlayerCard(Player asker, Player target, String playerSelectedRank) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                PacketPlayerAction packet = new PacketPlayerAction();
                packet.askerID = asker.getID();
                packet.asker = asker.getName();
                packet.target = target.getName();
                packet.targetID = target.getID();
                packet.rank = playerSelectedRank;
                client.sendTCP(packet);
            }
        });

    }
}