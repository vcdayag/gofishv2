module io.github.p0lbang.gofish {
    requires javafx.controls;
    requires javafx.fxml;
    requires kryonet;
    requires java.desktop;
    requires com.esotericsoftware.kryo;
    requires org.objenesis;


    opens io.github.p0lbang.gofish to javafx.fxml;
    opens io.github.p0lbang.gofish.network to javafx.fxml;
    exports io.github.p0lbang.gofish;
    exports io.github.p0lbang.gofish.network;
    exports io.github.p0lbang.gofish.game;
    opens io.github.p0lbang.gofish.game to javafx.fxml;
    exports io.github.p0lbang.gofish.network.packets;
    opens io.github.p0lbang.gofish.network.packets to javafx.fxml;

/*    exports io.github.p0lbang.gofish.network;
    opens io.github.p0lbang.gofish.network to javafx.fxml;*/
}