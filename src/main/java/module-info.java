module io.github.p0lbang.gofish {
    requires javafx.controls;
    requires javafx.fxml;
    requires kryonet;
    requires java.desktop;
    requires com.esotericsoftware.kryo;


    opens io.github.p0lbang.gofish to javafx.fxml;
    exports io.github.p0lbang.gofish;
}