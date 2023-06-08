package io.github.p0lbang.gofish.network.packets;

import io.github.p0lbang.gofish.game.Player;
import io.github.p0lbang.gofish.game.PlayerGroup;

import java.util.ArrayList;
import java.util.HashMap;

public class PacketUpdatePlayerDetails {

    public Player player;
    public PlayerGroup playerGroup;
    public ArrayList<String> targets;

    public HashMap<String, Player> PlayerMap;
}
