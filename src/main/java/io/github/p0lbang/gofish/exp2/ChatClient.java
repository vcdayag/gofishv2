package io.github.p0lbang.gofish.exp2;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import io.github.p0lbang.gofish.MainApp;

import java.io.IOException;

// ChatClient.java
public class ChatClient implements ChatInterface {

    public static Client client;
    public static String name;

    private static MainApp GUI;

    public ChatClient(MainApp gui, String inname) {
        try {
            GUI = gui;
            name = inname;
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initialize() throws IOException {
        System.out.print("INSIDE ");
        client = new Client();
        Network.register(client);

        client.start();
        client.connect(5000, "localhost", 50000, 54777);

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ChatMessage) {
                    ChatMessage chatMessage = (ChatMessage) object;
                    GUI.addToChatBar(chatMessage.senderName + ": " + chatMessage.messageText);
                }
            }
        });

//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter your name: ");
//        String name = scanner.nextLine();

        //noinspection InfiniteLoopStatement
//        while (true) {
//            System.out.print("> ");
//            String messageText = scanner.nextLine();
//
//            ChatMessage chatMessage = new ChatMessage();
//            chatMessage.senderName = name;
//            chatMessage.messageText = messageText;
//            client.sendTCP(chatMessage);
//        }
    }

    public void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.senderName = name;
        chatMessage.messageText = message;
        client.sendTCP(chatMessage);
    }
}