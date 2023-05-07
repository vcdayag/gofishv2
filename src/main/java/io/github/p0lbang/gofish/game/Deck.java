package io.github.p0lbang.gofish.game;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    @SuppressWarnings("CanBeFinal")
    ArrayList<Card> deck;
    final Random randomizer;

    final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

    public Deck() {
        this.deck = new ArrayList<>();
        this.randomizer = new Random();
    }

    public void initializeDeck() {
        for (int index = 0; index < 51; index++) {
            this.deck.add(new Card(RANKS[index % 13], Integer.toString(index)));
        }
    }

    public void addCard(Card card) {
        this.deck.add(card);
    }

    public void addCardMultiple(ArrayList<Card> cards) {
        this.deck.addAll(cards);
    }

    public Card removeCard(Card card) {
        this.deck.remove(card);
        return card;
    }

    public Card removeCard(int index) {
        return this.deck.remove(index);
    }

    public ArrayList<Card> stealCard(String rank) {
        ArrayList<Card> stolen = new ArrayList<>();

        if (!checkRank(rank)) {
            return stolen;
        }

        for (Card card : this.deck) {
            if (card.getRank().equals(rank)) {
                stolen.add(card);
            }
        }

        return stolen;
    }

    public Boolean checkRank(String rank) {
        for (Card card : this.deck) {
            // System.out.println(card.getRank()+" | "+rank);
            if (card.getRank().equals(rank)) {
                // System.out.println("True in check rank");
                return true;
            }
        }
        return false;
    }

    public Card getRandomCard() {
        int index = randomizer.nextInt(this.deck.size());
        return this.removeCard(index);
    }

    public void display() {
        for (Card card : this.deck) {
            System.out.print(card.stringify());
            System.out.print(" | ");
        }
        System.out.println();
    }

}
