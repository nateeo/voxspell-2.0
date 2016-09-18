package voxspell.scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import voxspell.engine.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the spelling quiz scene
 * Created by nateeo on 16/09/16.
 */
public class SpellingController implements Initializable {

    @FXML
    private Button submitButton;
    @FXML
    private Button listenAgainButton;
    @FXML
    private Text levelLabel;
    @FXML
    private TextField inputTextField;
    @FXML
    private TextArea outputTextArea;

    // plug in engine modules
    private DataIO data = new DataIO();
    private Festival festival = new Festival();

    private ArrayList<Word> words;
    private Word currentWord;

    class listenAgainHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            // call to festival to repeat word
            festival.read(currentWord, false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize words and level label using levelData
        int level = LevelData.getLevel();
        WordList wordList = new WordList(level);
        words = wordList.getWords();
        levelLabel.setText("Level " + level);

        // set up outputTextArea
        outputTextArea.setEditable(false);
        outputTextArea.setFocusTraversable(false);
        inputTextField.requestFocus();

        //set up buttons
        listenAgainButton.setDisable(true);
    }
}
