package io.github.p0lbang.gofish.game;

import java.util.Map;
import java.util.Random;

public class Game {
    PlayerGroup players;
    Deck deck;

    public Game() {
        this.players = new PlayerGroup();
        this.deck = new Deck();
        this.deck.initializeDeck();

        this.start();
    }

    public void start() {
        this.getPlayers();
        this.setupCards();
        this.gameloop();
    }

    private void getPlayers() {
        this.players.addPlayer(new Player("Juan"));
        this.players.addPlayer(new Player("Bot"));
        this.players.addPlayer(new Player("Player"));
    }

    private void setupCards() {
        Integer amountofcards = 15;
        Random rand = new Random();
        for (int index = 0; index < amountofcards; index++) {
            for (Player currentPlayer : this.players.PlayerList()) {
                currentPlayer.addCard(deck.getRandomCard());
            }
        }
    }

    public void gameloop() {
        for (String playerName : this.players.NameList()) {
            Player asker = this.players.getPlayer(playerName);
            Map<String, String> askingOutput = asker.ask(this.players.TargetList(asker));

            System.out.println("Asker: " + asker.name + " | Target: " + askingOutput.get("target"));
            asker.hand.display();

            this.checkPlayerCard(asker, this.players.getPlayer(askingOutput.get("target")),
                    askingOutput.get("rank"));

            asker.hand.display();
            System.out.println();
        }
    }

    // game funcitons

    private void getCard(Player asker, Player target, String rank) {
        asker.addMultipleCards(target.giveCards(rank));
    }

    private void playerGoFish(Player asker) {
        asker.addCard(this.deck.getRandomCard());
    }

    private void checkPlayerCard(Player asker, Player target, String rank) {
        if (!target.checkHand(rank)) {
            System.out.println("Go fish");
            this.playerGoFish(asker);
            return;
        }

        System.out.println("Stolen");
        this.getCard(asker, target, rank);
    }

}
