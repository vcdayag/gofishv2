package io.github.p0lbang.gofish.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import io.github.p0lbang.gofish.MainApp;

import java.io.IOException;

// ChatServer.java
public class ChatServer implements ChatInterface {
    private static Server server;
    private static String name;
    private static MainApp GUI;

    public ChatServer(MainApp gui, String inname) {
        try {
            GUI = gui;
            name = inname;
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initialize() throws IOException {
        System.out.println("hi");
        server = new Server();
        Network.register(server);

        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ChatMessage) {
                    ChatMessage chatMessage = (ChatMessage) object;
                    GUI.addToChatBar(chatMessage.senderName + ": " + chatMessage.messageText);
                    server.sendToAllExceptTCP(connection.getID(), chatMessage);
                }
            }
        });

        server.start();
        server.bind(50000, 54777);


    }

    public void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.senderName = name;
        chatMessage.messageText = message;
        server.sendToAllTCP(chatMessage);
    }
}