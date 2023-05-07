package io.github.p0lbang.gofish.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerGroup {
    Map<String, Player> players;
    ArrayList<String> playerNames;

    public PlayerGroup() {
        this.players = new HashMap<String, Player>();
        this.playerNames = new ArrayList<String>();
    }

    public void addPlayer(Player player) {
        this.players.put(player.getName(), player);
        this.playerNames.add(player.getName());
    }

    public void removePlayer(Player player) {
        this.players.remove(player.getName());
        this.playerNames.remove(player.getName());
    }

    public Collection<Player> PlayerList() {
        return this.players.values();
    }

    public ArrayList<String> NameList() {
        return this.playerNames;
    }

    public ArrayList<String> TargetList(Player asker) {
        ArrayList<String> output = new ArrayList<>();
        output.addAll(this.playerNames);
        output.remove(asker.name);
        return output;
    }

    public Player getPlayer(String playername) {
        return this.players.get(playername);
    }

}
