package io.github.p0lbang.gofish;

import io.github.p0lbang.gofish.network.ChatClient;
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
    public ChatClient NetworkClient;
    public ChatServer NetworkServer = null;
    //    public GameBase NetworkClient.gameHandler;
    int CurrentPlayersTurnID = -1;
    String CurrentPlayersTurnName = "";

    Label CurrentPlayerLabel;
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
    private ImageView selectedImageRank = null;
    private double selectedImageRankX;
    private double selectedImageRankY;
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

    public void setCurrentPlayer(int id, String name) {
        CurrentPlayersTurnID = id;
        CurrentPlayersTurnName = name;
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

        // Create an ImageView for the image
        URL imageUrl = Objects.requireNonNull(MainApp.class.getResource("logo.png"));
        Image image = new Image(imageUrl.toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(500); // Set the desired width of the image
        imageView.setPreserveRatio(true); // Preserve the image's aspect ratio

        // Create a VBox to stack the image and buttons vertically
        VBox mainMenuLayout = new VBox(10); // Spacing between image and buttons
        mainMenuLayout.setAlignment(Pos.CENTER);
        mainMenuLayout.getChildren().addAll(imageView, startButton, multiplayerButton, themeButton);


        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        // Background Image
        URL backgroundUrl = Objects.requireNonNull(MainApp.class.getResource("main_menu_bg.png"));
        Image backgroundImage = new Image(backgroundUrl.toString());
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        Background background = new Background(backgroundImg);
        mainMenuLayout.setBackground(background);

        Scene mainMenuScene = new Scene(mainMenuLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    private void showMultiplayerMenu() {
        mainMenuLayout = new VBox(10); // Spacing between buttons

        Label ErrorPrompt = new Label();

        HBox clientnameGroup = new HBox();
        TextField clientName = new TextField();
        clientnameGroup.getChildren().addAll(new Label("Username:"), clientName);
        clientnameGroup.setAlignment(Pos.CENTER);

        HBox ipaddrGroup = new HBox();
        TextField txtipaddr = new TextField();
        txtipaddr.setText("localhost");
        ipaddrGroup.getChildren().addAll(new Label("IP Address:"), txtipaddr);
        ipaddrGroup.setAlignment(Pos.CENTER);

        HBox portGroup = new HBox();
        TextField txtport = new TextField();
        txtport.setText("13700");
        portGroup.getChildren().addAll(new Label("Port:"), txtport);
        portGroup.setAlignment(Pos.CENTER);

        Button clientButton = new Button("Join Server");
        clientButton.setOnAction(evt -> {
            if (txtipaddr.getText().isEmpty() || txtport.getText().isEmpty() || clientName.getText().isEmpty()) {
                ErrorPrompt.setText("Inputs must not be empty.");
                return;
            }
            try {
                int port = Integer.parseInt(txtport.getText());
                if (port <= 1024 || port > 65536) {
                    ErrorPrompt.setText("Invalid port value.");
                    return;
                }
            } catch (NumberFormatException e) {
                ErrorPrompt.setText("Port must be an integer.");
                txtport.requestFocus();
                return;
            }
            this.currentPlayerName = clientName.getText();
            NetworkClient = (ChatClient) new ChatClient(this, clientName.getText(), txtipaddr.getText(), Integer.parseInt(txtport.getText()));
            NetworkClient.joinServer(this.currentPlayerName);
            startGame();
        });

        HBox servernameGroup = new HBox();
        TextField serverName = new TextField();
        servernameGroup.getChildren().addAll(new Label("Username:"), serverName);
        servernameGroup.setAlignment(Pos.CENTER);

        HBox serverportGroup = new HBox();
        TextField txtserverport = new TextField();
        txtserverport.setText("13700");
        serverportGroup.getChildren().addAll(new Label("Port:"), txtserverport);
        serverportGroup.setAlignment(Pos.CENTER);

        Button serverButton = new Button("Create Server");
        serverButton.setOnAction(evt -> {
            if (txtserverport.getText().isEmpty() || serverName.getText().isEmpty()) {
                ErrorPrompt.setText("Inputs must not be empty.");
                return;
            }
            try {
                int port = Integer.parseInt(txtserverport.getText());
                if (port <= 1024 || port > 65536) {
                    ErrorPrompt.setText("Invalid port value.");
                    return;
                }
            } catch (NumberFormatException e) {
                ErrorPrompt.setText("Port must be an integer.");
                txtserverport.requestFocus();
                return;
            }
            this.currentPlayerName = serverName.getText();
//            NetworkClient.gameHandler = new GameServer(this);
            NetworkServer = new ChatServer(this, serverName.getText(), "localhost", Integer.parseInt(txtserverport.getText()));
            NetworkClient = (ChatClient) new ChatClient(this, serverName.getText(), "localhost", Integer.parseInt(txtserverport.getText()));
            NetworkClient.joinServer(this.currentPlayerName);
            startGame();
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(evt -> {
            showMainMenu();
        });


        mainMenuLayout.getChildren().addAll(clientnameGroup, ipaddrGroup, portGroup, clientButton, new Label("or"), servernameGroup, serverportGroup, serverButton, backButton, ErrorPrompt);
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

    public void displayCurrentPlayer() {
        rootLayout.getChildren().removeAll(CurrentPlayerLabel);
        CurrentPlayerLabel = new Label(CurrentPlayersTurnName);
        CurrentPlayerLabel.setTranslateX(-300);
        CurrentPlayerLabel.setTranslateY(0);
        CurrentPlayerLabel.setTextFill(Color.WHITE);
        rootLayout.getChildren().addAll(CurrentPlayerLabel);
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
            int finalI = i;
            imageView.setOnMouseClicked(evt -> {
                if (selectedImageRank != null) {
                    selectedImageRank.setTranslateX(this.selectedImageRankX);
                    selectedImageRank.setTranslateY(this.selectedImageRankY);
                    if (!selectedImageRank.equals(imageView)) {
                        this.playerSelectedRank = Deck[finalI].split(":")[0];
                        this.selectedImageRank = imageView;
                        this.selectedImageRankX = imageView.getTranslateX();
                        this.selectedImageRankY = imageView.getTranslateY();
                        imageView.setTranslateY(this.selectedImageRankY - 20);
                    } else {
                        this.selectedImageRank = null;
                        this.playerSelectedRank = null;
                    }

                } else {
                    this.playerSelectedRank = Deck[finalI].split(":")[0];
                    this.selectedImageRank = imageView;
                    this.selectedImageRankX = imageView.getTranslateX();
                    this.selectedImageRankY = imageView.getTranslateY();
                    imageView.setTranslateY(this.selectedImageRankY - 20);
                }
                displaySelected();
            });
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
            addToChatBar(this.currentPlayerName + ": " + textField.getText());
            NetworkClient.sendMessage(textField.getText());
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
//            NetworkClient.gameHandler = new GameServer(this);
            /*Button gameloop = new Button("Game Loop");
            rootLayout.getChildren().add(gameloop);
            gameloop.setTranslateY(50);
            gameloop.setOnAction(evt -> AITurnAction());*/

            Button DoAction = new Button("Do Action");
            rootLayout.getChildren().add(DoAction);
            DoAction.setTranslateY(100);
            DoAction.setTranslateX(100);
            DoAction.setOnAction(evt -> {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (CurrentPlayersTurnID != NetworkClient.gameHandler.getSelf().getID()) {
                            System.out.println("not your turn");
                            return;
                        }
                        if (playerSelectedTarget.isBlank() || playerSelectedTarget.isEmpty() || playerSelectedRank.isBlank() || playerSelectedRank.isEmpty()) {
                            System.out.println("please select a target and rank");
                            return;
                        }
                        NetworkClient.checkPlayerCard(NetworkClient.gameHandler.getSelf(), NetworkClient.gameHandler.PlayerMap.get(playerSelectedTarget), playerSelectedRank);
                        selectedImageRank = null;
                        playerSelectedRank = "";
                        playerSelectedTarget = "";
                    }
                });
            });
            if (NetworkServer != null) {
                Button StartGame = new Button("Start Game");
                rootLayout.getChildren().add(StartGame);
                StartGame.setTranslateY(100);
                StartGame.setOnAction(evt -> {
                    NetworkServer.GUI_startGame();
                });
            }


//            this.updateUI();

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

    private void AITurnAction() {
        System.out.println("Run Gameloop again");
        //NetworkClient.gameHandler.AITurn();
    }

    public void updateUI() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //display the player's deck
                displayPlayerDeck(NetworkClient.gameHandler.getSelf().getHand());
//                displayRanksSelectionButtons(NetworkClient.gameHandler.getSelf().getHandRanks());
                ArrayList<String> targets = new ArrayList<>(NetworkClient.gameHandler.PlayerMap.keySet());
                displayTargetsSelectionButtons(targets);
                displayTargets(targets);
                displaySelected();
                displayCurrentPlayer();
            }
        });
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

        /*for (int i = 0; i < ranklen; i++) {
            String target = targetplayers.get(i);
            int handCount = NetworkClient.gameHandler.getPlayer(target).getHandCount();

            // Create label for target player
            String lbltext = target
                    + "\n" + handCount + " cards"
                    + "\n" + NetworkClient.gameHandler.getPlayer(target).getCompletedSuits() + " suits completed";
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
        }*/

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

        lbltext = NetworkClient.gameHandler.getSelf().getHandCount() + " cards | "
                + NetworkClient.gameHandler.getSelf().getCompletedSuits() + " suites completed";
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