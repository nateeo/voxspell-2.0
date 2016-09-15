package voxspell.scenes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import voxspell.engine.LevelData;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by nateeo on 16/09/16.
 */
public class SpellingController implements Initializable {

    @FXML
    private Button submitButton;
    @FXML
    private Text levelLabel;
    @FXML
    private TextField inputTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize using levelData
        int level = LevelData.getLevel();
        levelLabel.setText("Level " + level);
    }
}
