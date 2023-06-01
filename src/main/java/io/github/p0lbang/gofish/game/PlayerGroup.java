package io.github.p0lbang.gofish.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class PlayerGroup {
    public Map<String, Player> players;
    public ArrayList<String> playerNames;
    public ArrayList<Integer> playerIDS;

    public int PlayerCount;

    public PlayerGroup() {
        this.players = new HashMap<>();
        this.playerNames = new ArrayList<>();
        this.playerIDS = new ArrayList<>();
        this.PlayerCount = 0;
    }

    public void addPlayer(Player player) {
        this.players.put(player.getName(), player);
        this.playerNames.add(player.getName());
        this.playerIDS.add(player.getID());
        this.PlayerCount += 1;
    }

    public void removePlayer(Player player) {
        this.players.remove(player.getName());
        this.playerNames.remove(player.getName());
        this.playerIDS.remove(player.getID());
        this.PlayerCount -= 1;
    }

    public Collection<Player> PlayerList() {
        return this.players.values();
    }

    public ArrayList<String> NameList() {
        return this.playerNames;
    }

    public ArrayList<String> TargetList(Player asker) {
        ArrayList<String> output = new ArrayList<>(this.playerNames);
        output.remove(asker.name);
        return output;
    }

    public Player getPlayer(String playername) {
        return this.players.get(playername);
    }

    public Player getPlayer(int playerid) {
        Player output = null;
        for (Player player : this.players.values()) {
            if (player.getID() == playerid) {
                output = player;
                break;
            }
        }
        return output;
    }

}
