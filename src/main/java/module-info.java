module io.github.p0lbang.gofish {
    requires javafx.controls;
    requires javafx.fxml;
    requires kryonet;
    requires java.desktop;
    requires com.esotericsoftware.kryo;


    opens io.github.p0lbang.gofish to javafx.fxml;
    exports io.github.p0lbang.gofish;
    exports io.github.p0lbang.gofish.exp2;
    opens io.github.p0lbang.gofish.exp2 to javafx.fxml;
/*    exports io.github.p0lbang.gofish.network;
    opens io.github.p0lbang.gofish.network to javafx.fxml;*/
}