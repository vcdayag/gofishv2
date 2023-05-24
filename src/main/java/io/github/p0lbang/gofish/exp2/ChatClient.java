package io.github.p0lbang.gofish.exp2;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;
import java.util.Scanner;

// ChatClient.java
public class ChatClient {

    public ChatClient() {
        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void run() throws IOException {
        System.out.print("INSIDE ");
        Client client = new Client();
        Network.register(client);

        client.start();
        client.connect(5000, "localhost", 54555, 54777);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        //noinspection InfiniteLoopStatement
        while (true) {
            System.out.print("> ");
            String messageText = scanner.nextLine();

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.senderName = name;
            chatMessage.messageText = messageText;
            client.sendTCP(chatMessage);
        }
    }
}