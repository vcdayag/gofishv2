package io.github.p0lbang.gofish.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import io.github.p0lbang.gofish.MainApp;

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
                if (object instanceof ChatMessage) {
                    ChatMessage chatMessage = (ChatMessage) object;
                    GUI.addToChatBar(chatMessage.senderName + ": " + chatMessage.messageText);
                }
            }
        });

    }

    public void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.senderName = USERNAME;
        chatMessage.messageText = message;
        client.sendTCP(chatMessage);
    }
}