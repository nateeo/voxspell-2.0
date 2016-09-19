package voxspell.scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import voxspell.engine.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the spelling quiz scene
 * Created by nhur714 on 16/09/16.
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
    @FXML
    private Label rightLabel;
    @FXML
    private Label wrongLabel;
    @FXML
    private Label progressLabel;


    // plug in engine modules
    private DataIO data = new DataIO();
    private Festival festival = new Festival();

    // current quiz
    private ArrayList<Word> words;
    private Word currentWord;
    private boolean currentFaulted = false;

    /**
     * listener to handle listen-again requests
     */
    class listenAgainHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            // call to festival to repeat word
            festival.read(currentWord, false);
        }
    }

    /**
     * listener to handle enter key submits
     */
    class enterSubmitHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            submit();
        }
    }

    /**
     * listener to handle submits with the "Go!" button
     */
    class submitHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            // get user input and check/sanitize
            submit();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize words and level label using levelData
        int level = LevelData.getLevel();

        WordList wordList = new WordList(level);
        words = wordList.getWords();
        LevelData.setCurrentWordList(words); // set global application state
        currentWord = words.get(0);

        levelLabel.setText("Level " + level);

        // set up outputTextArea
        outputTextArea.setEditable(false);
        outputTextArea.setFocusTraversable(false);
        inputTextField.requestFocus();

        // reset labels and buttons
        reset();

        // add listeners
        submitButton.setOnMouseClicked(new submitHandler());
        inputTextField.setOnAction(new enterSubmitHandler());

        // start quiz
        readWord(currentWord);
    }

    private void readWord(Word word) {
        System.out.println(word); // in place of festival

    }

    private void submit() {
        String userInput = inputTextField.getText().trim();
        boolean valid = checkInput(userInput);
        if (valid) {
            // check word

        } else {
            // invalid input
            inputTextField.clear();
            inputTextField.requestFocus();
        }
    }

    /**
     * checks userInput for apostrophes and valid characters, returning true if valid
     * @param userInput
     * @return
     */
    private boolean checkInput(String userInput) {
        if (!userInput.matches("[a-zA-Z]+")) {
            boolean wordHasApostrophe = currentWord.toString().contains("'");
            boolean userInputHasApostrophe = userInput.contains("'");
            if (wordHasApostrophe && !userInputHasApostrophe) {
                // word contains an apostrophe but userInput does not
                outputTextArea.setText("Oops, that's wrong. The word has an apostrophe (')");
                return false;
            } else if (!wordHasApostrophe && userInputHasApostrophe) {
                // word does not contain an apostrophe but userInput does
                outputTextArea.setText("Oops, that's wrong. The word does NOT have an apostrophe (')");
                return false;
            } else {
                // invalid input
                outputTextArea.setText("Oops, you entered something wrong. Try again.");
                return false;
            }
        }
        return true;
    }

    /**
     * reset labels and counts
     */
    private void reset() {
        listenAgainButton.setDisable(true);
        rightLabel.setText("0");
        wrongLabel.setText("0");
        progressLabel.setText("1");

        currentFaulted = false;
    }
}
