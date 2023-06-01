package io.github.p0lbang.gofish.game;

import io.github.p0lbang.gofish.MainApp;

import java.util.ArrayList;
import java.util.HashMap;

public class GameBase {
    MainApp GUI;
    Player clientPlayer;

    public PlayerGroup players;

    public ArrayList<String> targetPlayers;

    public HashMap<String, Player> PlayerMap;

    public GameBase(MainApp gui) {
        this.GUI = gui;
        this.players = new PlayerGroup();
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
