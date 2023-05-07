package io.github.p0lbang.gofish.game;

public class Card {
    private final String rank; // hearts, spades, etc.
    private final String name; // ace, king, 6, etc.

    public Card(String rank, String name) {
        this.rank = rank;
        this.name = name;
    }

    public String getRank() {
        return this.rank;
    }

    public String getName() {
        return this.name;
    }

    public Card getCard() {
        return this;
    }

    public String stringify() {
        return this.rank + ":" + this.name;
    }
}
