package io.github.p0lbang.gofish.exp2;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

// ChatServer.java
public class ChatServer {

    public ChatServer() {
        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void run() throws IOException {
        System.out.println("hi");
        Server server = new Server();
        Network.register(server);

        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ChatMessage) {
                    ChatMessage chatMessage = (ChatMessage) object;
                    System.out.println(chatMessage.senderName + ": " + chatMessage.messageText);
                    server.sendToAllExceptTCP(connection.getID(), chatMessage);
                }
            }
        });

        server.start();
        server.bind(54555, 54777);


    }
}