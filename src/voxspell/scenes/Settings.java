package voxspell.scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import voxspell.engine.DataIO;
import voxspell.engine.Festival;
import voxspell.engine.LevelData;
import voxspell.engine.SceneManager;

import java.util.ArrayList;

/**
 * Settings popup to manage voice, sound and level data features
 * Created by nhur714 on 10/9/2016.
 */
public class Settings {
    Stage stage = new Stage();
    VBox vBox = new VBox(5);

    MenuButton voiceMenuButton = new MenuButton();
    Button resetButton = new Button();
    Button enableAllButton = new Button();
    Button musicToggle = new Button();

    DataIO data = new DataIO();

    ArrayList<Button> buttons; // reference to level buttons

    private static final String NZ_VOICE_CHOICE = "NZ Voice";
    private static final String DEFAULT_VOICE_CHOICE = "Default Voice";

    public Settings(ArrayList<Button> buttonList) {
        buttons = buttonList;
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setWidth(150);
        stage.setHeight(180);
        stage.setTitle("Settings");
        stage.setResizable(false);

        updateMusicToggle();

        resetButton.setText("Reset levels");
        resetButton.setStyle("-fx-background-color: red; -fx-cursor: hand");

        enableAllButton.setText("(Evaluation only)\nUnlock all levels");
        enableAllButton.setStyle("-fx-background-color: #cf7869; -fx-cursor: hand; -fx-font-size: 9");

        vBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(voiceMenuButton, musicToggle, resetButton, enableAllButton);
        vBox.setBackground(SceneManager.makeAmbientBackground());
        Scene scene = new Scene(vBox);
        stage.setScene(scene);

        setUp();
    }

    public void show() {
        stage.showAndWait();
    }

    private void updateMusicToggle() {
        String musicText = SceneManager.enableMusic ? "Disable Music" : "Enable Music";
        String musicColor = SceneManager.enableMusic ? "#ff7f7f" : "#7fbf7f";
        musicToggle.setText(musicText);
        musicToggle.setStyle("-fx-cursor: hand; -fx-background-color: " + musicColor);
    }

    class voiceMenuButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            switch((String)((MenuItem)actionEvent.getSource()).getUserData()) { // userData is voice type
                case Festival.DEFAULT:
                    LevelData.setVoice(Festival.DEFAULT);
                    voiceMenuButton.setText(DEFAULT_VOICE_CHOICE);
                    break;
                case Festival.NZ:
                    LevelData.setVoice(Festival.NZ);
                    voiceMenuButton.setText(NZ_VOICE_CHOICE);
                    break;
                default:
                    LevelData.setVoice(Festival.DEFAULT); // set to default if invalid voice
                    voiceMenuButton.setText(DEFAULT_VOICE_CHOICE);
                    break;
            }
        }
    }

    public void setUp() {
        voiceMenuButton.setText("Change voice");
        EventHandler<ActionEvent> voiceMenuButtonHandler = new voiceMenuButtonHandler();
        MenuItem defaultVoice = new MenuItem(DEFAULT_VOICE_CHOICE);
        defaultVoice.setUserData(Festival.DEFAULT);
        MenuItem nzVoice = new MenuItem(NZ_VOICE_CHOICE);
        nzVoice.setUserData(Festival.NZ);
        voiceMenuButton.getItems().setAll(defaultVoice, nzVoice);

        defaultVoice.setOnAction(voiceMenuButtonHandler);
        nzVoice.setOnAction(voiceMenuButtonHandler);

        enableAllButton.setOnMouseClicked((e) -> {
            for (Button button : buttons) {
                button.setDisable(false);
            }
        });

        musicToggle.setOnMouseClicked((e) -> {
            SceneManager.enableMusic = !SceneManager.enableMusic;
            if (SceneManager.enableMusic) {
                SceneManager.playMusic();
            } else {
                SceneManager.stopMusic();
            }
            updateMusicToggle();
        });

        resetButton.setOnMouseClicked(new resetHandler());
    }

    class resetHandler implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            data.delete();
            disable(1); // disable all levels except the first
        }
    }

    private void disable(int maxLevel) {
        for (int i = 9; i > maxLevel - 1; i--) {
            buttons.get(i).setDisable(true);
        }
    }
}
