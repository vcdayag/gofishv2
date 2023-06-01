package io.github.p0lbang.gofish.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.util.Map.entry;

public class Player {
    public final String name;
    public final int ID;
    @SuppressWarnings("CanBeFinal")
    public Deck hand;
    public Deck completed;

    public Player() {
        this.name = "name";
        this.hand = new Deck();
        this.completed = new Deck();
        this.ID = 0;
    }

    public Player(String name) {
        this.name = name;
        this.hand = new Deck();
        this.completed = new Deck();
        this.ID = 0;
    }

    public Player(String name, int id) {
        this.name = name;
        this.hand = new Deck();
        this.completed = new Deck();
        this.ID = id;
    }

    public int getID() {
        return this.ID;
    }

    public void addCard(Card card) {
        this.hand.addCard(card);
        this.checkCompleted();
        this.hand.sortDeck();
    }

    public Boolean checkHand(String rank) {
        return this.hand.checkRank(rank);
    }

    public void addMultipleCards(ArrayList<Card> cards) {
        this.hand.addCardMultiple(cards);
        this.checkCompleted();
        this.hand.sortDeck();
    }

    public int getHandCount() {
        return this.hand.size();
    }

    public int getCompletedSuits() {
        return this.completed.size() / 4;
    }

    public ArrayList<Card> giveCards(String rank) {
        ArrayList<Card> stolen = this.hand.stealCard(rank);
        this.hand.countRanksHeld();
        return stolen;
    }

    public Map<String, String> ask(ArrayList<String> playerNames) {
        Random rand = new Random();

        Card selectedCard = hand.selectRandomCard();
        String selectedPlayer = playerNames.get(rand.nextInt(playerNames.size()));

        return Map.ofEntries(entry("asker", this.name),
                entry("rank", selectedCard.getRank()),
                entry("target", selectedPlayer));
    }

    public void checkCompleted() {
        HashMap<String, Integer> test = this.hand.getRanksHeld();
        for (String key : test.keySet()) {
            if (test.get(key) != 4) {
                continue;
            }
            this.completed.addCardMultiple(this.hand.stealCard(key));
        }
        this.hand.countRanksHeld();
    }

    public String getName() {
        return this.name;
    }

    public void displayHand() {
        this.hand.display();
    }



    public void displayCompleted() {
        this.completed.display();
    }

    public void displayAll() {
        System.out.println(this.name);
        System.out.println("Hand:");
        this.displayHand();
        System.out.println("Completed:");
        this.displayCompleted();
    }


    public String[] getHand() {
        return this.hand.getDeckAsStringArray();
    }

    public String[] getHandRanks() {
        return this.hand.getRanksHeld().keySet().toArray(new String[0]);
    }

    public Deck Network_getHand() {
        return this.hand;
    }


    public void parseNetworkCards(String[] cards) {

    }
}
