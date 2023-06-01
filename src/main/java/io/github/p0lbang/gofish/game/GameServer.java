package io.github.p0lbang.gofish.game;

import io.github.p0lbang.gofish.MainApp;
import io.github.p0lbang.gofish.network.ChatServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameServer extends GameBase {
    @SuppressWarnings("CanBeFinal")
    public Deck deck;

    @SuppressWarnings("CanBeFinal")

    public int TurnIndex;

    ChatServer NETWORK;


    public GameServer(MainApp gui, ChatServer network) {
        super(gui);
        this.deck = new Deck();
        this.deck.initializeDeck();
        this.TurnIndex = 0;
        this.NETWORK = network;

        this.start();
    }

    public void start() {

    }

    public Player getNextPlayer() {
        return this.players.getPlayer(this.players.playerIDS.get(TurnIndex++ % this.players.PlayerCount));
    }

    public Player getCurrentPlayer() {
        return this.players.getPlayer(this.players.playerIDS.get(TurnIndex % this.players.PlayerCount));
    }

    private void getPlayers() {
        this.players.addPlayer(new Player("Juan"));
        this.players.addPlayer(new Player("Bot"));
        this.players.addPlayer(new Player("Player"));
    }

    public void setupCards() {
        int amountofcards = 10;
        Random rand = new Random();
        for (int index = 0; index < amountofcards; index++) {
            for (Player currentPlayer : this.players.PlayerList()) {
                currentPlayer.addCard(deck.getRandomCard());
            }
        }
    }

    public void gameloop() {
        for (String playerName : this.players.playerNames) {
            Player asker = this.players.getPlayer(playerName);
            /* check players hand if empty. if true immediately go fish. */
            if (asker.hand.isEmpty()) {
                this.playerGoFish(asker);
                continue;
            }
            Map<String, String> askingOutput = asker.ask(this.players.TargetList(asker));

            /*System.out.println("Asker: " + asker.name + " | Target: " + askingOutput.get("target"));
            asker.displayHand();*/

            this.checkPlayerCard(asker, this.players.getPlayer(askingOutput.get("target")),
                    askingOutput.get("rank"));

            asker.displayHand();
        }

        System.out.println();

        /*for (String playerName : this.players.NameList()) {
            Player asker = this.players.getPlayer(playerName);
            asker.displayAll();
        }*/
    }

    public void AITurn() {
        ArrayList<String> others = this.getTargetPlayers("Player");
        for (String playerName : others) {
            Player asker = this.players.getPlayer(playerName);
            /* check players hand if empty. if true immediately go fish. */
            if (asker.hand.isEmpty()) {
                this.playerGoFish(asker);
                continue;
            }
            Map<String, String> askingOutput = asker.ask(this.players.TargetList(asker));

            //System.out.println("Asker: " + asker.name + " | Target: " + askingOutput.get("target"));
//            asker.displayHand();

            this.checkPlayerCard(asker, this.players.getPlayer(askingOutput.get("target")),
                    askingOutput.get("rank"));

//            asker.displayHand();
        }

        System.out.println();

   /*     for (String playerName : this.players.NameList()) {
            Player asker = this.players.getPlayer(playerName);
            asker.displayAll();
        }*/
    }

    // game funcitons

    private void getCard(Player asker, Player target, String rank) {
        ArrayList<Card> stolencards = target.giveCards(rank);
        if (this.GUI.currentPlayerName.equals(target.getName())) {
            for (Card card : stolencards) {
                this.GUI.takeCardAnimation(card.stringify());
            }
        }

        asker.addMultipleCards(stolencards);
//        NETWORK.GAME_sendPlayerTurn(this.getCurrentPlayer());
    }

    private void playerGoFish(Player asker) {
        if (this.deck.isEmpty()) {
            System.out.println("Deck is Empty.");
        } else {
            asker.addCard(this.deck.getRandomCard());
        }
//        NETWORK.GAME_sendPlayerTurn(this.getNextPlayer());
    }

    public void checkPlayerCard(Player asker, Player target, String rank) {
        if (!target.checkHand(rank)) {
            System.out.println("Go fish");
            this.playerGoFish(asker);
            return;
        }

        System.out.println("Stolen");
        this.getCard(asker, target, rank);
    }

    public String NETWORK_checkPlayerCard(int askerid, int targetid, String rank) {
        Player asker = this.players.getPlayer(askerid);
        Player target = this.players.getPlayer(targetid);

        if (!target.checkHand(rank)) {
            System.out.println("Go fish");
            this.playerGoFish(asker);
            return "End";
        }

        System.out.println("Stolen");
        this.getCard(asker, target, rank);
        return "Again";
    }

    public void displayPlayerHand(String playername) {
        Player choosen = this.players.getPlayer(playername);
        choosen.displayHand();
    }


    public String[] getPlayerHand(String playername) {
        Player chosen = this.players.getPlayer(playername);
        return chosen.getHand();
    }

    public String[] getPlayerHandRanks(String playername) {
        Player chosen = this.players.getPlayer(playername);
        return chosen.getHandRanks();
    }

    public ArrayList<String> getTargetPlayers(String askername) {
        Player asker = this.players.getPlayer(askername);
        return this.players.TargetList(asker);
    }

    public HashMap<String, Player> getTargetPlayersMap(String askername) {

        HashMap<String, Player> targets = new HashMap<String, Player>(this.players.players);
        targets.remove(askername);
        System.out.println("gettargetplayersmap");
        System.out.println(targets.size());
        return targets;
    }

    public Player getPlayer(String playername) {
        return this.players.getPlayer(playername);
    }

    public void Network_AddPlayer(String playername, int id) {
        Player newplayer = new Player(playername, id);
        this.players.addPlayer(newplayer);
    }
}
