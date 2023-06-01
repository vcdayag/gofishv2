package io.github.p0lbang.gofish.game;

public class Card {
    public final int hierarchy; // for sorting purposes
    public final String rank; // hearts, spades, etc.
    public final String name; // ace, king, 6, etc.

    public Card() {
        this.hierarchy = 0;
        this.rank = "rank";
        this.name = "name";
    }

    public Card(int hierarchy, String rank, String name) {
        this.hierarchy = hierarchy;
        this.rank = rank;
        this.name = name;
    }

    public int getHierarchy() {
        return hierarchy;
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
