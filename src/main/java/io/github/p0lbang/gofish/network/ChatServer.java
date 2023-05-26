package io.github.p0lbang.gofish.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import io.github.p0lbang.gofish.MainApp;

import java.io.IOException;

// ChatServer.java
public class ChatServer implements ChatInterface {
    public static String USERNAME;
    private static Server server;
    private static MainApp GUI;

    private static String IPADDR;
    private static int PORT;

    public ChatServer(MainApp gui, String name, String ipaddr, int port) {
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
        server = new Server();
        Network.register(server);

        server.addListener(new Listener() {
            /*public void connected(Connection connection) {
                GUI.addToChatBar("kumonek bhie");
            }*/
            public void received(Connection connection, Object object) {
                if (object instanceof ChatMessage) {
                    ChatMessage chatMessage = (ChatMessage) object;
                    GUI.addToChatBar(chatMessage.senderName + ": " + chatMessage.messageText);
                    server.sendToAllExceptTCP(connection.getID(), chatMessage);
                }
            }
        });

        server.start();
        server.bind(PORT);
    }

    public void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.senderName = USERNAME;
        chatMessage.messageText = message;
        server.sendToAllTCP(chatMessage);
    }
}