package voxspell.scenes.controllers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import voxspell.engine.DataIO;
import voxspell.engine.Festival;
import voxspell.engine.LevelData;
import voxspell.engine.SceneManager;

/**
 * SettingsController manage voice, sound and level data features
 * Created by nhur714 on 10/9/2016.
 */
public class SettingsController implements Initializable {

    @FXML
    MenuButton voiceMenuButton;
    @FXML
    Button resetButton;
    @FXML
    Button enableAllButton;
    @FXML
    Button changeList;
    @FXML
    Button mainMenuButton;
    @FXML
    Label statusLabel;
    @FXML
    VBox vBox;

    DataIO data = DataIO.getInstance();

    ArrayList<Button> buttons; // reference to level buttons

    private static final String NZ_VOICE_CHOICE = "NZ Voice";
    private static final String DEFAULT_VOICE_CHOICE = "Default Voice";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vBox.setBackground(SceneManager.makeBackground());
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
            LevelData.developerMode = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("All levels temporarily unlocked");
            alert.setTitle("Success!");
            alert.setContentText("Unlocked all levels for the current session");
            alert.showAndWait();
        });

        resetButton.setOnMouseClicked((e) -> {
            data.resetAchievements();
            data.resetStats();
        });

        changeList.setOnMouseClicked((e) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showOpenDialog(changeList.getScene().getWindow());
            if (file != null) {
                LevelData.setCurrentWordFile(file.getPath());
                LevelData.calculateMaxLevel(); // update new max level
                data.loadAll();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Successfully changed the spelling list!");
                alert.setTitle("Success!");
                alert.setContentText("Loaded previous data associated with this spelling list (if any)");
                alert.showAndWait();

            }
        });

        mainMenuButton.setOnAction((e) -> SceneManager.goTo("main.fxml"));
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
}
