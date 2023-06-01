package io.github.p0lbang.gofish.network;

import io.github.p0lbang.gofish.game.Player;

public interface ChatInterface {
    void sendMessage(String message);

    void joinServer(String name);

    void GAME_Action();

    void GUI_startGame();

    void checkPlayerCard(Player self, Player player, String playerSelectedRank);
}
