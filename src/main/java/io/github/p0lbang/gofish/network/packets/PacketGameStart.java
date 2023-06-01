package io.github.p0lbang.gofish.network.packets;

import io.github.p0lbang.gofish.game.Player;
import io.github.p0lbang.gofish.game.PlayerGroup;

import java.util.ArrayList;

public class PacketGameStart {
//    public String playername;
//    public int playerID;
//    public String[] cards;

    public Player player;
    public PlayerGroup playerGroup;
    public ArrayList<String> targets;
}
