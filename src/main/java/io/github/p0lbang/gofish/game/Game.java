package io.github.p0lbang.gofish.game;

import io.github.p0lbang.gofish.MainApp;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Game {
    @SuppressWarnings("CanBeFinal")
    PlayerGroup players;
    @SuppressWarnings("CanBeFinal")
    public Deck deck;

    MainApp GUI;

    public Game(MainApp gui) {
        this.GUI = gui;
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
        int amountofcards = 10;
        Random rand = new Random();
        for (int index = 0; index < amountofcards; index++) {
            for (Player currentPlayer : this.players.PlayerList()) {
                currentPlayer.addCard(deck.getRandomCard());
            }
        }
    }

    public void gameloop() {
        for (String playerName : this.players.playerNames) {
            Player asker = this.players.getPlayer(playerName);
            /* check players hand if empty. if true immediately go fish. */
            if (asker.hand.isEmpty()) {
                this.playerGoFish(asker);
                continue;
            }
            Map<String, String> askingOutput = asker.ask(this.players.TargetList(asker));

            /*System.out.println("Asker: " + asker.name + " | Target: " + askingOutput.get("target"));
            asker.displayHand();*/

            this.checkPlayerCard(asker, this.players.getPlayer(askingOutput.get("target")),
                    askingOutput.get("rank"));

            asker.displayHand();
        }

        System.out.println();

        /*for (String playerName : this.players.NameList()) {
            Player asker = this.players.getPlayer(playerName);
            asker.displayAll();
        }*/
    }

    public void AITurn() {
        ArrayList<String> others = this.getTargetPlayers("Player");
        for (String playerName : others) {
            Player asker = this.players.getPlayer(playerName);
            /* check players hand if empty. if true immediately go fish. */
            if (asker.hand.isEmpty()) {
                this.playerGoFish(asker);
                continue;
            }
            Map<String, String> askingOutput = asker.ask(this.players.TargetList(asker));

            //System.out.println("Asker: " + asker.name + " | Target: " + askingOutput.get("target"));
//            asker.displayHand();

            this.checkPlayerCard(asker, this.players.getPlayer(askingOutput.get("target")),
                    askingOutput.get("rank"));

//            asker.displayHand();
        }

        System.out.println();

   /*     for (String playerName : this.players.NameList()) {
            Player asker = this.players.getPlayer(playerName);
            asker.displayAll();
        }*/
    }

    // game funcitons

    private void getCard(Player asker, Player target, String rank) {
        ArrayList<Card> stolencards = target.giveCards(rank);
        if (this.GUI.currentPlayerName.equals(target.getName())) {
            for (Card card : stolencards) {
                this.GUI.takeCardAnimation(card.stringify());
            }
        }

        asker.addMultipleCards(stolencards);
    }

    private void playerGoFish(Player asker) {
        if (this.deck.isEmpty()) {
            System.out.println("Deck is Empty.");
            return;
        }
        asker.addCard(this.deck.getRandomCard());
    }

    public void checkPlayerCard(Player asker, Player target, String rank) {
        if (!target.checkHand(rank)) {
            System.out.println("Go fish");
            this.playerGoFish(asker);
            return;
        }

        System.out.println("Stolen");
        this.getCard(asker, target, rank);
    }

    public void displayPlayerHand(String playername) {
        Player choosen = this.players.getPlayer(playername);
        choosen.displayHand();
    }


    public String[] getPlayerHand(String playername) {
        Player chosen = this.players.getPlayer(playername);
        return chosen.getHand();
    }

    public String[] getPlayerHandRanks(String playername) {
        Player chosen = this.players.getPlayer(playername);
        return chosen.getHandRanks();
    }

    public ArrayList<String> getTargetPlayers(String askername) {
        Player asker = this.players.getPlayer(askername);
        return this.players.TargetList(asker);
    }

    public Player getPlayer(String playername) {
        return this.players.getPlayer(playername);
    }
}
