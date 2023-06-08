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
    private HashMap<ImageView, double[]> playerDeckImageViewsSelected = new HashMap<>();
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
    private String theme_clicked;

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
        displayCurrentPlayer();
    }

    private void showMainMenu() {
        Button multiplayerButton = new Button("Start Game");
        multiplayerButton.setOnAction(evt -> {
            showMultiplayerMenu();
        });

        Button themeButton = new Button("Choose Theme");
        themeButton.setOnAction(evt -> chooseTheme());

        Button aboutButton = new Button("About the game");
        aboutButton.setOnAction(evt -> showaboutMenu());

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(evt -> Platform.exit());

        String buttonStyle = "-fx-background-color: #007bff; " +
                "-fx-text-fill: #ffffff; " +
                "-fx-background-radius: 20; " +
                "-fx-border-color: #00aaff; " +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20;";

        String hoverStyle = "-fx-background-color: #00aaff;";

        multiplayerButton.setStyle(buttonStyle);
        multiplayerButton.setOnMouseEntered(e -> multiplayerButton.setStyle(buttonStyle + hoverStyle));
        multiplayerButton.setOnMouseExited(e -> multiplayerButton.setStyle(buttonStyle));

        themeButton.setStyle(buttonStyle);
        themeButton.setOnMouseEntered(e -> themeButton.setStyle(buttonStyle + hoverStyle));
        themeButton.setOnMouseExited(e -> themeButton.setStyle(buttonStyle));

        aboutButton.setStyle(buttonStyle);
        aboutButton.setOnMouseEntered(e -> aboutButton.setStyle(buttonStyle + hoverStyle));
        aboutButton.setOnMouseExited(e -> aboutButton.setStyle(buttonStyle));

        exitButton.setStyle(buttonStyle);
        exitButton.setOnMouseEntered(e -> exitButton.setStyle(buttonStyle + hoverStyle));
        exitButton.setOnMouseExited(e -> exitButton.setStyle(buttonStyle));

        // Create an ImageView for the image
        URL imageUrl = Objects.requireNonNull(MainApp.class.getResource("logo.png"));
        Image image = new Image(imageUrl.toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(500); // Set the desired width of the image
        imageView.setPreserveRatio(true); // Preserve the image's aspect ratio

        // Create a VBox to stack the image and buttons vertically
        VBox mainMenuLayout = new VBox(10); // Spacing between image and buttons
        mainMenuLayout.setAlignment(Pos.CENTER);
        mainMenuLayout.getChildren().addAll(imageView, multiplayerButton,themeButton,aboutButton,exitButton);


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

    private void showaboutMenu() {

        Button backButton = new Button("Back");
        backButton.setOnAction(evt -> {
            showMainMenu();
        });

        Label paragraph = new Label("Object of the Game\n" +
                "The goal is to win the most \"books\" of cards. A book is any four of a kind, such as four\n" +
                "kings, four aces, and so on."+"\n \n"+
                "The Standard Play (Easy)\n" +
                "The current player can ask any opponent, for example,\"Give me your kings,\" usually\n" +
                "addressing the opponent by name and specifying the rank that they want, from ace\n" +
                "down to two.\n" +
                "The player who is fishing must have at least one card of the rank that was asked for in\n" +
                "their hand. The player who is addressed must hand over all the cards requested. If the\n" +
                "player has none, they say, \"Go fish!\" and the player who made the request draws the\n" +
                "top card of the stock and places it in their hand.\n" +
                "The player can ask for the same card or a different one to any of the opponents. So\n" +
                "long as the player succeeds in getting cards (makes a catch), their turn continues.\n" +
                "When a player makes a catch, they must reveal the card so that the catch is verified. If\n" +
                "a player gets the fourth card of a book, the player shows all four cards, places them on\n" +
                "the table face up in front of everyone, and plays again.\n" +
                "\n" +
                "If the player goes fishing without \"making a catch\" (does not receive a card he asked\n" +
                "for), the turn passes to the next player.\n" +
                "The game ends when all thirteen books have been won. The winner is the player with\n" +
                "the most books. During the game, if a player is left without cards, they may (when it's\n" +
                "their turn to play), draw from the stock, and then ask for cards of that rank. If there are\n" +
                "no cards left in the stock, they are out of the game.");

        // Set background color and transparency for overlay effect

        paragraph.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-text-fill: white; -fx-font-size: 16px;");






        // Create a VBox to stack the image and buttons vertically
        VBox mainMenuLayout = new VBox(10); // Spacing between image and buttons
        mainMenuLayout.setAlignment(Pos.CENTER);
        mainMenuLayout.getChildren().addAll(paragraph,backButton);


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
            NetworkClient = new ChatClient(this, clientName.getText(), txtipaddr.getText(), Integer.parseInt(txtport.getText()));
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
            NetworkClient = new ChatClient(this, serverName.getText(), "localhost", Integer.parseInt(txtserverport.getText()));
            NetworkClient.joinServer(this.currentPlayerName);
            startGame();
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(evt -> showMainMenu());


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
            theme_clicked = "animal";
        });

        Button fruitThemeButton = new Button("Fruit Theme");
        fruitThemeButton.setOnAction(evt -> {
            theme_clicked = "fruit";
        });

        Button defaultThemeButton = new Button("Default Theme");
        defaultThemeButton.setOnAction(evt -> {
            theme_clicked = "default";
        });

        Button okButton = new Button("Apply");
        okButton.setOnAction(evt -> {
            theme = theme_clicked;
            resetThemeSelectionPage();
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(evt -> {
            showMainMenu();
        });



        URL url = Objects.requireNonNull(MainApp.class.getResource("/io/github/p0lbang/gofish/" + theme + "_pack/" + "A_S" + ".png"));
        Image cardImage = new Image(url.toString());
        ImageView imageView = new ImageView(cardImage);
        imageView.setFitWidth(100);
        imageView.setFitHeight(150);

        URL url2 = Objects.requireNonNull(MainApp.class.getResource("/io/github/p0lbang/gofish/" + theme + "_pack/" + "background" + ".png"));
        Image bgimage = new Image(url2.toString());
        ImageView imageView2 = new ImageView(bgimage);
        imageView2.setFitWidth(75);
        imageView2.setFitHeight(125);






        String buttonStyle = "-fx-background-color: #007bff; " +
                "-fx-text-fill: #ffffff; " +
                "-fx-background-radius: 20; " +
                "-fx-border-color: #00aaff; " +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20;";

        String hoverStyle = "-fx-background-color: #00aaff;";

        okButton.setStyle(buttonStyle);
        okButton.setOnMouseEntered(e -> okButton.setStyle(buttonStyle + hoverStyle));
        okButton.setOnMouseExited(e -> okButton.setStyle(buttonStyle));

        backButton.setStyle(buttonStyle);
        backButton.setOnMouseEntered(e -> backButton.setStyle(buttonStyle + hoverStyle));
        backButton.setOnMouseExited(e -> backButton.setStyle(buttonStyle));

        animalThemeButton.setStyle(buttonStyle);
        animalThemeButton.setOnMouseEntered(e -> animalThemeButton.setStyle(buttonStyle + hoverStyle));
        animalThemeButton.setOnMouseExited(e -> animalThemeButton.setStyle(buttonStyle));

        defaultThemeButton.setStyle(buttonStyle);
        defaultThemeButton.setOnMouseEntered(e -> defaultThemeButton.setStyle(buttonStyle + hoverStyle));
        defaultThemeButton.setOnMouseExited(e -> defaultThemeButton.setStyle(buttonStyle));

        fruitThemeButton.setStyle(buttonStyle);
        fruitThemeButton.setOnMouseEntered(e -> fruitThemeButton.setStyle(buttonStyle + hoverStyle));
        fruitThemeButton.setOnMouseExited(e -> fruitThemeButton.setStyle(buttonStyle));

        HBox themeContainer = new HBox(10);
        themeContainer.getChildren().addAll(imageView,imageView2);
        themeContainer.setAlignment(Pos.CENTER);

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(animalThemeButton, fruitThemeButton, defaultThemeButton);
        buttonContainer.setAlignment(Pos.CENTER);

        HBox buttonContainer2 = new HBox(10);
        buttonContainer2.getChildren().addAll(okButton,backButton);
        buttonContainer2.setAlignment(Pos.CENTER);


        VBox themeMenuLayout = new VBox(20);
        themeMenuLayout.setAlignment(Pos.CENTER);
        themeMenuLayout.getChildren().addAll(themeContainer,buttonContainer,buttonContainer2);


        URL url3 = Objects.requireNonNull(MainApp.class.getResource("main_menu_bg.png"));
        Image backgroundImage = new Image(url3.toString());

        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        BackgroundImage backgroundImageObject = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        Background background = new Background(backgroundImageObject);
        themeMenuLayout.setBackground(background); // Set background to the main menu layout

        Scene scene = new Scene(themeMenuLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);


    }

    private void resetThemeSelectionPage() {
        chooseTheme();
    }

    private void startGame() {
        initRootLayout();
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
        for (ImageView imageView : this.playerDeckImageViews) {
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
        Platform.runLater(() -> {
            Text text;
            if (chatLayout.getChildren().size() == 0) {
                text = new Text(chatinput);
            } else {
                // Add new line if not the first child
                text = new Text("\n" + chatinput);
            }
            chatLayout.getChildren().add(text);
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

            Button DoAction = new Button("Do Action");
            rootLayout.getChildren().add(DoAction);
            DoAction.setTranslateY(100);
            DoAction.setTranslateX(100);
            DoAction.setOnAction(evt -> Platform.runLater(() -> {
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
            }));
            if (NetworkServer != null) {
                Button StartGame = new Button("Start Game");
                rootLayout.getChildren().add(StartGame);
                StartGame.setTranslateY(100);
                StartGame.setOnAction(evt -> {
                    NetworkServer.GUI_startGame();
                    rootLayout.getChildren().remove(StartGame);
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

    public void updateUI() {
        Platform.runLater(() -> {
            //display the player's deck
            displayPlayerDeck(NetworkClient.gameHandler.getSelf().getHand());
            ArrayList<String> targets = new ArrayList<>(NetworkClient.gameHandler.PlayerMap.keySet());
            displayTargetsSelectionButtons(targets);
            displayTargets(targets);
            displaySelected();
            displayCurrentPlayer();
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

        for (int i = 0; i < ranklen; i++) {
            String target = targetplayers.get(i);
            int handCount = NetworkClient.gameHandler.PlayerMap.get(target).getHandCount();

            // Create label for target player
            String lbltext = target
                    + "\n" + handCount + " cards"
                    + "\n" + NetworkClient.gameHandler.PlayerMap.get(target).getCompletedSuits() + " suits completed";
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

        lbltext = NetworkClient.gameHandler.getSelf().getHandCount() + " cards | "
                + NetworkClient.gameHandler.getSelf().getCompletedSuits() + " suites completed";
        temp = new Label(lbltext);
        temp.setTranslateX(-300);
        temp.setTranslateY(initialY + 25);
        temp.setTextFill(Color.WHITE);
        this.playerInfoLabels.add(temp);

        rootLayout.getChildren().addAll(this.playerInfoLabels);
    }

}