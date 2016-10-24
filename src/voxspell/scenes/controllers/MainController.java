package voxspell.scenes.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import voxspell.Voxspell;
import voxspell.engine.DataIO;
import voxspell.engine.LevelData;
import voxspell.engine.SceneManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * MainController class for the application entry / level selection screen (main.fxml)
 */
public class MainController implements Initializable {
    //private static final String BASE = "-fx-border-color: rgb(31,65,9); -fx-border-width: 5px; -fx-border-radius: 1px; -fx-background-color: #b6e7c9; ";
    private ArrayList<Button> buttons = new ArrayList<Button>();

    // data IO
    DataIO data = DataIO.getInstance();

    // initialize buttons from FXML
    @FXML
    private Button achievementsButton;
    @FXML
    private VBox vBox;
    @FXML
    private Button viewStatsButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button musicButton;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label listLabel;
    @FXML
    private Button shopButton;

    /**
     * parse button text into level number
     * @param text
     * @return level
     */
    private int getLevelNumber(String text) {
        String number = text.substring(text.length() - 1);
        if (number.equals("0")) {
            return 10;
        }
        return Integer.parseInt(number);
    }

    // legacy handlers (from java 1.7)

    /**
     * level selection handler to handle level selection button presses
     */
    class levelSelect implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            String text = ((Button)event.getSource()).getText();
            int level = getLevelNumber(text);
            LevelData.setLevel(level);
            SceneManager.goTo("spelling.fxml");
        }
    }

    /**
     * handle change to stats scene
     */
    class statsSelectHandler implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            SceneManager.goTo("stats.fxml");
        }
    }

    /**
     * voice MenuButton handler for voice changing
     */


    /**
     * onHover handler for button styling
     */
    class hoverHandler implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            Button button = (Button)event.getSource();
            button.getStyleClass().add("hover");
        }
    }

    /**
     * onExit handler for button styling
     */
    class exitHandler implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            Button button = (Button)event.getSource();
            button.getStyleClass().remove("hover");
        }
    }

    /**
     * handler for resetting labels
     */
    class resetHandler implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            data.delete();
            disable(1); // disable all levels except the first
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // create listeners
        EventHandler<MouseEvent> levelSelectionHandler = new levelSelect();
        EventHandler<MouseEvent> hoverHandler = new hoverHandler();
        EventHandler<MouseEvent> exitHandler = new exitHandler();
        EventHandler<MouseEvent> statsSelectHandler = new statsSelectHandler();

        // get list and display name
        String listText = LevelData.currentWordFile;
        listText = listText.substring(0, listText.lastIndexOf("."));
        listText = listText.substring(listText.lastIndexOf("/"));
        listText = listText.replaceAll("-|_|/", " ");
        listLabel.setText("current spelling list:" + listText);


        // generate level list dynamically
        int max = 1;
        for (int i = 0; i < Math.ceil((double) LevelData.getMaxLevel() / 3); i++) {
            for (int j = 0; j < 3; j++) {
                if (max < LevelData.getMaxLevel()) {
                    Button button = new Button("Level " + max);
                    gridPane.add(button, j, i);
                    buttons.add(button);
                }
                max++;
            }
        }

        // add buttons to list, then iterate through list assigning listeners


        for (Button button : buttons) {
            classNames.setStyle(button, classNames.Style.LEVEL_BUTTON);
            System.out.println(button.getStyle());
            button.setOnMouseClicked(levelSelectionHandler);
            button.setOnMouseEntered(hoverHandler);
            button.setOnMouseExited(exitHandler);
        }

        classNames.setStyle(settingsButton, classNames.Style.BUTTON, classNames.Style.NEUTRAL);

        // initialise buttons
        viewStatsButton.setOnMouseClicked(statsSelectHandler);
        classNames.setStyle(viewStatsButton, classNames.Style.BUTTON, classNames.Style.SECONDARY);
        // disable locked levels
        if (!LevelData.developerMode) {
            disable(data.highestLevelEnabled());
        }

        // music initialisation
        SceneManager.playMusic();
        String onOff = SceneManager.isMusicPlaying() ? "ON" : "OFF";
        musicButton.setGraphic(new ImageView(new Image(Voxspell.class.getResource("scenes/assets/sound" + onOff + ".png").toExternalForm())));
        musicButton.setOpacity(0.7);
        musicButton.setStyle("-fx-cursor: hand");

        //shop
        classNames.setStyle(shopButton, classNames.Style.BUTTON, classNames.Style.NEUTRAL);
        shopButton.setStyle("-fx-cursor: hand");
        shopButton.setOnMouseClicked((e) -> SceneManager.goTo("shop.fxml"));


        // achievements
        achievementsButton.setOnMouseClicked((e) -> SceneManager.goTo("achievements.fxml"));
        classNames.setStyle(achievementsButton, classNames.Style.BUTTON, classNames.Style.TERTIARY);

        // settings
        settingsButton.setOnMouseClicked((e) -> SceneManager.goTo("settings.fxml"));

        //music button
        musicButton.setOnAction((e) -> {
            if (SceneManager.toggleMusic()) {
                // music is playing now
                musicButton.setGraphic(new ImageView(new Image(Voxspell.class.getResource("scenes/assets/soundON.png").toExternalForm())));
            } else {
                musicButton.setGraphic(new ImageView(new Image(Voxspell.class.getResource("scenes/assets/soundOFF.png").toExternalForm())));
            }
        });
    }

    private void disable(int maxLevel) {
        for (int i = LevelData.getMaxLevel(); i > maxLevel - 1; i--) {
            if (i < buttons.size()) {
                buttons.get(i).setDisable(true);
            }
        }
    }
}
