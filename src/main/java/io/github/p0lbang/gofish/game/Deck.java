package io.github.p0lbang.gofish.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class Deck {
    public final int[] HIERARCHY = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    public final String[] RANKS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    public final String[] SUITS = {"C", "S", "H", "D"};
    @SuppressWarnings("CanBeFinal")
    public ArrayList<Card> deck;
    public HashMap<String, Integer> ranksheld;

    public Deck() {
        this.deck = new ArrayList<>();
        this.ranksheld = new HashMap<>();
    }

    public void initializeDeck() {
        for (int index = 0; index < 52; index++) {
            this.deck.add(new Card(HIERARCHY[index % 13], RANKS[index % 13], SUITS[index % 4]));
        }
    }

    public void addCard(Card card) {
        this.deck.add(card);
        this.sortDeck();
        this.countRanksHeld();
    }

    public void sortDeck() {
        this.deck.sort(Comparator.comparing(Card::getHierarchy));
    }

    public void addCardMultiple(ArrayList<Card> cards) {
        this.deck.addAll(cards);
        this.countRanksHeld();
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

        for (Card card : stolen) {
            this.removeCard(card);
        }

        return stolen;
    }

    public void countRanksHeld() {
        this.ranksheld.clear();
        for (Card card : this.deck) {
            this.ranksheld.computeIfPresent(card.getRank(), (key, value) -> value + 1);
            this.ranksheld.putIfAbsent(String.valueOf(card.getRank()), 1);
        }
    }

    public HashMap<String, Integer> getRanksHeld() {
        return this.ranksheld;
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

    public Card selectRandomCard() {
        int index = new Random().nextInt(this.deck.size());
        return this.deck.get(index);
    }

    public Card getRandomCard() {
        System.out.println(this.deck.size());
        int index = new Random().nextInt(this.deck.size());
        return this.removeCard(index);
    }

    public void display() {
        for (Card card : this.deck) {
            System.out.printf("%4s", card.stringify());
            System.out.print(" | ");
        }
        System.out.println();
    }

    public String[] getDeckAsStringArray() {
        ArrayList<String> deckStrings = new ArrayList<>();
        for (Card card : this.deck) {
            deckStrings.add(card.stringify());
        }
        return deckStrings.toArray(new String[0]);
    }

    public boolean isEmpty() {
        return this.deck.isEmpty();
    }

    public int size() {
        return this.deck.size();
    }

}
