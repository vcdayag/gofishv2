package io.github.p0lbang.gofish.network.packets;

public class PacketPlayerAction {
    public int askerID;
    public String asker;
    public String target;
    public int targetID;
    public String rank;
    public int amount;

    public PacketPlayerAction(int askerID) {
        this.askerID = askerID;
    }
}
