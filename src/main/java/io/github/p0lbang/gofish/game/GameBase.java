package io.github.p0lbang.gofish.game;

import io.github.p0lbang.gofish.MainApp;

public class GameBase {
    MainApp GUI;
    Player clientPlayer;

    public GameBase(MainApp gui) {
        this.GUI = gui;
    }

    public Player getSelf() {
        return clientPlayer;
    }

    public void setSelf(Player player) {
        this.clientPlayer = player;
    }

    public void parseNetworkCards(String[] cards) {
        this.clientPlayer.parseNetworkCards(cards);
    }
}
