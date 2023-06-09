package io.github.p0lbang.gofish.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import io.github.p0lbang.gofish.game.Card;
import io.github.p0lbang.gofish.game.Deck;
import io.github.p0lbang.gofish.game.Player;
import io.github.p0lbang.gofish.game.PlayerGroup;
import io.github.p0lbang.gofish.network.packets.*;

import java.util.ArrayList;
import java.util.HashMap;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
    // This registers objects that are going to be sent over the network.
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(String.class);
        kryo.register(Integer.class);
        kryo.register(int[].class);
        kryo.register(String[].class);
        kryo.register(ArrayList.class);
        kryo.register(HashMap.class);

        kryo.register(Player.class);
        kryo.register(PlayerGroup.class);
        kryo.register(Deck.class);
        kryo.register(Card.class);
        kryo.register(PacketChatMessage.class);
        kryo.register(PacketUpdatePlayerDetails.class);
        kryo.register(PacketPlayerAction.class);
        kryo.register(PacketPlayerJoin.class);
        kryo.register(PacketPlayerTurn.class);
        kryo.register(PacketPlayersWaiting.class);
        kryo.register(PacketStartGame.class);
        kryo.register(PacketPlayerNoCard.class);

    }

}
