package io.github.p0lbang.gofish;

import io.github.p0lbang.gofish.game.Game;
import io.github.p0lbang.gofish.game.Player;
import io.github.p0lbang.gofish.network.ChatClient;
import io.github.p0lbang.gofish.network.ChatInterface;
import io.github.p0lbang.gofish.network.ChatServer;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

//Main class which extends from Application Class
public class MainApp extends Application {

    final int WINDOW_HEIGHT = 700;
    final int WINDOW_WIDTH = 1200;
    public String currentPlayerName = "Player";
    ChatInterface chatInterface;
    Game gameLogic;
    // This is our PrimaryStage (It contains everything)
    private Stage primaryStage;
    // This is the BorderPane of RootLayout
    @SuppressWarnings("FieldCanBeLocal")
    private StackPane rootLayout;
    private BorderPane mainLayout;
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<ImageView> playerDeckImageViews = new ArrayList<>();
    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<ImageView, double[]> playerDeckImageViewsSelected = new HashMap<ImageView, double[]>();
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Button> playerRanksButtons = new ArrayList<>();
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Button> playerTargetsButtons = new ArrayList<>();
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Label> TargetsLabels = new ArrayList<>();
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Label> playerInfoLabels = new ArrayList<>();
    private String playerSelectedRank = "";
    private String playerSelectedTarget = "";
    private String theme = "animal";
    private VBox mainMenuLayout;
    private TextFlow chatLayout;

    @Override
    public void start(Stage primaryStage) {
        // 1) Declare a primary stage (Everything will be on this stage)
        this.primaryStage = primaryStage;

        // Optional: Set a title for primary stage
        this.primaryStage.setTitle("Go Fish");


        showMainMenu();


    }

    private void showMainMenu() {
        Button startButton = new Button("Start Game");
        startButton.setOnAction(evt -> {
            startGame();
        });

        Button multiplayerButton = new Button("Multiplayer");
        multiplayerButton.setOnAction(evt -> {
            showMultiplayerMenu();
        });

        Button themeButton = new Button("Choose Theme");
        themeButton.setOnAction(evt -> chooseTheme());

        mainMenuLayout = new VBox(10); // Spacing between buttons
        mainMenuLayout.getChildren().addAll(startButton, multiplayerButton, themeButton);
        mainMenuLayout.setAlignment(Pos.CENTER);

        // Background Image
        URL url = Objects.requireNonNull(MainApp.class.getResource("main_menu_bg.png"));
        Image backgroundImage = new Image(url.toString());

        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        BackgroundImage backgroundImageObject = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        Background background = new Background(backgroundImageObject);
        mainMenuLayout.setBackground(background); // Set background to the main menu layout

        Scene mainMenuScene = new Scene(mainMenuLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    private void showMultiplayerMenu() {
        mainMenuLayout = new VBox(10); // Spacing between buttons
        HBox ipaddrGroup = new HBox();
        HBox portGroup = new HBox();
        TextField txtipaddr = new TextField();
        TextField txtport = new TextField();

        ipaddrGroup.getChildren().addAll(new Label("IP Address:"), txtipaddr);
        ipaddrGroup.setAlignment(Pos.CENTER);

        portGroup.getChildren().addAll(new Label("Port:"), txtport);
        portGroup.setAlignment(Pos.CENTER);

        Button clientButton = new Button("Join Server");
        clientButton.setOnAction(evt -> {
            if (txtipaddr.getText().isEmpty() || txtport.getText().isEmpty()) {
                return;
            }
            try {
                Integer.parseInt(txtport.getText());
            } catch (NumberFormatException e) {
                return;
            }
            chatInterface = new ChatClient(this, "Client", txtipaddr.getText(), Integer.parseInt(txtport.getText()));
            startGame();
        });

        Button serverButton = new Button("Create Server");
        serverButton.setOnAction(evt -> {
            chatInterface = new ChatServer(this, "Server");
            startGame();
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(evt -> {
            showMainMenu();
        });

        mainMenuLayout.getChildren().addAll(ipaddrGroup, portGroup, clientButton, new Label("or"), serverButton, backButton);
        mainMenuLayout.setAlignment(Pos.CENTER);

        // Background Image
        URL url = Objects.requireNonNull(MainApp.class.getResource("main_menu_bg.png"));
        Image backgroundImage = new Image(url.toString());

        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        BackgroundImage backgroundImageObject = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        Background background = new Background(backgroundImageObject);
        mainMenuLayout.setBackground(background); // Set background to the main menu layout

        Scene mainMenuScene = new Scene(mainMenuLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    public void chooseTheme() {
        Button animalThemeButton = new Button("Animal Theme");
        animalThemeButton.setOnAction(evt -> {
            theme = "animal";
        });

        Button fruitThemeButton = new Button("Fruit Theme");
        fruitThemeButton.setOnAction(evt -> {
            theme = "fruit";
        });

        Button defaultThemeButton = new Button("Default Theme");
        defaultThemeButton.setOnAction(evt -> {
            theme = "default";
        });

        Button okButton = new Button("OK");
        okButton.setOnAction(evt -> {
            showMainMenu();
        });

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(animalThemeButton, fruitThemeButton, defaultThemeButton);
        buttonContainer.setAlignment(Pos.CENTER);

        VBox themeMenuLayout = new VBox(10);
        themeMenuLayout.setAlignment(Pos.CENTER);
        themeMenuLayout.getChildren().addAll(buttonContainer, okButton);


        URL url = Objects.requireNonNull(MainApp.class.getResource("main_menu_bg.png"));
        Image backgroundImage = new Image(url.toString());

        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        BackgroundImage backgroundImageObject = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        Background background = new Background(backgroundImageObject);
        themeMenuLayout.setBackground(background); // Set background to the main menu layout

        Scene scene = new Scene(themeMenuLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
    }


    private void startGame() {
        initRootLayout();
    }

    public void RanksSelectionAction(String rank) {
        this.playerSelectedRank = rank;
        displaySelected();
    }

    public void displayRanksSelectionButtons(String[] Ranks) {
        rootLayout.getChildren().removeAll(playerRanksButtons);
        playerRanksButtons.clear();

        int ranklen = Ranks.length;
        int halfrank = ranklen / 2;
        int transval = 25;

        for (int i = 0; i < ranklen; i++) {
            Button temp = new Button(Ranks[i]);
            playerRanksButtons.add(temp);
            int finalI = i;
            temp.setOnAction(evt -> RanksSelectionAction(Ranks[finalI]));
            temp.setTranslateX((i - halfrank) * transval);
        }

        rootLayout.getChildren().addAll(playerRanksButtons);
    }

    public void TargetSelectionAction(String asker, String target) {
        this.playerSelectedTarget = target;
        this.displaySelected();
    }

    public void DoActionAction() {
        Player Pasker = gameLogic.getPlayer(this.currentPlayerName);
        Player Ptarget = gameLogic.getPlayer(this.playerSelectedTarget);
        gameLogic.checkPlayerCard(Pasker, Ptarget, this.playerSelectedRank);
        this.updateUI();
        this.AITurnAction();
    }

    public void displayTargetsSelectionButtons(ArrayList<String> Targets) {
        rootLayout.getChildren().removeAll(playerTargetsButtons);
        playerTargetsButtons.clear();

        int ranklen = Targets.size();
        int halfrank = ranklen / 2;
        int transval = 50;

        for (int i = 0; i < ranklen; i++) {
            Button temp = new Button(Targets.get(i));
            playerTargetsButtons.add(temp);
            int finalI = i;
            temp.setOnAction(evt -> TargetSelectionAction(this.currentPlayerName, Targets.get(finalI)));
            temp.setTranslateX((i - halfrank) * transval);
            temp.setTranslateY(50);
        }

        rootLayout.getChildren().addAll(playerTargetsButtons);
    }


    public void displayPlayerDeck(String[] Deck) {
        rootLayout.getChildren().removeAll(playerDeckImageViews);
        playerDeckImageViews.clear();

        int ranklen = Deck.length;
        int halfrank = ranklen / 2;
        int transval = 40;

        for (int i = 0; i < ranklen; i++) {
            String cardName = Deck[i].replace(":", "_");
            URL url = Objects.requireNonNull(MainApp.class.getResource("/io/github/p0lbang/gofish/" + theme + "_pack/" + cardName + ".png"));
            Image cardImage = new Image(url.toString());
            ImageView imageView = new ImageView(cardImage);
            imageView.setFitWidth(75);
            imageView.setFitHeight(125);
            imageView.setOnMouseClicked(evt -> giveCardAnimation(imageView));
            imageView.setTranslateX((i - halfrank) * transval);
            imageView.setTranslateY(200);
            imageView.setId(Deck[i]);
            imageView.setStyle("-fx-border-color: red; -fx-border-width: 5px;");
            rootLayout.getChildren().add(imageView);
            playerDeckImageViews.add(imageView);
        }
    }

    public void takeCardAnimation(String cardid) {
        for (int i = 0; i < this.playerDeckImageViews.size(); i++) {
            ImageView imageView = this.playerDeckImageViews.get(i);
            if (imageView.getId().equals(cardid)) {
                this.giveCardAnimation(imageView);
            }
        }
    }

    public void giveCardAnimation(ImageView imageView) {
        // Make imageView not clickable during transition
        imageView.setDisable(true);
        // Don't change imageView opacity when disabled
        imageView.setStyle("-fx-opacity: 1;");
        TranslateTransition transCenter = new TranslateTransition(new Duration(400), imageView);
        TranslateTransition transHide = new TranslateTransition(new Duration(400), imageView);
        TranslateTransition transPause = new TranslateTransition(new Duration(200), imageView);
        transCenter.setInterpolator(Interpolator.EASE_BOTH);
        transHide.setInterpolator(Interpolator.EASE_BOTH);

        SequentialTransition seqT = new SequentialTransition(transCenter);
        // On animation finish, enable imageView
        seqT.setOnFinished(e -> imageView.setDisable(false));

        if (playerDeckImageViewsSelected.containsKey(imageView)) {
            double[] coords = playerDeckImageViewsSelected.get(imageView);
            transCenter.setToX(coords[0]);
            transCenter.setToY(coords[1]);
            playerDeckImageViewsSelected.remove(imageView);
        } else {
            seqT = new SequentialTransition(transCenter, transPause, transHide);
            transCenter.setToX(0);
            transCenter.setToY(0);
            transHide.setToY(-500);

            double[] d = {imageView.getTranslateX(), imageView.getTranslateY()};
            playerDeckImageViewsSelected.put(imageView, d);
        }
        seqT.play();
    }

    public void addToChatBar(String chatinput) {
//        required to wrap gui code into this platform runlater to work
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Text text;
                if (chatLayout.getChildren().size() == 0) {
                    text = new Text(chatinput);
                } else {
                    // Add new line if not the first child
                    text = new Text("\n" + chatinput);
                }
                chatLayout.getChildren().add(text);
            }
        });

    }

    private void initChatmenu() {
        ScrollPane sp = new ScrollPane();
        chatLayout = new TextFlow();
        chatLayout.setLineSpacing(10);
        TextField textField = new TextField();
        textField.setPrefSize(150, 30);
        Button button = new Button("Send");
        button.setPrefSize(80, 30);
        VBox container = new VBox();
        VBox box = new VBox();
        box.getChildren().addAll(sp, chatLayout);
        container.setPadding(new Insets(10));
        container.getChildren().addAll(box, new HBox(textField, button));


        VBox.setVgrow(sp, Priority.ALWAYS);
        VBox.setVgrow(chatLayout, Priority.ALWAYS);

        // On Enter press
        textField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                button.fire();
            }
        });

        button.setOnAction(e -> {
            addToChatBar(textField.getText());
            chatInterface.sendMessage(textField.getText());
            textField.clear();
            textField.requestFocus();
        });
        VBox vb = new VBox();
        vb.getChildren().addAll(chatLayout);
        sp.setVmax(440);
        sp.setPrefSize(200, 300);
        sp.setContent(vb);
        sp.vvalueProperty().bind(vb.heightProperty());
        sp.setPannable(true);
        mainLayout.setLeft(container);
    }

    // Initializes the root layout.
    public void initRootLayout() {
        try {
            rootLayout = new StackPane();
            mainLayout = new BorderPane();
            mainLayout.setCenter(rootLayout);
            gameLogic = new Game(this);
            /*Button gameloop = new Button("Game Loop");
            rootLayout.getChildren().add(gameloop);
            gameloop.setTranslateY(50);
            gameloop.setOnAction(evt -> AITurnAction());*/

            Button DoAction = new Button("Do Action");
            rootLayout.getChildren().add(DoAction);
            DoAction.setTranslateY(100);
            DoAction.setOnAction(evt -> DoActionAction());

            this.updateUI();

            //Background Image
            URL url = Objects.requireNonNull(MainApp.class.getResource("/io/github/p0lbang/gofish/" + theme + "_pack/background.png"));
            Image backgroundImage = new Image(url.toString());

            BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
            BackgroundImage backgroundImageObject = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
            Background background = new Background(backgroundImageObject);
            rootLayout.setBackground(background);
            /////////////////////////////////////////////////////////////
            // Second, show the scene containing the root layout.
            initChatmenu();
            Scene scene = new Scene(mainLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
            primaryStage.setScene(scene); // Set the scene in primary stage.
            primaryStage.setResizable(false);

            // Third, show the primary stage
            primaryStage.show(); // Display the primary stage
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buttonClick(Button button) {
        System.out.println(button.getText());
        gameLogic.displayPlayerHand("Bot");


    }

    private void AITurnAction() {
        System.out.println("Run Gameloop again");
        gameLogic.AITurn();
    }

    void updateUI() {
        //display the player's deck
        displayPlayerDeck(gameLogic.getPlayerHand(this.currentPlayerName));
        displayRanksSelectionButtons(gameLogic.getPlayerHandRanks(this.currentPlayerName));
        displayTargetsSelectionButtons(gameLogic.getTargetPlayers(this.currentPlayerName));
        displayTargets(gameLogic.getTargetPlayers(this.currentPlayerName));
        displaySelected();
    }

    void displayTargets(ArrayList<String> targetplayers) {
        ArrayList<ImageView> targetImages = new ArrayList<>();
        rootLayout.getChildren().removeAll(TargetsLabels);
        TargetsLabels.clear();

        rootLayout.getChildren().removeAll(targetImages);
        TargetsLabels.clear();

        int ranklen = targetplayers.size();
        int halfrank = ranklen / 2;
        int transval = 200;
        int transvalcard = 220;
        int transval2 = 10;
        int imageWidth = 30;
        int imageHeight = 30;

        for (int i = 0; i < ranklen; i++) {
            String target = targetplayers.get(i);
            int handCount = gameLogic.getPlayer(target).getHandCount();

            // Create label for target player
            String lbltext = target
                    + "\n" + handCount + " cards"
                    + "\n" + gameLogic.getPlayer(target).getCompletedSuits() + " suits completed";
            Label temp = new Label(lbltext);
            temp.setTranslateY(-270);
            temp.setTranslateX((i - halfrank) * transval);
            temp.setTextFill(Color.WHITE);

            // Add the label to the root layout
            TargetsLabels.add(temp);

            for (int j = 0; j < handCount; j++) {
                URL url = Objects.requireNonNull(MainApp.class.getResource("back_card.png"));
                Image backCardImage = new Image(url.toString());
                ImageView backCardImageImageView = new ImageView(backCardImage);
                backCardImageImageView.setFitWidth(imageWidth);
                backCardImageImageView.setFitHeight(imageHeight);
                backCardImageImageView.setTranslateX((i - halfrank) * transvalcard + j * transval2);
                backCardImageImageView.setTranslateY(-220); // Keep the Y translation constant to maintain the same vertical position

                // Add the image view to the ArrayList
                targetImages.add(backCardImageImageView);
            }
        }

        rootLayout.getChildren().addAll(TargetsLabels);
        rootLayout.getChildren().addAll(targetImages); // Add the ImageView instances to the root layout
    }

    void displaySelected() {
        rootLayout.getChildren().removeAll(this.playerInfoLabels);
        this.playerInfoLabels.clear();

        int initialY = 50;

        String lbltext = "Selected Rank: " + this.playerSelectedRank + " | "
                + "Selected Target: " + this.playerSelectedTarget;
        Label temp = new Label(lbltext);
        temp.setTranslateX(-300);
        temp.setTranslateY(initialY);
        temp.setTextFill(Color.WHITE);
        this.playerInfoLabels.add(temp);

        lbltext = gameLogic.getPlayer(this.currentPlayerName).getHandCount() + " cards | "
                + gameLogic.getPlayer(this.currentPlayerName).getCompletedSuits() + " suites completed";
        temp = new Label(lbltext);
        temp.setTranslateX(-300);
        temp.setTranslateY(initialY + 25);
        temp.setTextFill(Color.WHITE);
        this.playerInfoLabels.add(temp);

        /*lbltext = "Selected Rank: " + this.playerSelectedRank + " | "
                + "Selected Target: " + this.playerSelectedTarget;
        temp = new Label(lbltext);
        temp.setTranslateX(-300);
        temp.setTranslateY(initialY+50);
        this.playerInfoLabels.add(temp);*/

        rootLayout.getChildren().addAll(this.playerInfoLabels);
    }

}