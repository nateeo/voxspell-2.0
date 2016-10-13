package voxspell.scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import voxspell.engine.DataIO;
import voxspell.engine.Festival;
import voxspell.engine.LevelData;
import voxspell.engine.SceneManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static voxspell.scenes.classNames.setStyle;
import static voxspell.scenes.classNames.Style;

/**
 * MainController class for the application entry / level selection screen (main.fxml)
 */
public class MainController implements Initializable {
    //private static final String BASE = "-fx-border-color: rgb(31,65,9); -fx-border-width: 5px; -fx-border-radius: 1px; -fx-background-color: #b6e7c9; ";

    private ArrayList<Button> buttons = new ArrayList<Button>();

    // data IO
    DataIO data = new DataIO();

    // initialize buttons from FXML
    @FXML
    private Button achievementsButton;
    @FXML
    private VBox vBox;
    @FXML
    private Button level1;
    @FXML
    private Button level2;
    @FXML
    private Button level3;
    @FXML
    private Button level4;
    @FXML
    private Button level5;
    @FXML
    private Button level6;
    @FXML
    private Button level7;
    @FXML
    private Button level8;
    @FXML
    private Button level9;
    @FXML
    private Button level10;
    @FXML
    private Button viewStatsButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button musicButton;

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
        vBox.setBackground(SceneManager.makeBackground());
        // create listeners
        EventHandler<MouseEvent> levelSelectionHandler = new levelSelect();
        EventHandler<MouseEvent> hoverHandler = new hoverHandler();
        EventHandler<MouseEvent> exitHandler = new exitHandler();
        EventHandler<MouseEvent> statsSelectHandler = new statsSelectHandler();

        // add buttons to list, then iterate through list assigning listeners
        buttons.add(level1);
        buttons.add(level2);
        buttons.add(level3);
        buttons.add(level4);
        buttons.add(level5);
        buttons.add(level6);
        buttons.add(level7);
        buttons.add(level8);
        buttons.add(level9);
        buttons.add(level10);

        for (Button button : buttons) {
            setStyle(button, Style.LEVEL_BUTTON);
            System.out.println(button.getStyle());
            button.setOnMouseClicked(levelSelectionHandler);
            button.setOnMouseEntered(hoverHandler);
            button.setOnMouseExited(exitHandler);
        }

        settingsButton.setOnAction((e) -> {
            SceneManager.goTo("settings.fxml");
        });
        setStyle(settingsButton, Style.BUTTON, Style.NEUTRAL);

        // initialise buttons
        viewStatsButton.setOnMouseClicked(statsSelectHandler);
        setStyle(viewStatsButton, Style.BUTTON, Style.SECONDARY);

        // disable locked levels
        disable(data.highestLevelEnabled());
        SceneManager.playMusic();


        // achievements
        achievementsButton.setOnMouseClicked((e) -> SceneManager.goTo("achievements.fxml"));
        setStyle(achievementsButton, Style.BUTTON, Style.TERTIARY);

        //music button
        musicButton.setOnAction((e) -> {
            if (SceneManager.toggleMusic()) {
                // music is playing now
                musicButton.setText("MUSIC ON");
            } else {
                musicButton.setText("MUSIC OFF");
            }
        });
    }

    private void disable(int maxLevel) {
        for (int i = 9; i > maxLevel - 1; i--) {
            buttons.get(i).setDisable(true);
        }
    }
}
