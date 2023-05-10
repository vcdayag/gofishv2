package io.github.p0lbang.gofish;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import io.github.p0lbang.gofish.game.Game;

import java.io.IOException;
import java.util.ArrayList;

//Main class which extends from Application Class
public class MainApp extends Application {

    // This is our PrimaryStage (It contains everything)
    private Stage primaryStage;

    // This is the BorderPane of RootLayout
    @SuppressWarnings("FieldCanBeLocal")
    private StackPane rootLayout;

    Game gameLogic;
    @Override
    public void start(Stage primaryStage) {
        // 1) Declare a primary stage (Everything will be on this stage)
        this.primaryStage = primaryStage;

        // Optional: Set a title for primary stage
        this.primaryStage.setTitle("Go Fish");

        // 2) Initialize RootLayout
        initRootLayout();

        // 3) Display the EmployeeOperations View
        // showEmployeeOperationsView();
    }

    public void createSelectionButtons(String[] Ranks){
        ArrayList<Button> buttonlist = new ArrayList<>();

        int ranklen = 13;
        int halfrank = ranklen / 2;
        int transval = 25;

        for (int i = 0; i < ranklen ; i++) {
            Button temp = new Button(Ranks[i]);
            buttonlist.add(temp);
            temp.setOnAction(evt -> buttonClick(temp));
            temp.setTranslateX((i-halfrank)*transval);
        }
        rootLayout = new StackPane();
        rootLayout.getChildren().addAll(buttonlist);
    }

    // Initializes the root layout.
    public void initRootLayout() {
        try {
            gameLogic = new Game();
            createSelectionButtons(gameLogic.deck.RANKS);

            // Second, show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, 500, 500);
            primaryStage.setScene(scene); // Set the scene in primary stage.

            // Third, show the primary stage
            primaryStage.show(); // Display the primary stage
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buttonClick(Button button) {
        System.out.println(button.getText());
    }

    // Shows the employee operations view inside the root layout.
    // public void showEmployeeOperationsView() {
    //     try {
    //         // First, load EmployeeOperationsView from EmployeeOperations.fxml
    //         FXMLLoader loader = new FXMLLoader();
    //         loader.setLocation(App.class.getResource("view/EmployeeOperations.fxml"));
    //         AnchorPane employeeOperationsView = (AnchorPane) loader.load();

    //         // Set Employee Operations view into the center of root layout.
    //         rootLayout.setCenter(employeeOperationsView);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}