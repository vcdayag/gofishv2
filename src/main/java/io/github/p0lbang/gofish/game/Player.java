package io.github.p0lbang.gofish.game;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import static java.util.Map.entry;

public class Player {
    String name;
    Deck hand;

    public Player(String name) {
        this.name = name;
        this.hand = new Deck();
    }

    public void addCard(Card card) {
        this.hand.addCard(card);
    }

    public Boolean checkHand(String rank) {
        return this.hand.checkRank(rank);
    }

    public void addMultipleCards(ArrayList<Card> cards) {
        this.hand.addCardMultiple(cards);
    }

    public ArrayList<Card> giveCards(String rank) {
        return this.hand.stealCard(rank);
    }

    public Map<String, String> ask(ArrayList<String> playerNames) {
        Random rand = new Random();

        Card selectedCard = hand.getRandomCard();
        String selectedPlayer = playerNames.get(rand.nextInt(playerNames.size()));

        Map<String, String> asktemplate = Map.ofEntries(entry("asker", this.name),
                entry("rank", selectedCard.getRank()),
                entry("target", selectedPlayer));
        return asktemplate;
    }

    public String getName() {
        return this.name;
    }

}
