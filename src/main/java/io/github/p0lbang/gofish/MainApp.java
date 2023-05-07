package io.github.p0lbang.gofish;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import io.github.p0lbang.gofish.game.Game;

import java.io.IOException;

//Main class which extends from Application Class
public class MainApp extends Application {

    // This is our PrimaryStage (It contains everything)
    private Stage primaryStage;

    // This is the BorderPane of RootLayout
    @SuppressWarnings("FieldCanBeLocal")
    private StackPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        // 1) Declare a primary stage (Everything will be on this stage)
        this.primaryStage = primaryStage;

        // Optional: Set a title for primary stage
        this.primaryStage.setTitle("Sample JavaFX App");

        // 2) Initialize RootLayout
        initRootLayout();

        Game newg = new Game();

        // 3) Display the EmployeeOperations View
        // showEmployeeOperationsView();
    }

    // Initializes the root layout.
    public void initRootLayout() {
        try {
            // First, load root layout from RootLayout.fxml
            // FXMLLoader loader = new FXMLLoader();
            // loader.setLocation(App.class.getResource("view/RootLayout.fxml"));
            // rootLayout = (BorderPane) loader.load();

            Button btn1 = new Button("Button 1 on bottom ");
            Button btn2 = new Button("Button 2 on top");
            rootLayout = new StackPane();
            rootLayout.getChildren().addAll(btn1, btn2);
            
            // Second, show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, 200, 200);
            primaryStage.setScene(scene); // Set the scene in primary stage.

            // Third, show the primary stage
            primaryStage.show(); // Display the primary stage
        } catch (Exception e) {
            e.printStackTrace();
        }
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