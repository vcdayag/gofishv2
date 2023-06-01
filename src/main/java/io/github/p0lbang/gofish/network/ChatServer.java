package io.github.p0lbang.gofish.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import io.github.p0lbang.gofish.MainApp;
import io.github.p0lbang.gofish.game.GameServer;
import io.github.p0lbang.gofish.game.Player;
import io.github.p0lbang.gofish.network.packets.PacketChatMessage;
import io.github.p0lbang.gofish.network.packets.PacketGameStart;
import io.github.p0lbang.gofish.network.packets.PacketPlayerAction;
import io.github.p0lbang.gofish.network.packets.PacketPlayerJoin;

import java.io.IOException;

// ChatServer.java
public class ChatServer implements ChatInterface {
    public static String USERNAME;
    private static Server server;
    private static MainApp GUI;
    private static GameServer GAMEServer;

    private static String IPADDR;
    private static int PORT;

    public ChatServer(MainApp gui, String name, String ipaddr, int port) {
        GUI = gui;
        USERNAME = name;
        IPADDR = ipaddr;
        PORT = port;
        GAMEServer = (GameServer) GUI.gameHandler;
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initialize() throws IOException {
        server = new Server();
        Network.register(server);

        server.addListener(new Listener() {
            public void connected(Connection connection) {

            }

            public void received(Connection connection, Object object) {
                if (object instanceof PacketChatMessage) {
                    PacketChatMessage chatMessage = (PacketChatMessage) object;
                    GUI.addToChatBar(chatMessage.senderName + ": " + chatMessage.messageText);
                    server.sendToAllExceptTCP(connection.getID(), chatMessage);
                } else if (object instanceof PacketPlayerJoin) {
                    PacketPlayerJoin packet = (PacketPlayerJoin) object;
                    GAMEServer.Network_AddPlayer(packet.name, connection.getID());
                } else if (object instanceof PacketPlayerAction) {
                    PacketPlayerAction action = (PacketPlayerAction) object;
//                    GUI.gameLogic.
                    /*server.sendToAllExceptTCP(connection.getID(), action);*/
                }
            }
        });

        server.start();
        server.bind(PORT);
    }

    public void GUI_startGame() {
        GAMEServer.setupCards();
        for (Player player : GAMEServer.players.PlayerList()) {
            PacketGameStart packet = new PacketGameStart();
            packet.player = player;
            server.sendToTCP(player.getID(), packet);
            player.displayHand();
        }
    }

    public void sendMessage(String message) {
        PacketChatMessage chatMessage = new PacketChatMessage();
        chatMessage.senderName = USERNAME;
        chatMessage.messageText = message;
        server.sendToAllTCP(chatMessage);
    }

    @Override
    public void joinServer(String name) {

    }

    /*public void sendAction(PlayerAction action) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.senderName = USERNAME;
        chatMessage.messageText = message;
        server.sendToAllTCP(chatMessage);
    }*/
}